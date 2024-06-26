package org.stellar.sdk.exception;

/** Connection error when trying to connect to federation server. */
public class ConnectionErrorException extends NetworkException {
  public ConnectionErrorException(Throwable cause) {
    super(cause, null, null);
  }
}
