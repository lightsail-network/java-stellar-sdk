package org.stellar.sdk.federation.exception;

import org.stellar.sdk.exception.NetworkException;

/** Federation server was not found in stellar.toml file. */
public class NoFederationServerException extends NetworkException {

  public NoFederationServerException() {
    super("No federation server found in stellar.toml file", null, null);
  }
}
