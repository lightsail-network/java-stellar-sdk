package org.stellar.sdk.contract;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.*;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.xdr.SCVal;

/**
 * A client to interact with Soroban smart contracts.
 *
 * <p>This client is a wrapper for {@link SorobanServer} and {@link TransactionBuilder} to make it
 * easier to interact with Soroban smart contracts. If you need more fine-grained control, please
 * consider using them directly.
 */
public class ContractClient implements Closeable {
  private final String contractId;
  private final Network network;
  private final SorobanServer server;

  /**
   * Creates a new {@link ContractClient} with the given contract ID, RPC URL, and network.
   *
   * @param contractId The contract ID to interact with.
   * @param rpcUrl The RPC URL of the Soroban server.
   * @param network The network to interact with.
   */
  public ContractClient(String contractId, String rpcUrl, Network network) {
    this.contractId = contractId;
    this.network = network;
    this.server = new SorobanServer(rpcUrl);
  }

  /**
   * Build an {@link AssembledTransaction} to invoke a function on the contract.
   *
   * <p>An alias for {@link #invoke(String, Collection, String, KeyPair, Function, int, int, int,
   * boolean, boolean)}.
   *
   * @param functionName The name of the function to invoke.
   * @param parameters The parameters to pass to the function.
   * @param source The source account to use for the transaction.
   * @param signer The key pair to sign the transaction with.
   * @param parseResultXdrFn A function to parse the result XDR of the transaction.
   * @param baseFee The base fee for the transaction.
   */
  public <T> AssembledTransaction<T> invoke(
      String functionName,
      List<SCVal> parameters,
      String source,
      KeyPair signer,
      Function<SCVal, T> parseResultXdrFn,
      int baseFee) {
    return invoke(
        functionName, parameters, source, signer, parseResultXdrFn, baseFee, 300, 30, true, true);
  }

  /**
   * Build an {@link AssembledTransaction} to invoke a function on the contract.
   *
   * @param functionName The name of the function to invoke.
   * @param parameters The parameters to pass to the function.
   * @param source The source account to use for the transaction.
   * @param signer The key pair to sign the transaction with.
   * @param parseResultXdrFn A function to parse the result XDR of the transaction.
   * @param baseFee The base fee for the transaction.
   * @param transactionTimeout The timeout for the transaction.
   * @param submitTimeout The timeout for submitting the transaction.
   * @param simulate Whether to simulate the transaction.
   * @param restore Whether to restore the transaction, only valid when <code>simulate</code> is
   *     <code>true</code>, and the signer is provided.
   */
  public <T> AssembledTransaction<T> invoke(
      String functionName,
      Collection<SCVal> parameters,
      String source,
      @Nullable KeyPair signer,
      @Nullable Function<SCVal, T> parseResultXdrFn,
      int baseFee,
      int transactionTimeout,
      int submitTimeout,
      boolean simulate,
      boolean restore) {
    TransactionBuilder builder =
        new TransactionBuilder(new Account(source, 0L), network)
            .addOperation(
                InvokeHostFunctionOperation.invokeContractFunctionOperationBuilder(
                        contractId, functionName, parameters)
                    .build())
            .setTimeout(transactionTimeout)
            .setBaseFee(baseFee);
    AssembledTransaction<T> assembled =
        new AssembledTransaction<>(builder, server, signer, parseResultXdrFn, submitTimeout);
    if (simulate) {
      assembled = assembled.simulate(restore);
    }
    return assembled;
  }

  @Override
  public void close() throws IOException {
    server.close();
  }
}
