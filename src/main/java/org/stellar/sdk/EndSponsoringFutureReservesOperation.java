package org.stellar.sdk;

import java.util.Objects;
import lombok.NonNull;
import org.stellar.sdk.xdr.OperationType;

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

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getSourceAccount());
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof EndSponsoringFutureReservesOperation)) {
      return false;
    }

    EndSponsoringFutureReservesOperation other = (EndSponsoringFutureReservesOperation) object;
    return Objects.equals(this.getSourceAccount(), other.getSourceAccount());
  }
}
