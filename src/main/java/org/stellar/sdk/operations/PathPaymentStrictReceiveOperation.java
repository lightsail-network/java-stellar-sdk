package org.stellar.sdk.operations;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.Asset;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.PathPaymentStrictReceiveOp;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#path-payment-strict-receive"
 * target="_blank">PathPaymentStrictReceive</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class PathPaymentStrictReceiveOperation extends Operation {

  /** The asset deducted from the sender's account. */
  @NonNull private final Asset sendAsset;

  /** The maximum amount of send asset to deduct (excluding fees) (max of 7 decimal places). */
  @NonNull private final BigDecimal sendMax;

  /** Account that receives the payment. */
  @NonNull private final String destination;

  /** The asset the destination account receives. */
  @NonNull private final Asset destAsset;

  /** The amount of destination asset the destination account receives (max of 7 decimal places). */
  @NonNull private final BigDecimal destAmount;

  /**
   * The assets (other than send asset and destination asset) involved in the offers the path takes.
   * For example, if you can only find a path from USD to EUR through XLM and BTC, the path would be
   * USD -&raquo; XLM -&raquo; BTC -&raquo; EUR and the path would contain XLM and BTC.
   */
  @NonNull @Builder.Default private final Asset[] path = new Asset[0];

  /**
   * Construct a new {@link PathPaymentStrictReceiveOperation} object from the {@link
   * PathPaymentStrictReceiveOp} XDR object.
   *
   * @param op {@link PathPaymentStrictReceiveOp} XDR object
   * @return {@link PathPaymentStrictReceiveOperation} object
   */
  public static PathPaymentStrictReceiveOperation fromXdr(PathPaymentStrictReceiveOp op) {
    Asset sendAsset = Asset.fromXdr(op.getSendAsset());
    BigDecimal sendMax = Operation.fromXdrAmount(op.getSendMax().getInt64());
    String destination = StrKey.encodeMuxedAccount(op.getDestination());
    Asset destAsset = Asset.fromXdr(op.getDestAsset());
    BigDecimal destAmount = Operation.fromXdrAmount(op.getDestAmount().getInt64());
    Asset[] path = new Asset[op.getPath().length];
    for (int i = 0; i < op.getPath().length; i++) {
      path[i] = Asset.fromXdr(op.getPath()[i]);
    }
    return new PathPaymentStrictReceiveOperation(
        sendAsset, sendMax, destination, destAsset, destAmount, path);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    PathPaymentStrictReceiveOp op = new PathPaymentStrictReceiveOp();

    // sendAsset
    op.setSendAsset(sendAsset.toXdr());
    // sendMax
    Int64 sendMax = new Int64();
    sendMax.setInt64(Operation.toXdrAmount(this.sendMax));
    op.setSendMax(sendMax);
    // destination
    op.setDestination(StrKey.encodeToXDRMuxedAccount(this.destination));
    // destAsset
    op.setDestAsset(destAsset.toXdr());
    // destAmount
    Int64 destAmount = new Int64();
    destAmount.setInt64(Operation.toXdrAmount(this.destAmount));
    op.setDestAmount(destAmount);
    // path
    org.stellar.sdk.xdr.Asset[] path = new org.stellar.sdk.xdr.Asset[this.path.length];
    for (int i = 0; i < this.path.length; i++) {
      path[i] = this.path[i].toXdr();
    }
    op.setPath(path);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.PATH_PAYMENT_STRICT_RECEIVE);
    body.setPathPaymentStrictReceiveOp(op);
    return body;
  }

  private static final class PathPaymentStrictReceiveOperationBuilderImpl
      extends PathPaymentStrictReceiveOperationBuilder<
          PathPaymentStrictReceiveOperation, PathPaymentStrictReceiveOperationBuilderImpl> {
    public PathPaymentStrictReceiveOperation build() {
      PathPaymentStrictReceiveOperation op = new PathPaymentStrictReceiveOperation(this);
      if (op.path.length > 5) {
        throw new IllegalArgumentException("The maximum number of assets in the path is 5");
      }
      return op;
    }
  }
}
