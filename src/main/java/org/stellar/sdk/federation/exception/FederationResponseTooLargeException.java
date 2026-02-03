package org.stellar.sdk.federation.exception;

import org.stellar.sdk.exception.NetworkException;

/**
 * Thrown when the federation server response exceeds the maximum allowed size.
 *
 * <p>This limit prevents denial-of-service attacks where a malicious server could send an infinite
 * stream of data, causing OutOfMemoryError.
 */
public class FederationResponseTooLargeException extends NetworkException {
  public FederationResponseTooLargeException(long maxSize) {
    super("Federation response exceeds maximum allowed size of " + maxSize + " bytes", null, null);
  }
}
