package org.stellar.sdk.exception;

import lombok.Getter;
import org.stellar.sdk.responses.sorobanrpc.SimulateTransactionResponse;

/** Exception thrown when preparing a transaction failed. */
@Getter
public class PrepareTransactionException extends SdkException {
  // The response returned by the Soroban-RPC instance when simulating the transaction.
  private final SimulateTransactionResponse simulateTransactionResponse;

  public PrepareTransactionException(
      String message, SimulateTransactionResponse simulateTransactionResponse) {
    super(message);
    this.simulateTransactionResponse = simulateTransactionResponse;
  }
}
