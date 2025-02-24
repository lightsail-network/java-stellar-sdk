package org.stellar.sdk.contract.exception;

import org.stellar.sdk.contract.AssembledTransaction;

/** Raised when trying to get the result of a transaction that has not been simulated yet. */
public class NotYetSimulatedException extends AssembledTransactionException {
  public NotYetSimulatedException(String message, AssembledTransaction<?> assembledTransaction) {
    super(message, assembledTransaction);
  }
}
