package org.stellar.sdk.operations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.Asset;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.PathPaymentStrictReceiveOp;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#path-payment-strict-receive"
 * target="_blank">PathPaymentStrictReceive</a> operation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class PathPaymentStrictReceiveOperation extends Operation {

  /** The asset deducted from the sender's account. */
  @NonNull private final Asset sendAsset;

  /** The maximum amount of send asset to deduct (excluding fees) */
  @NonNull private final String sendMax;

  /** Account that receives the payment. */
  @NonNull private final String destination;

  /** The asset the destination account receives. */
  @NonNull private final Asset destAsset;

  /** The amount of destination asset the destination account receives. */
  @NonNull private final String destAmount;

  /**
   * The assets (other than send asset and destination asset) involved in the offers the path takes.
   * For example, if you can only find a path from USD to EUR through XLM and BTC, the path would be
   * USD -&raquo; XLM -&raquo; BTC -&raquo; EUR and the path would contain XLM and BTC.
   */
  @NonNull private final Asset[] path;

  private PathPaymentStrictReceiveOperation(
      @NonNull Asset sendAsset,
      @NonNull String sendMax,
      @NonNull String destination,
      @NonNull Asset destAsset,
      @NonNull String destAmount,
      Asset[] path) {
    this.sendAsset = sendAsset;
    this.sendMax = sendMax;
    this.destination = destination;
    this.destAsset = destAsset;
    this.destAmount = destAmount;
    if (path == null) {
      this.path = new Asset[0];
    } else {
      if (path.length > 5) {
        throw new IllegalArgumentException("The maximum number of assets in the path is 5");
      }
      this.path = path;
    }
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    PathPaymentStrictReceiveOp op = new PathPaymentStrictReceiveOp();

    // sendAsset
    op.setSendAsset(sendAsset.toXdr());
    // sendMax
    Int64 sendMax = new Int64();
    sendMax.setInt64(Operation.toXdrAmount(this.sendMax));
    op.setSendMax(sendMax);
    // destination
    op.setDestination(accountConverter.encode(this.destination));
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

  /**
   * Builds PathPayment operation.
   *
   * @see PathPaymentStrictReceiveOperation
   */
  public static class Builder {

    private final Asset sendAsset;
    private final String sendMax;
    private final String destination;
    private final Asset destAsset;
    private final String destAmount;
    private Asset[] path;

    private String sourceAccount;

    Builder(AccountConverter accountConverter, PathPaymentStrictReceiveOp op) {
      sendAsset = Asset.fromXdr(op.getSendAsset());
      sendMax = Operation.fromXdrAmount(op.getSendMax().getInt64());
      destination = accountConverter.decode(op.getDestination());
      destAsset = Asset.fromXdr(op.getDestAsset());
      destAmount = Operation.fromXdrAmount(op.getDestAmount().getInt64());
      path = new Asset[op.getPath().length];
      for (int i = 0; i < op.getPath().length; i++) {
        path[i] = Asset.fromXdr(op.getPath()[i]);
      }
    }

    /**
     * Creates a new PathPaymentStrictReceiveOperation builder.
     *
     * @param sendAsset The asset deducted from the sender's account.
     * @param sendMax The asset deducted from the sender's account.
     * @param destination Payment destination
     * @param destAsset The asset the destination account receives.
     * @param destAmount The amount of destination asset the destination account receives.
     * @throws ArithmeticException when sendMax or destAmount has more than 7 decimal places.
     */
    public Builder(
        @NonNull Asset sendAsset,
        @NonNull String sendMax,
        @NonNull String destination,
        @NonNull Asset destAsset,
        @NonNull String destAmount) {
      this.sendAsset = sendAsset;
      this.sendMax = sendMax;
      this.destination = destination;
      this.destAsset = destAsset;
      this.destAmount = destAmount;
    }

    /**
     * Sets path for this operation
     *
     * @param path The assets (other than send asset and destination asset) involved in the offers
     *     the path takes. For example, if you can only find a path from USD to EUR through XLM and
     *     BTC, the path would be USD -&raquo; XLM -&raquo; BTC -&raquo; EUR and the path field
     *     would contain XLM and BTC.
     * @return Builder object so you can chain methods.
     */
    public PathPaymentStrictReceiveOperation.Builder setPath(@NonNull Asset[] path) {
      if (path.length > 5) {
        throw new IllegalArgumentException("The maximum number of assets in the path is 5");
      }
      this.path = path;
      return this;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public PathPaymentStrictReceiveOperation.Builder setSourceAccount(
        @NonNull String sourceAccount) {
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public PathPaymentStrictReceiveOperation build() {
      PathPaymentStrictReceiveOperation operation =
          new PathPaymentStrictReceiveOperation(
              sendAsset, sendMax, destination, destAsset, destAmount, path);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
