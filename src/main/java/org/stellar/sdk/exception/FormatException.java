package org.stellar.sdk.exception;

import org.stellar.sdk.KeyPair;

/**
 * Indicates that there was a problem decoding strkey encoded string.
 *
 * @see KeyPair
 */
public class FormatException extends SdkException {
  public FormatException() {
    super();
  }

  public FormatException(String message) {
    super(message);
  }
}
