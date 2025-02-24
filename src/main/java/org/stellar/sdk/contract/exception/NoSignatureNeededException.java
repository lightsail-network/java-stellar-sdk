package org.stellar.sdk.contract.exception;

import org.stellar.sdk.contract.AssembledTransaction;

/** Raised when no signature is needed. */
public class NoSignatureNeededException extends AssembledTransactionException {
  public NoSignatureNeededException(String message, AssembledTransaction<?> assembledTransaction) {
    super(message, assembledTransaction);
  }
}
