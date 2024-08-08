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

  /** The amount of send asset to deduct (excluding fees) (max of 7 decimal places). */
  @NonNull private final BigDecimal sendAmount;

  /** Account that receives the payment. */
  @NonNull private final String destination;

  /** The asset the destination account receives. */
  @NonNull private final Asset destAsset;

  /**
   * The minimum amount of destination asset the destination account receives. (max of 7 decimal
   * places).
   */
  @NonNull private final BigDecimal destMin;

  /**
   * The assets (other than send asset and destination asset) involved in the offers the path takes.
   * For example, if you can only find a path from USD to EUR through XLM and BTC, the path would be
   * USD -&raquo; XLM -&raquo; BTC -&raquo; EUR and the path would contain XLM and BTC.
   */
  @NonNull @Builder.Default private final Asset[] path = new Asset[0];

  /**
   * Construct a new {@link PathPaymentStrictSendOperation} object from the {@link
   * PathPaymentStrictSendOp} XDR object.
   *
   * @param op {@link PathPaymentStrictSendOp} XDR object
   * @return {@link PathPaymentStrictSendOperation} object
   */
  public static PathPaymentStrictSendOperation fromXdr(PathPaymentStrictSendOp op) {
    Asset sendAsset = Asset.fromXdr(op.getSendAsset());
    BigDecimal sendAmount = Operation.fromXdrAmount(op.getSendAmount().getInt64());
    String destination = StrKey.encodeMuxedAccount(op.getDestination());
    Asset destAsset = Asset.fromXdr(op.getDestAsset());
    BigDecimal destMin = Operation.fromXdrAmount(op.getDestMin().getInt64());
    Asset[] path = new Asset[op.getPath().length];
    for (int i = 0; i < op.getPath().length; i++) {
      path[i] = Asset.fromXdr(op.getPath()[i]);
    }
    return new PathPaymentStrictSendOperation(
        sendAsset, sendAmount, destination, destAsset, destMin, path);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    PathPaymentStrictSendOp op = new PathPaymentStrictSendOp();

    // sendAsset
    op.setSendAsset(sendAsset.toXdr());
    // sendAmount
    Int64 sendAmount = new Int64();
    sendAmount.setInt64(Operation.toXdrAmount(this.sendAmount));
    op.setSendAmount(sendAmount);
    // destination
    op.setDestination(StrKey.encodeToXDRMuxedAccount(this.destination));
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

  public abstract static class PathPaymentStrictSendOperationBuilder<
          C extends PathPaymentStrictSendOperation,
          B extends PathPaymentStrictSendOperationBuilder<C, B>>
      extends OperationBuilder<C, B> {
    public B sendAmount(@NonNull BigDecimal sendAmount) {
      this.sendAmount = Operation.formatAmountScale(sendAmount);
      return self();
    }

    public B destMin(@NonNull BigDecimal destMin) {
      this.destMin = Operation.formatAmountScale(destMin);
      return self();
    }
  }
}
