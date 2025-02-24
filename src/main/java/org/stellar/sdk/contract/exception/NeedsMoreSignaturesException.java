package org.stellar.sdk.contract.exception;

import org.stellar.sdk.contract.AssembledTransaction;

/** Raised when more signatures are needed. */
public class NeedsMoreSignaturesException extends AssembledTransactionException {
  public NeedsMoreSignaturesException(
      String message, AssembledTransaction<?> assembledTransaction) {
    super(message, assembledTransaction);
  }
}
