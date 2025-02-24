package org.stellar.sdk.contract.exception;

import org.stellar.sdk.contract.AssembledTransaction;

/** Raised when invoking `sendTransaction` fails. */
public class SendTransactionFailedException extends AssembledTransactionException {
  public SendTransactionFailedException(
      String message, AssembledTransaction<?> assembledTransaction) {
    super(message, assembledTransaction);
  }
}
