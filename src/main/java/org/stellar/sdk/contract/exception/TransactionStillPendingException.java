package org.stellar.sdk.contract.exception;

import org.stellar.sdk.contract.AssembledTransaction;

/** Raised when the transaction is still pending. */
public class TransactionStillPendingException extends AssembledTransactionException {
  public TransactionStillPendingException(
      String message, AssembledTransaction<?> assembledTransaction) {
    super(message, assembledTransaction);
  }
}
