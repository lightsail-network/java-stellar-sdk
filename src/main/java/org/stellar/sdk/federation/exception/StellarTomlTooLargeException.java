package org.stellar.sdk.federation.exception;

import org.stellar.sdk.exception.NetworkException;

/**
 * Thrown when the stellar.toml file exceeds the maximum allowed size.
 *
 * <p>This limit prevents denial-of-service attacks where a malicious server could send an infinite
 * stream of data, causing OutOfMemoryError.
 */
public class StellarTomlTooLargeException extends NetworkException {
  public StellarTomlTooLargeException(long maxSize) {
    super("stellar.toml exceeds maximum allowed size of " + maxSize + " bytes", null, null);
  }
}
