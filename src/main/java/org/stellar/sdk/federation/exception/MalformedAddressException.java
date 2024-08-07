package org.stellar.sdk.federation.exception;

/** Given Stellar address is malformed. */
public class MalformedAddressException extends IllegalArgumentException {
  public MalformedAddressException(String message) {
    super(message);
  }
}
