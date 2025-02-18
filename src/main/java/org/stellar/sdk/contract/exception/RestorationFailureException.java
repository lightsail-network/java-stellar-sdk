package org.stellar.sdk.contract.exception;

import org.stellar.sdk.contract.AssembledTransaction;

/** Raised when a restoration fails. */
public class RestorationFailureException extends AssembledTransactionException {
  public RestorationFailureException(String message, AssembledTransaction<?> assembledTransaction) {
    super(message, assembledTransaction);
  }
}
