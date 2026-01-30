package org.stellar.sdk.federation.exception;

import org.stellar.sdk.exception.NetworkException;

/** Stellar.toml file was not found or was malformed. */
public class StellarTomlNotFoundInvalidException extends NetworkException {
  public StellarTomlNotFoundInvalidException(Integer code) {
    super(code, null);
  }

  public StellarTomlNotFoundInvalidException(String message) {
    super(message, null, null);
  }
}
