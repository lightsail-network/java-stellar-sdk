package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.Asset;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.PathPaymentStrictSendOp;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#path-payment-strict-send"
 * target="_blank">PathPaymentStrictSend</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class PathPaymentStrictSendOperation extends Operation {
  /** The asset deducted from the sender's account. */
  @NonNull private final Asset sendAsset;

  /** The amount of send asset to deduct (excluding fees) */
  @NonNull private final String sendAmount;

  /** Account that receives the payment. */
  @NonNull private final String destination;

  /** The asset the destination account receives. */
  @NonNull private final Asset destAsset;

  /** The minimum amount of destination asset the destination account receives. */
  @NonNull private final String destMin;

  /**
   * The assets (other than send asset and destination asset) involved in the offers the path takes.
   * For example, if you can only find a path from USD to EUR through XLM and BTC, the path would be
   * USD -&raquo; XLM -&raquo; BTC -&raquo; EUR and the path would contain XLM and BTC.
   */
  @NonNull @Builder.Default private final Asset[] path = new Asset[0];

  /**
   * Construct a new {@link PathPaymentStrictSendOperation} object from the {@link AccountConverter}
   * object and the {@link PathPaymentStrictSendOp} XDR object.
   *
   * @param op {@link PathPaymentStrictSendOp} XDR object
   * @return {@link PathPaymentStrictSendOperation} object
   */
  public static PathPaymentStrictSendOperation fromXdr(
      AccountConverter accountConverter, PathPaymentStrictSendOp op) {
    Asset sendAsset = Asset.fromXdr(op.getSendAsset());
    String sendAmount = Operation.fromXdrAmount(op.getSendAmount().getInt64());
    String destination = accountConverter.decode(op.getDestination());
    Asset destAsset = Asset.fromXdr(op.getDestAsset());
    String destMin = Operation.fromXdrAmount(op.getDestMin().getInt64());
    Asset[] path = new Asset[op.getPath().length];
    for (int i = 0; i < op.getPath().length; i++) {
      path[i] = Asset.fromXdr(op.getPath()[i]);
    }
    return new PathPaymentStrictSendOperation(
        sendAsset, sendAmount, destination, destAsset, destMin, path);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    PathPaymentStrictSendOp op = new PathPaymentStrictSendOp();

    // sendAsset
    op.setSendAsset(sendAsset.toXdr());
    // sendAmount
    Int64 sendAmount = new Int64();
    sendAmount.setInt64(Operation.toXdrAmount(this.sendAmount));
    op.setSendAmount(sendAmount);
    // destination
    op.setDestination(accountConverter.encode(this.destination));
    // destAsset
    op.setDestAsset(destAsset.toXdr());
    // destMin
    Int64 destMin = new Int64();
    destMin.setInt64(Operation.toXdrAmount(this.destMin));
    op.setDestMin(destMin);
    // path
    org.stellar.sdk.xdr.Asset[] path = new org.stellar.sdk.xdr.Asset[this.path.length];
    for (int i = 0; i < this.path.length; i++) {
      path[i] = this.path[i].toXdr();
    }
    op.setPath(path);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.PATH_PAYMENT_STRICT_SEND);
    body.setPathPaymentStrictSendOp(op);
    return body;
  }

  private void checkValidate() {
    if (this.path.length > 5) {
      throw new IllegalArgumentException("The maximum number of assets in the path is 5");
    }
  }
}
