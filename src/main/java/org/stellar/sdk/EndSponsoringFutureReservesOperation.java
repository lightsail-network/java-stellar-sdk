package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#end-sponsoring-future-reserves"
 * target="_blank">EndSponsoringFutureReserves</a> operation.
 */
@EqualsAndHashCode(callSuper = true)
public class EndSponsoringFutureReservesOperation extends Operation {
  public EndSponsoringFutureReservesOperation() {}

  public EndSponsoringFutureReservesOperation(@NonNull String sourceAccount) {
    setSourceAccount(sourceAccount);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.END_SPONSORING_FUTURE_RESERVES);

    return body;
  }
}
