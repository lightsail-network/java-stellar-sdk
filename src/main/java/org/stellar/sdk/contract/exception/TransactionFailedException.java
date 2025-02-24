package org.stellar.sdk.contract.exception;

import org.stellar.sdk.contract.AssembledTransaction;

/** Raised when invoking `getTransaction` fails. */
public class TransactionFailedException extends AssembledTransactionException {
  public TransactionFailedException(String message, AssembledTransaction<?> assembledTransaction) {
    super(message, assembledTransaction);
  }
}
