package org.stellar.sdk.exception;

import org.stellar.sdk.KeyPair;

/**
 * Indicates that there was a problem decoding strkey encoded string.
 *
 * @see KeyPair
 */
public class StrKeyException extends SdkException {
  public StrKeyException() {
    super();
  }

  public StrKeyException(String message) {
    super(message);
  }

  public StrKeyException(String message, Throwable cause) {
    super(message, cause);
  }
}
