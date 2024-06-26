package org.stellar.sdk.exception;

public class RequestTimeoutException extends NetworkException {
  public RequestTimeoutException() {
    super("Request timed out", null, null);
  }

  public RequestTimeoutException(Throwable cause) {
    super("Request timed out", cause, null, null);
  }
}
