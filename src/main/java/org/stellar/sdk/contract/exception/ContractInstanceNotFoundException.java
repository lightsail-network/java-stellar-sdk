package org.stellar.sdk.contract.exception;

import lombok.Getter;

/** Raised when a contract instance ledger entry cannot be found on the network. */
@Getter
public class ContractInstanceNotFoundException extends ContractIntrospectionException {
  private final String contractId;

  public ContractInstanceNotFoundException(String contractId) {
    super("Contract instance not found, contractId: " + contractId);
    this.contractId = contractId;
  }
}
