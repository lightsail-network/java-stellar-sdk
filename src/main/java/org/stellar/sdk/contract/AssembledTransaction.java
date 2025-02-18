package org.stellar.sdk.contract;

import static org.stellar.sdk.Auth.authorizeEntry;
import static org.stellar.sdk.SorobanServer.assembleTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.*;
import org.stellar.sdk.TimeBounds;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.contract.exception.*;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.operations.Operation;
import org.stellar.sdk.operations.RestoreFootprintOperation;
import org.stellar.sdk.responses.sorobanrpc.GetTransactionResponse;
import org.stellar.sdk.responses.sorobanrpc.SendTransactionResponse;
import org.stellar.sdk.responses.sorobanrpc.SimulateTransactionResponse;
import org.stellar.sdk.xdr.*;

public class AssembledTransaction<T> {
  private final SorobanServer server;
  private final int submitTimeout;

  private final KeyPair transactionSigner;
  private final Function<SCVal, T> parseResultXdrFn;

  private final TransactionBuilder transactionBuilder;
  @Getter private Transaction builtTransaction;

  @Getter private SimulateTransactionResponse simulation;
  private SimulateTransactionResponse.SimulateHostFunctionResult simulationResult;
  private SorobanTransactionData simulationTransactionData;

  @Getter private SendTransactionResponse sendTransactionResponse;
  @Getter private GetTransactionResponse getTransactionResponse;

  /**
   * Creates a new AssembledTransaction.
   *
   * @param transactionBuilder the transaction builder
   * @param server the Soroban server
   * @param transactionSigner the keypair to sign the transaction with
   * @param parseResultXdrFn the function to parse the result XDR
   * @param submitTimeout the timeout for submitting the transaction
   */
  public AssembledTransaction(
      TransactionBuilder transactionBuilder,
      SorobanServer server,
      @Nullable KeyPair transactionSigner,
      @Nullable Function<SCVal, T> parseResultXdrFn,
      int submitTimeout) {
    this.server = server;
    this.submitTimeout = submitTimeout;
    this.transactionSigner = transactionSigner;
    this.parseResultXdrFn = parseResultXdrFn;
    this.transactionBuilder = transactionBuilder;
  }

  /**
   * Simulates the transaction on the network. Must be called before signing or submitting the
   * transaction. Will automatically restore required contract state if <code>restore</code> to
   * <code>true</code> and this is not a read call.
   *
   * @param restore whether to automatically restore contract state if needed
   * @return this AssembledTransaction
   * @throws SimulationFailedException if the simulation failed
   * @throws RestorationFailureException if the contract state could not be restored
   */
  public AssembledTransaction<T> simulate(boolean restore) {
    simulationResult = null;
    simulationTransactionData = null;

    TransactionBuilderAccount source =
        server.getAccount(transactionBuilder.getSourceAccount().getAccountId());
    transactionBuilder.getSourceAccount().setSequenceNumber(source.getSequenceNumber());

    Transaction builtTx = transactionBuilder.build();
    simulation = server.simulateTransaction(builtTx);

    if (restore && simulation.getRestorePreamble() != null && !isReadCall()) {
      try {
        restoreFootprint();
      } catch (SimulationFailedException
          | TransactionStillPendingException
          | SendTransactionFailedException
          | TransactionFailedException e) {
        throw new RestorationFailureException("Failed to restore contract data.", this);
      }
      return simulate(false);
    }

    if (simulation.getError() != null) {
      throw new SimulationFailedException(
          "Transaction simulation failed: " + simulation.getError(), this);
    }
    builtTransaction = assembleTransaction(builtTx, simulation);
    return this;
  }

  /**
   * Signs and submits the transaction in one step.
   *
   * <p>A convenience method combining {@link #sign(KeyPair, boolean)} and {@link #submit()}.
   *
   * @param transactionSigner the keypair to sign the transaction with, or <code>null</code> to use
   *     the signer provided in the constructor
   * @param force whether to sign and submit even if the transaction is a read call
   * @return The value returned by the invoked function, parsed if <code>parseResultXdrFn</code> was
   *     set, otherwise raw {@link SCVal}
   * @throws NotYetSimulatedException if the transaction has not yet been simulated
   * @throws NoSignatureNeededException if the transaction is a read call, and <code>force</code> is
   *     not set to <code>true</code>
   * @throws NeedsMoreSignaturesException if the transaction requires more signatures
   * @throws SendTransactionFailedException if sending the transaction to the network failed
   * @throws TransactionStillPendingException if the transaction is still pending after the timeout
   * @throws ExpiredStateException if the transaction requires restoring contract state
   * @throws TransactionFailedException if the transaction failed
   */
  public T signAndSubmit(@Nullable KeyPair transactionSigner, boolean force) {
    sign(transactionSigner, force);
    return submit();
  }

  /**
   * Signs the transaction.
   *
   * @param transactionSigner the keypair to sign the transaction with, or <code>null</code> to use
   *     the signer provided in the constructor
   * @param force whether to sign and submit even if the transaction is a read call
   * @return this AssembledTransaction
   * @throws NotYetSimulatedException if the transaction has not yet been simulated
   * @throws NoSignatureNeededException if the transaction is a read call, and <code>force</code> is
   *     not set to <code>true</code>
   * @throws ExpiredStateException if the transaction requires restoring contract state
   * @throws NeedsMoreSignaturesException if the transaction requires more signatures
   */
  public AssembledTransaction<T> sign(@Nullable KeyPair transactionSigner, boolean force) {
    if (builtTransaction == null) {
      throw new NotYetSimulatedException("Transaction has not yet been simulated.", this);
    }

    if (!force && isReadCall()) {
      throw new NoSignatureNeededException(
          "This is a read call. It requires no signature or submitting. Set force=true to sign and submit anyway.",
          this);
    }

    if (simulation != null && simulation.getRestorePreamble() != null) {
      throw new ExpiredStateException(
          "You need to restore contract state before you can invoke this method. "
              + "You can set `restore` to true in order to "
              + "automatically restore the contract state when needed.",
          this);
    }

    KeyPair signer = transactionSigner != null ? transactionSigner : this.transactionSigner;
    if (signer == null) {
      throw new IllegalArgumentException(
          "You must provide a signTransactionFunc to sign the transaction, either here or in the constructor.");
    }

    Set<String> sigsNeeded = needsNonInvokerSigningBy(false);
    sigsNeeded.removeIf(s -> s.startsWith("C"));
    if (!sigsNeeded.isEmpty()) {
      throw new NeedsMoreSignaturesException(
          "Transaction requires signatures from "
              + sigsNeeded
              + ". See `needsNonInvokerSigningBy` for details.",
          this);
    }

    builtTransaction.sign(signer);
    return this;
  }

  /**
   * Signs the transaction's authorization entries.
   *
   * <p>An alias for {@link #signAuthEntries(KeyPair, Long)} with <code>null</code> as the second
   * argument.
   *
   * @param authEntriesSigner the keypair to sign the authorization entries with
   * @return this AssembledTransaction
   * @throws NotYetSimulatedException if the transaction has not yet been simulated
   */
  public AssembledTransaction<T> signAuthEntries(KeyPair authEntriesSigner) {
    return signAuthEntries(authEntriesSigner, null);
  }

  /**
   * Signs the transaction's authorization entries.
   *
   * @param authEntriesSigner the keypair to sign the authorization entries with
   * @param validUntilLedgerSequence the ledger sequence number until which the authorization
   *     entries are valid, or <code>null</code> to set it to the current ledger sequence + 100
   * @return this AssembledTransaction
   * @throws NotYetSimulatedException if the transaction has not yet been simulated
   */
  public AssembledTransaction<T> signAuthEntries(
      KeyPair authEntriesSigner, @Nullable Long validUntilLedgerSequence) {
    if (builtTransaction == null) {
      throw new NotYetSimulatedException("Transaction has not yet been simulated.", this);
    }

    if (validUntilLedgerSequence == null) {
      validUntilLedgerSequence = server.getLatestLedger().getSequence() + 100L;
    }

    Operation op = builtTransaction.getOperations()[0];
    if (!(op instanceof InvokeHostFunctionOperation)) {
      throw new IllegalStateException("Expected InvokeHostFunction operation");
    }
    InvokeHostFunctionOperation invokeHostFunctionOp = (InvokeHostFunctionOperation) op;

    for (int i = 0; i < invokeHostFunctionOp.getAuth().size(); i++) {
      SorobanAuthorizationEntry e = invokeHostFunctionOp.getAuth().get(i);
      if (SorobanCredentialsType.SOROBAN_CREDENTIALS_SOURCE_ACCOUNT.equals(
          e.getCredentials().getDiscriminant())) {
        continue;
      }
      if (e.getCredentials().getAddress() == null) {
        throw new IllegalStateException("Expected address in credentials");
      }
      if (!Address.fromSCAddress(e.getCredentials().getAddress().getAddress())
          .toString()
          .equals(authEntriesSigner.getAccountId())) {
        continue;
      }
      invokeHostFunctionOp
          .getAuth()
          .set(
              i,
              authorizeEntry(
                  e, authEntriesSigner, validUntilLedgerSequence, builtTransaction.getNetwork()));
    }
    return this;
  }

  /**
   * Get the addresses that need to sign the authorization entries.
   *
   * @param includeAlreadySigned whether to include addresses that have already signed the
   *     authorization entries
   * @return The addresses that need to sign the authorization entries.
   * @throws NotYetSimulatedException if the transaction has not yet been simulated
   */
  public Set<String> needsNonInvokerSigningBy(boolean includeAlreadySigned) {
    if (builtTransaction == null) {
      throw new NotYetSimulatedException("Transaction has not yet been simulated.", this);
    }

    Operation op = builtTransaction.getOperations()[0];
    if (!(op instanceof InvokeHostFunctionOperation)) {
      return new HashSet<>();
    }
    InvokeHostFunctionOperation invokeHostFunctionOp = (InvokeHostFunctionOperation) op;

    return invokeHostFunctionOp.getAuth().stream()
        .filter(
            entry ->
                entry.getCredentials().getDiscriminant()
                    == SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
        .filter(
            entry ->
                includeAlreadySigned
                    || entry.getCredentials().getAddress().getSignature().getDiscriminant()
                        == SCValType.SCV_VOID)
        .map(
            entry ->
                Address.fromSCAddress(entry.getCredentials().getAddress().getAddress()).toString())
        .collect(Collectors.toSet());
  }

  /**
   * Get the result of the function invocation from the simulation.
   *
   * @return The value returned by the invoked function, parsed if <code>parseResultXdrFn</code> was
   *     set, otherwise raw {@link SCVal}
   * @throws NotYetSimulatedException if the transaction has not yet been simulated
   */
  public T result() throws NotYetSimulatedException {
    SimulationData simulationData = simulationData();
    SCVal rawResult;
    try {
      rawResult = SCVal.fromXdrBase64(simulationData.result.getXdr());
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to convert simulation result to SCVal", e);
    }
    if (parseResultXdrFn != null) {
      return parseResultXdrFn.apply(rawResult);
    }
    @SuppressWarnings("unchecked")
    T result = (T) rawResult;
    return result;
  }

  /**
   * Check if the transaction is a read call.
   *
   * @return <code>true</code> if the transaction is a read call, <code>false</code> otherwise
   * @throws NotYetSimulatedException if the transaction has not yet been simulated
   */
  public boolean isReadCall() {
    SimulationData simulationData = simulationData();
    List<String> auths = simulationData.result.getAuth();
    LedgerKey[] writes =
        simulationData.transactionData.getResources().getFootprint().getReadWrite();
    return auths.isEmpty() && writes.length == 0;
  }

  /**
   * Get the transaction envelope XDR.
   *
   * @return The transaction envelope XDR.
   */
  public String toEnvelopeXdrBase64() {
    return builtTransaction.toEnvelopeXdrBase64();
  }

  /**
   * Restore the contract state.
   *
   * @throws TransactionFailedException if the transaction failed
   * @throws TransactionStillPendingException if the transaction is still pending after the timeout
   * @throws SendTransactionFailedException if sending the transaction to the network failed
   */
  public void restoreFootprint() {
    if (transactionSigner == null) {
      throw new IllegalArgumentException(
          "For automatic restore to work you must provide a transactionSigner when initializing AssembledTransaction.");
    }

    TransactionBuilder restoreTx =
        new TransactionBuilder(
                transactionBuilder.getSourceAccount(), transactionBuilder.getNetwork())
            .setBaseFee(transactionBuilder.getBaseFee())
            .addOperation(RestoreFootprintOperation.builder().build())
            .setSorobanData(
                new SorobanDataBuilder(simulation.getRestorePreamble().getTransactionData())
                    .build())
            .addPreconditions(
                TransactionPreconditions.builder().timeBounds(new TimeBounds(0, 0)).build());
    AssembledTransaction<SCVal> restoreAssembled =
        new AssembledTransaction<>(restoreTx, server, transactionSigner, null, submitTimeout);
    restoreAssembled.simulate(false).sign(this.transactionSigner, true).submitInternal();
  }

  /**
   * Submits the transaction to the network.
   *
   * <p>It will send the transaction to the network and wait for the result.
   *
   * @return The value returned by the invoked function, parsed if <code>parseResultXdrFn</code> was
   *     set, otherwise raw {@link SCVal}
   * @throws NotYetSimulatedException if the transaction has not yet been simulated
   * @throws SendTransactionFailedException if sending the transaction to the network failed
   * @throws TransactionStillPendingException if the transaction is still pending after the timeout
   * @throws TransactionFailedException if the transaction failed
   */
  @SuppressWarnings("unchecked")
  public T submit() {
    GetTransactionResponse response = submitInternal();
    TransactionMeta transactionMeta;
    try {
      transactionMeta = TransactionMeta.fromXdrBase64(response.getResultMetaXdr());
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "Unable to convert transaction meta to TransactionMeta", e);
    }
    SCVal resultVal = transactionMeta.getV3().getSorobanMeta().getReturnValue();
    return parseResultXdrFn != null ? parseResultXdrFn.apply(resultVal) : (T) resultVal;
  }

  private SimulationData simulationData() {
    if (simulationResult != null && simulationTransactionData != null) {
      return new SimulationData(simulationResult, simulationTransactionData);
    }

    if (simulation == null) {
      throw new NotYetSimulatedException("Transaction has not yet been simulated.", this);
    }

    simulationResult = simulation.getResults().get(0);
    try {
      simulationTransactionData =
          SorobanTransactionData.fromXdrBase64(simulation.getTransactionData());
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "Unable to convert transaction data to SorobanTransactionData", e);
    }
    return new SimulationData(simulationResult, simulationTransactionData);
  }

  private GetTransactionResponse submitInternal() {
    if (builtTransaction == null) {
      throw new NotYetSimulatedException("Transaction has not yet been simulated.", this);
    }

    if (sendTransactionResponse == null) {
      sendTransactionResponse = server.sendTransaction(builtTransaction);
      if (sendTransactionResponse.getStatus()
          != SendTransactionResponse.SendTransactionStatus.PENDING) {
        throw new SendTransactionFailedException(
            "Sending the transaction to the network failed!", this);
      }
    }

    String txHash = sendTransactionResponse.getHash();
    List<GetTransactionResponse> attempts =
        withExponentialBackoff(
            () -> server.getTransaction(txHash),
            resp -> resp.getStatus() == GetTransactionResponse.GetTransactionStatus.NOT_FOUND,
            submitTimeout);
    getTransactionResponse = attempts.get(attempts.size() - 1);

    if (getTransactionResponse.getStatus() == GetTransactionResponse.GetTransactionStatus.SUCCESS) {
      return getTransactionResponse;
    }
    if (getTransactionResponse.getStatus()
        == GetTransactionResponse.GetTransactionStatus.NOT_FOUND) {
      throw new TransactionStillPendingException(
          "Waited "
              + submitTimeout
              + " seconds for transaction to complete, but it did not. "
              + "Returning anyway. You can call result() to await the result later "
              + "or check the status of the transaction manually.",
          this);
    } else if (getTransactionResponse.getStatus()
        == GetTransactionResponse.GetTransactionStatus.FAILED) {
      throw new TransactionFailedException("Transaction failed.", this);
    } else {
      throw new IllegalStateException("Unexpected transaction status.");
    }
  }

  private static <T> List<T> withExponentialBackoff(
      Supplier<T> fn, Predicate<T> keepWaitingIf, long timeout) {
    List<T> attempts = new ArrayList<>();
    attempts.add(fn.get());
    if (!keepWaitingIf.test(attempts.get(0))) {
      return attempts;
    }

    long waitUntil = System.currentTimeMillis() + timeout * 1000;
    long waitTime = 1000;
    long maxWaitTime = 60000;

    while (System.currentTimeMillis() < waitUntil
        && keepWaitingIf.test(attempts.get(attempts.size() - 1))) {
      try {
        CompletableFuture<T> future = CompletableFuture.supplyAsync(fn);
        future.get(waitTime, TimeUnit.MILLISECONDS);
        attempts.add(future.getNow(null));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new UnexpectedException("Exponential backoff interrupted", e);
      } catch (ExecutionException | TimeoutException e) {
        // Ignore the exception and continue with the next iteration
      }

      waitTime *= 2;
      if (waitTime > maxWaitTime) {
        waitTime = maxWaitTime;
      }

      if (waitUntil - System.currentTimeMillis() < waitTime) {
        waitTime = waitUntil - System.currentTimeMillis();
      }
    }
    return attempts;
  }

  @Value
  private static class SimulationData {
    SimulateTransactionResponse.SimulateHostFunctionResult result;
    SorobanTransactionData transactionData;
  }
}
