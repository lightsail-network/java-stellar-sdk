package org.stellar.sdk.federation.exception;

import org.stellar.sdk.exception.NetworkException;

/** Federation server is invalid (malformed URL, not HTTPS, etc.) */
public class FederationServerInvalidException extends NetworkException {
  public FederationServerInvalidException() {
    super("Federation server is invalid", null, null);
  }
}
