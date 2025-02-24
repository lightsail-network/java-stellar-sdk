package org.stellar.sdk.contract.exception;

import lombok.Getter;
import org.stellar.sdk.contract.AssembledTransaction;
import org.stellar.sdk.exception.SdkException;

/** Raised when an assembled transaction fails. */
@Getter
public class AssembledTransactionException extends SdkException {
  /** The assembled transaction that failed. */
  private final AssembledTransaction<?> assembledTransaction;

  public AssembledTransactionException(
      String message, AssembledTransaction<?> assembledTransaction) {
    super(message);
    this.assembledTransaction = assembledTransaction;
  }
}
