package org.stellar.sdk.contract.exception;

import org.stellar.sdk.contract.AssembledTransaction;

/** Raised when a simulation fails. */
public class SimulationFailedException extends AssembledTransactionException {
  public SimulationFailedException(String message, AssembledTransaction<?> assembledTransaction) {
    super(message, assembledTransaction);
  }
}
