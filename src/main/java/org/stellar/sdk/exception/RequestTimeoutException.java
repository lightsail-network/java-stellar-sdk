package org.stellar.sdk.exception;

public class RequestTimeoutException extends NetworkException {
  public RequestTimeoutException() {
    super(null, null);
  }

  public RequestTimeoutException(Throwable cause) {
    super(cause, null, null);
  }
}
