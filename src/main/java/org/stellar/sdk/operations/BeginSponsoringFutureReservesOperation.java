package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.xdr.BeginSponsoringFutureReservesOp;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#begin-sponsoring-future-reserves"
 * target="_blank">BeginSponsoringFutureReserves</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class BeginSponsoringFutureReservesOperation extends Operation {
  /** The sponsored account id. * */
  @NonNull private final String sponsoredId;

  /**
   * Construct a new {@link BeginSponsoringFutureReservesOperation} object from a {@link
   * BeginSponsoringFutureReservesOp} XDR object.
   *
   * @param op {@link BeginSponsoringFutureReservesOp} XDR object
   * @return {@link BeginSponsoringFutureReservesOperation} object
   */
  public static BeginSponsoringFutureReservesOperation fromXdr(BeginSponsoringFutureReservesOp op) {
    return new BeginSponsoringFutureReservesOperation(
        StrKey.encodeEd25519PublicKey(
            op.getSponsoredID().getAccountID().getEd25519().getUint256()));
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    BeginSponsoringFutureReservesOp op = new BeginSponsoringFutureReservesOp();
    op.setSponsoredID(KeyPair.fromAccountId(this.sponsoredId).getXdrAccountId());

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.BEGIN_SPONSORING_FUTURE_RESERVES);
    body.setBeginSponsoringFutureReservesOp(op);

    return body;
  }
}
