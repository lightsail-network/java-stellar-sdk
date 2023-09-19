package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.*;

public class BeginSponsoringFutureReservesOperation extends Operation {
  private final String sponsoredId;

  private BeginSponsoringFutureReservesOperation(String sponsoredId) {
    this.sponsoredId = sponsoredId;
  }

  public String getSponsoredId() {
    return sponsoredId;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    BeginSponsoringFutureReservesOp op = new BeginSponsoringFutureReservesOp();
    op.setSponsoredID(StrKey.encodeToXDRAccountId(sponsoredId));

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.BEGIN_SPONSORING_FUTURE_RESERVES);
    body.setBeginSponsoringFutureReservesOp(op);

    return body;
  }

  public static class Builder {
    private final String sponsoredId;

    private String mSourceAccount;

    /**
     * Construct a new BeginSponsoringFutureReserves builder from a BeginSponsoringFutureReserves
     * XDR.
     *
     * @param op {@link BeginSponsoringFutureReservesOp}
     */
    Builder(BeginSponsoringFutureReservesOp op) {
      sponsoredId = StrKey.encodeStellarAccountId(op.getSponsoredID());
    }

    /**
     * Creates a new BeginSponsoringFutureReserves builder.
     *
     * @param sponsoredId account id to sponsor
     */
    public Builder(String sponsoredId) {
      this.sponsoredId = sponsoredId;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public BeginSponsoringFutureReservesOperation.Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
      return this;
    }

    /** Builds an operation */
    public BeginSponsoringFutureReservesOperation build() {
      BeginSponsoringFutureReservesOperation operation =
          new BeginSponsoringFutureReservesOperation(sponsoredId);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.sponsoredId, this.getSourceAccount());
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof BeginSponsoringFutureReservesOperation)) {
      return false;
    }

    BeginSponsoringFutureReservesOperation other = (BeginSponsoringFutureReservesOperation) object;
    return Objects.equal(this.sponsoredId, other.sponsoredId)
        && Objects.equal(this.getSourceAccount(), other.getSourceAccount());
  }
}
