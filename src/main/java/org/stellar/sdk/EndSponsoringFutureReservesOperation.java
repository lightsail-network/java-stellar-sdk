package org.stellar.sdk;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.OperationType;

import static com.google.common.base.Preconditions.checkNotNull;

public class EndSponsoringFutureReservesOperation extends Operation {
  public EndSponsoringFutureReservesOperation() {

  }

  public EndSponsoringFutureReservesOperation(String sourceAccount) {
    setSourceAccount(checkNotNull(sourceAccount, "sourceAccount cannot be null"));
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    org.stellar.sdk.xdr.Operation.OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.END_SPONSORING_FUTURE_RESERVES);

    return body;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getSourceAccount());
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || !(object instanceof EndSponsoringFutureReservesOperation)) {
      return false;
    }

    EndSponsoringFutureReservesOperation other = (EndSponsoringFutureReservesOperation) object;
    return Objects.equal(this.getSourceAccount(), other.getSourceAccount());
  }
}
