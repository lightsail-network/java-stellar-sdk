package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.Asset;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.PaymentOp;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#payment"
 * target="_blank">Payment</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class PaymentOperation extends Operation {
  /** Account that receives the payment. */
  @NonNull private final String destination;

  /** Asset to send to the destination account. */
  @NonNull private final Asset asset;

  /** Amount of the asset to send. */
  @NonNull private final String amount;

  /**
   * Construct a new {@link PaymentOperation} object from the {@link PaymentOp} XDR object.
   *
   * @param op {@link PaymentOp} XDR object
   * @return {@link PaymentOperation} object
   */
  public static PaymentOperation fromXdr(PaymentOp op) {
    String destination = StrKey.encodeMuxedAccount(op.getDestination());
    Asset asset = Asset.fromXdr(op.getAsset());
    String amount = Operation.fromXdrAmount(op.getAmount().getInt64());
    return new PaymentOperation(destination, asset, amount);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    PaymentOp op = new PaymentOp();

    // destination
    op.setDestination(StrKey.encodeToXDRMuxedAccount(this.destination));
    // asset
    op.setAsset(asset.toXdr());
    // amount
    Int64 amount = new Int64();
    amount.setInt64(Operation.toXdrAmount(this.amount));
    op.setAmount(amount);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.PAYMENT);
    body.setPaymentOp(op);
    return body;
  }
}
