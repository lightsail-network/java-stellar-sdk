package org.stellar.sdk.contract.exception;

/**
 * Raised when a contract code ledger entry cannot be found on the network. The entry may have been
 * archived; restoring the contract code footprint may be required.
 */
public class ContractCodeNotFoundException extends ContractIntrospectionException {
  private final byte[] wasmHash;

  public ContractCodeNotFoundException(byte[] wasmHash) {
    super(
        "Contract code not found or archived. The contract code footprint may need to be restored.");
    this.wasmHash = wasmHash == null ? null : wasmHash.clone();
  }

  /** Returns a defensive copy of the Wasm hash that was looked up. */
  public byte[] getWasmHash() {
    return wasmHash == null ? null : wasmHash.clone();
  }
}
