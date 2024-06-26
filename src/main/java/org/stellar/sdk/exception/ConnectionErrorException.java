package org.stellar.sdk.exception;

/** Connection error when trying to connect to federation server. */
public class ConnectionErrorException extends SdkException {
  public ConnectionErrorException(Throwable cause) {
    super(cause);
  }
}
