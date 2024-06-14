package org.stellar.sdk.operations;

import lombok.EqualsAndHashCode;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#inflation"
 * target="_blank">Inflation</a> operation.
 */
@EqualsAndHashCode(callSuper = true)
public class InflationOperation extends Operation {
  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.INFLATION);
    return body;
  }
}
