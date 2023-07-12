package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import java.util.Arrays;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.PathPaymentStrictSendOp;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/start/list-of-operations/#path-payment-strict-send"
 * target="_blank">PathPaymentStrictSend</a> operation.
 *
 * @see <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">List
 *     of Operations</a>
 */
public class PathPaymentStrictSendOperation extends Operation {

  private final Asset sendAsset;
  private final String sendAmount;
  private final String destination;
  private final Asset destAsset;
  private final String destMin;
  private final Asset[] path;

  private PathPaymentStrictSendOperation(
      Asset sendAsset,
      String sendAmount,
      String destination,
      Asset destAsset,
      String destMin,
      Asset[] path) {
    this.sendAsset = checkNotNull(sendAsset, "sendAsset cannot be null");
    this.sendAmount = checkNotNull(sendAmount, "sendAmount cannot be null");
    this.destination = checkNotNull(destination, "destination cannot be null");
    this.destAsset = checkNotNull(destAsset, "destAsset cannot be null");
    this.destMin = checkNotNull(destMin, "destMin cannot be null");
    if (path == null) {
      this.path = new Asset[0];
    } else {
      checkArgument(path.length <= 5, "The maximum number of assets in the path is 5");
      this.path = path;
    }
  }

  /** The asset deducted from the sender's account. */
  public Asset getSendAsset() {
    return sendAsset;
  }

  /** The amount of send asset to deduct (excluding fees) */
  public String getSendAmount() {
    return sendAmount;
  }

  /** Account that receives the payment. */
  public String getDestination() {
    return destination;
  }

  /** The asset the destination account receives. */
  public Asset getDestAsset() {
    return destAsset;
  }

  /** The minimum amount of destination asset the destination account receives. */
  public String getDestMin() {
    return destMin;
  }

  /**
   * The assets (other than send asset and destination asset) involved in the offers the path takes.
   * For example, if you can only find a path from USD to EUR through XLM and BTC, the path would be
   * USD -&raquo; XLM -&raquo; BTC -&raquo; EUR and the path would contain XLM and BTC.
   */
  public Asset[] getPath() {
    return path;
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

  /**
   * Builds PathPayment operation.
   *
   * @see PathPaymentStrictSendOperation
   */
  public static class Builder {
    private final Asset sendAsset;
    private final String sendAmount;
    private final String destination;
    private final Asset destAsset;
    private final String destMin;
    private Asset[] path;

    private String mSourceAccount;

    Builder(AccountConverter accountConverter, PathPaymentStrictSendOp op) {
      sendAsset = Asset.fromXdr(op.getSendAsset());
      sendAmount = Operation.fromXdrAmount(op.getSendAmount().getInt64().longValue());
      destination = accountConverter.decode(op.getDestination());
      destAsset = Asset.fromXdr(op.getDestAsset());
      destMin = Operation.fromXdrAmount(op.getDestMin().getInt64().longValue());
      path = new Asset[op.getPath().length];
      for (int i = 0; i < op.getPath().length; i++) {
        path[i] = Asset.fromXdr(op.getPath()[i]);
      }
    }

    /**
     * Creates a new PathPaymentStrictSendOperation builder.
     *
     * @param sendAsset The asset deducted from the sender's account.
     * @param sendAmount The asset deducted from the sender's account.
     * @param destination Payment destination
     * @param destAsset The asset the destination account receives.
     * @param destMin The minimum amount of destination asset the destination account receives.
     * @throws ArithmeticException when sendAmount or destMin has more than 7 decimal places.
     */
    public Builder(
        Asset sendAsset, String sendAmount, String destination, Asset destAsset, String destMin) {
      this.sendAsset = checkNotNull(sendAsset, "sendAsset cannot be null");
      this.sendAmount = checkNotNull(sendAmount, "sendAmount cannot be null");
      this.destination = checkNotNull(destination, "destination cannot be null");
      this.destAsset = checkNotNull(destAsset, "destAsset cannot be null");
      this.destMin = checkNotNull(destMin, "destMin cannot be null");
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
    public PathPaymentStrictSendOperation.Builder setPath(Asset[] path) {
      checkNotNull(path, "path cannot be null");
      checkArgument(path.length <= 5, "The maximum number of assets in the path is 5");
      this.path = path;
      return this;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public PathPaymentStrictSendOperation.Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
      return this;
    }

    /** Builds an operation */
    public PathPaymentStrictSendOperation build() {
      PathPaymentStrictSendOperation operation =
          new PathPaymentStrictSendOperation(
              sendAsset, sendAmount, destination, destAsset, destMin, path);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  public int hashCode() {
    return Objects.hashCode(
        this.getSourceAccount(),
        this.destMin,
        this.destAsset,
        this.destination,
        Arrays.hashCode(this.path),
        this.sendAsset,
        this.sendAmount);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof PathPaymentStrictSendOperation)) {
      return false;
    }

    PathPaymentStrictSendOperation other = (PathPaymentStrictSendOperation) object;
    return Objects.equal(this.getSourceAccount(), other.getSourceAccount())
        && Objects.equal(this.destMin, other.destMin)
        && Objects.equal(this.destAsset, other.destAsset)
        && Objects.equal(this.destination, other.destination)
        && Arrays.equals(this.path, other.path)
        && Objects.equal(this.sendAsset, other.sendAsset)
        && Objects.equal(this.sendAmount, other.sendAmount);
  }
}
