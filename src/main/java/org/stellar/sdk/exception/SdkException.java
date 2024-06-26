package org.stellar.sdk.exception;

/** Base class for all exceptions thrown by the SDK. */
public class SdkException extends RuntimeException {
  public SdkException() {}

  public SdkException(String message) {
    super(message);
  }

  public SdkException(String message, Throwable cause) {
    super(message, cause);
  }

  public SdkException(Throwable cause) {
    super(cause);
  }

  public SdkException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
