package org.stellar.sdk.exception;

public class RequestTimeoutException extends SdkException {
  public RequestTimeoutException() {}

  public RequestTimeoutException(String message) {
    super(message);
  }

  public RequestTimeoutException(String message, Throwable cause) {
    super(message, cause);
  }

  public RequestTimeoutException(Throwable cause) {
    super(cause);
  }

  public RequestTimeoutException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
