package org.stellar.sdk.contract;

import static org.stellar.sdk.Auth.authorizeEntry;
import static org.stellar.sdk.SorobanServer.assembleTransaction;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.*;
import org.stellar.sdk.Auth;
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

/**
 * Represents an assembled Stellar smart contract invocation transaction.
 *
 * <p>This class manages the full lifecycle of a contract call: building the transaction, simulating
 * it via Stellar RPC, managing authorization entries, signing, submitting, and polling for the
 * result. It also handles automatic restoration of expired contract state when needed.
 *
 * <p>Use {@link AssembledTransaction#isReadCall()} to check whether the invocation is read-only
 * (simulation-only) or requires on-chain submission.
 *
 * @param <T> the type of the parsed result from the contract function invocation
 * @see org.stellar.sdk.SorobanServer
 */
public class AssembledTransaction<T> {
  /** Delay before the second {@code getTransaction} poll; grows by half after every poll. */
  private static final long INITIAL_POLL_DELAY_MILLIS = 1_000L;

  /**
   * Upper bound on the delay between two {@code getTransaction} polls: about one ledger. A
   * transaction's status only changes when a ledger closes (roughly every 5 seconds), so a longer
   * gap would only delay the result without saving a meaningful number of requests.
   */
  private static final long MAX_POLL_DELAY_MILLIS = 6_000L;

  private final SorobanServer server;
  private final int submitTimeout;

  private final KeyPair transactionSigner;
  private final Function<SCVal, T> parseResultXdrFn;

  /**
   * Sleeps for the given number of milliseconds between two {@code getTransaction} polls.
   * Package-private so tests can replace it with a recording no-op.
   */
  LongConsumer sleeper = AssembledTransaction::sleep;

  /**
   * Monotonic clock, in nanoseconds, used to enforce {@code submitTimeout}. Package-private so
   * tests can drive it deterministically together with {@link #sleeper}.
   */
  LongSupplier nanoClock = System::nanoTime;

  private final TransactionBuilder transactionBuilder;
  @Getter private Transaction builtTransaction;

  @Getter private SimulateTransactionResponse simulation;
  private SimulateTransactionResponse.SimulateHostFunctionResult simulationResult;
  private SorobanTransactionData simulationTransactionData;

  /**
   * The CAP-71 credential format the last {@link #simulate(boolean, boolean)} asked for, defaulting
   * to the {@code ADDRESS_V2} default of the shorter overloads. Transactions derived from this one,
   * such as the restore transaction built by {@link #restoreFootprint()}, inherit it.
   */
  private boolean useUpgradedAuth = true;

  @Getter private SendTransactionResponse sendTransactionResponse;
  @Getter private GetTransactionResponse getTransactionResponse;

  /**
   * Creates a new AssembledTransaction.
   *
   * @param transactionBuilder the transaction builder
   * @param server the Soroban server
   * @param transactionSigner the keypair to sign the transaction with
   * @param parseResultXdrFn the function to parse the result XDR
   * @param submitTimeout how many seconds to keep polling for the result after the transaction has
   *     been sent. It bounds the polling loop only: a single RPC request is bounded by the HTTP
   *     timeouts of the {@link SorobanServer}, not by this value
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
   * <p>Simulation records {@code ADDRESS_V2} (CAP-71) authorization credentials; use {@link
   * #simulate(boolean, boolean)} to ask for the legacy {@code ADDRESS} credentials.
   *
   * @param restore whether to automatically restore contract state if needed
   * @return this AssembledTransaction
   * @throws SimulationFailedException if the simulation failed
   * @throws RestorationFailureException if the contract state could not be restored
   */
  public AssembledTransaction<T> simulate(boolean restore) {
    return simulate(restore, true);
  }

  /**
   * Simulates the transaction on the network. Must be called before signing or submitting the
   * transaction. Will automatically restore required contract state if <code>restore</code> to
   * <code>true</code> and this is not a read call.
   *
   * @param restore whether to automatically restore contract state if needed
   * @param useUpgradedAuth whether simulation records {@code ADDRESS_V2} ("upgraded") authorization
   *     credentials (CAP-71) instead of the legacy {@code ADDRESS} credentials. It only affects the
   *     recording auth modes. Transitional: once the network returns {@code ADDRESS_V2} credentials
   *     by default (protocol 28), it becomes a no-op
   * @return this AssembledTransaction
   * @throws SimulationFailedException if the simulation failed
   * @throws RestorationFailureException if the contract state could not be restored
   */
  public AssembledTransaction<T> simulate(boolean restore, boolean useUpgradedAuth) {
    this.useUpgradedAuth = useUpgradedAuth;
    simulationResult = null;
    simulationTransactionData = null;

    TransactionBuilderAccount source =
        server.getAccount(transactionBuilder.getSourceAccount().getAccountId());
    transactionBuilder.getSourceAccount().setSequenceNumber(source.getSequenceNumber());

    Transaction builtTx = transactionBuilder.build();
    simulation = server.simulateTransaction(builtTx, null, null, useUpgradedAuth);

    if (restore && simulation.getRestorePreamble() != null && !isReadCall()) {
      try {
        restoreFootprint();
      } catch (SimulationFailedException
          | TransactionStillPendingException
          | SendTransactionFailedException
          | TransactionFailedException e) {
        throw new RestorationFailureException("Failed to restore contract data.", this);
      }
      return simulate(false, useUpgradedAuth);
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
   * @throws org.stellar.sdk.exception.NetworkException if a request to the RPC server fails while
   *     sending the transaction or polling for its result
   */
  public T signAndSubmit(@Nullable KeyPair transactionSigner, boolean force) {
    sign(transactionSigner, force);
    return submit();
  }

  /**
   * Signs the transaction.
   *
   * <p>Entries whose credential address is a contract ({@code C...}) are excluded from the
   * missing-signature check: contract-account authorization is evaluated on-chain by the account's
   * {@code __check_auth} and cannot be verified client-side.
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
   * <p>Only entries whose top-level credential address matches the signer are signed. Delegate
   * nodes of {@code SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES} entries (CAP-71-01) are not
   * traversed — which delegates must sign is the account contract's policy. Fill them with {@link
   * Auth#authorizeEntry(SorobanAuthorizationEntry, Auth.Signer, Long, Network, String)}, passing
   * the delegate's address as {@code forAddress} and the same expiration ledger for every signer of
   * one entry (the shared signature payload commits to it).
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
      SorobanAddressCredentials addressCredentials = Auth.getAddressCredentials(e.getCredentials());
      if (addressCredentials == null) {
        // source-account credentials don't need an explicit signature here, since the tx
        // envelope is already signed by the source account
        continue;
      }
      if (!Address.fromSCAddress(addressCredentials.getAddress())
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
   * <p>Only the top-level address credentials of each entry are considered. Delegate signers of
   * {@code SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES} entries (CAP-71-01) are not reported: which
   * delegates must sign is the account contract's policy, which the SDK cannot know — per CAP-71-01
   * a listed delegate is only verified when the account's {@code __check_auth} consumes it, so a
   * delegate left with a void signature may be perfectly valid.
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
        .map(entry -> Auth.getAddressCredentials(entry.getCredentials()))
        .filter(
            addressCredentials ->
                // skip source-account credentials (no address payload), which are covered by
                // the envelope signature on the source account
                addressCredentials != null
                    && (includeAlreadySigned
                        || addressCredentials.getSignature().getDiscriminant()
                            == SCValType.SCV_VOID))
        .map(
            addressCredentials -> Address.fromSCAddress(addressCredentials.getAddress()).toString())
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
   * <p>The restore transaction is simulated with the {@code useUpgradedAuth} choice of the last
   * {@link #simulate(boolean, boolean)} call on this transaction. The choice does not change the
   * restore itself — a {@link RestoreFootprintOperation} records no authorization entries — but
   * keeps the derived transaction consistent with the one it restores state for.
   *
   * @throws TransactionFailedException if the transaction failed
   * @throws TransactionStillPendingException if the transaction is still pending after the timeout
   * @throws SendTransactionFailedException if sending the transaction to the network failed
   * @throws org.stellar.sdk.exception.NetworkException if a request to the RPC server fails while
   *     sending the transaction or polling for its result
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
    restoreAssembled
        .simulate(false, useUpgradedAuth)
        .sign(this.transactionSigner, true)
        .submitInternal();
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
   * @throws org.stellar.sdk.exception.NetworkException if a request to the RPC server fails while
   *     sending the transaction or polling for its result
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
    SCVal resultVal;
    if (transactionMeta.getV3() != null) {
      resultVal = transactionMeta.getV3().getSorobanMeta().getReturnValue();
    } else {
      resultVal = transactionMeta.getV4().getSorobanMeta().getReturnValue();
    }
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

    getTransactionResponse = waitForTransaction(sendTransactionResponse.getHash());

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

  /**
   * Polls {@code getTransaction} until the transaction leaves {@code NOT_FOUND} or {@code
   * submitTimeout} seconds have elapsed, sleeping between polls with a gentle exponential backoff
   * (1s, 1.5s, 2.25s, ... growing by half each time, capped at 6s and at the time left before the
   * deadline).
   *
   * <p>The first poll is made immediately. The last sleep is cut short at the deadline and is
   * followed by one final poll, so a transaction that lands during that sleep is still picked up;
   * no new sleep is started once the deadline has passed. {@code submitTimeout} therefore bounds
   * the polling loop, not the duration of an individual request, which is bounded by the server's
   * HTTP timeouts.
   *
   * <p>Network errors raised by {@code getTransaction} are not swallowed; they propagate to the
   * caller.
   *
   * @return the last response received, which is still {@code NOT_FOUND} if the deadline passed
   */
  private GetTransactionResponse waitForTransaction(String txHash) {
    // Monotonic clock: a wall-clock adjustment must not stretch or cut the wait short.
    long deadlineNanos = nanoClock.getAsLong() + TimeUnit.SECONDS.toNanos(submitTimeout);
    long delayMillis = INITIAL_POLL_DELAY_MILLIS;

    GetTransactionResponse response = server.getTransaction(txHash);
    while (response.getStatus() == GetTransactionResponse.GetTransactionStatus.NOT_FOUND) {
      long remainingMillis = TimeUnit.NANOSECONDS.toMillis(deadlineNanos - nanoClock.getAsLong());
      if (remainingMillis <= 0) {
        break;
      }
      sleeper.accept(Math.min(delayMillis, remainingMillis));
      response = server.getTransaction(txHash);
      delayMillis = Math.min(delayMillis * 3 / 2, MAX_POLL_DELAY_MILLIS);
    }
    return response;
  }

  private static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UnexpectedException("Interrupted while waiting for the transaction", e);
    }
  }

  @Value
  private static class SimulationData {
    SimulateTransactionResponse.SimulateHostFunctionResult result;
    SorobanTransactionData transactionData;
  }
}
