package org.stellar.sdk;

public class PrepareTransactionException extends RuntimeException {
  public PrepareTransactionException() {}

  public PrepareTransactionException(String message) {
    super(message);
  }

  public PrepareTransactionException(String message, Throwable cause) {
    super(message, cause);
  }

  public PrepareTransactionException(Throwable cause) {
    super(cause);
  }

  public PrepareTransactionException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
