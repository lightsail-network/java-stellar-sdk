package org.stellar.sdk;

import java.util.Objects;
import lombok.NonNull;
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
      sponsoredId = StrKey.encodeEd25519PublicKey(op.getSponsoredID());
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
    public BeginSponsoringFutureReservesOperation.Builder setSourceAccount(
        @NonNull String sourceAccount) {
      mSourceAccount = sourceAccount;
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
    return Objects.hash(this.sponsoredId, this.getSourceAccount());
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof BeginSponsoringFutureReservesOperation)) {
      return false;
    }

    BeginSponsoringFutureReservesOperation other = (BeginSponsoringFutureReservesOperation) object;
    return Objects.equals(this.sponsoredId, other.sponsoredId)
        && Objects.equals(this.getSourceAccount(), other.getSourceAccount());
  }
}
