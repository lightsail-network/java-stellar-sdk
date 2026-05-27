package org.stellar.sdk.contract.exception;

import org.stellar.sdk.exception.SdkException;

/** Base class for exceptions raised by contract introspection APIs. */
public class ContractIntrospectionException extends SdkException {
  public ContractIntrospectionException(String message) {
    super(message);
  }

  public ContractIntrospectionException(String message, Throwable cause) {
    super(message, cause);
  }
}
