package org.stellar.sdk.contract.exception;

import lombok.Getter;

/**
 * Raised when the requested contract is a Stellar Asset Contract, which does not have an associated
 * Wasm module.
 */
@Getter
public class StellarAssetContractHasNoWasmException extends ContractIntrospectionException {
  private final String contractId;

  public StellarAssetContractHasNoWasmException(String contractId) {
    super("Contract is a Stellar Asset Contract and has no Wasm module, contractId: " + contractId);
    this.contractId = contractId;
  }
}
