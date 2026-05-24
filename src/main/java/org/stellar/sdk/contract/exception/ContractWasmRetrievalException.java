package org.stellar.sdk.contract.exception;

/** Raised when a contract Wasm cannot be retrieved due to unexpected RPC response data. */
public class ContractWasmRetrievalException extends ContractIntrospectionException {
  public ContractWasmRetrievalException(String message) {
    super(message);
  }

  public ContractWasmRetrievalException(String message, Throwable cause) {
    super(message, cause);
  }
}
