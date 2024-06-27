package org.stellar.sdk.federation.exception;

import org.stellar.sdk.exception.NetworkException;

/** Stellar address not found by federation server */
public class NotFoundException extends NetworkException {
  public NotFoundException() {
    super(404, null);
  }
}
