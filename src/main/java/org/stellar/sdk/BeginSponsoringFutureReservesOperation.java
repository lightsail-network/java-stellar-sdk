package org.stellar.sdk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.xdr.*;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#begin-sponsoring-future-reserves"
 * target="_blank">BeginSponsoringFutureReserves</a> operation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BeginSponsoringFutureReservesOperation extends Operation {
  /** The sponsored account id. * */
  @NonNull private final String sponsoredId;

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

    private String sourceAccount;

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
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public BeginSponsoringFutureReservesOperation build() {
      BeginSponsoringFutureReservesOperation operation =
          new BeginSponsoringFutureReservesOperation(sponsoredId);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
