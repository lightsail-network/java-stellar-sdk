package org.stellar.sdk.contract.exception;

/** Raised when a Wasm module or XDR stream cannot be decoded. */
public class InvalidWasmException extends ContractIntrospectionException {
  public InvalidWasmException(String message) {
    super(message);
  }

  public InvalidWasmException(String message, Throwable cause) {
    super(message, cause);
  }
}
