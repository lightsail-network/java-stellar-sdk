package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import java.util.Arrays;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.PathPaymentStrictReceiveOp;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/start/list-of-operations/#path-payment-strict-receive"
 * target="_blank">PathPaymentStrictReceive</a> operation.
 *
 * @see <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">List
 *     of Operations</a>
 */
public class PathPaymentStrictReceiveOperation extends Operation {

  private final Asset sendAsset;
  private final String sendMax;
  private final String destination;
  private final Asset destAsset;
  private final String destAmount;
  private final Asset[] path;

  private PathPaymentStrictReceiveOperation(
      Asset sendAsset,
      String sendMax,
      String destination,
      Asset destAsset,
      String destAmount,
      Asset[] path) {
    this.sendAsset = checkNotNull(sendAsset, "sendAsset cannot be null");
    this.sendMax = checkNotNull(sendMax, "sendMax cannot be null");
    this.destination = checkNotNull(destination, "destination cannot be null");
    this.destAsset = checkNotNull(destAsset, "destAsset cannot be null");
    this.destAmount = checkNotNull(destAmount, "destAmount cannot be null");
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

  /** The maximum amount of send asset to deduct (excluding fees) */
  public String getSendMax() {
    return sendMax;
  }

  /** Account that receives the payment. */
  public String getDestination() {
    return destination;
  }

  /** The asset the destination account receives. */
  public Asset getDestAsset() {
    return destAsset;
  }

  /** The amount of destination asset the destination account receives. */
  public String getDestAmount() {
    return destAmount;
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

    private String mSourceAccount;

    Builder(AccountConverter accountConverter, PathPaymentStrictReceiveOp op) {
      sendAsset = Asset.fromXdr(op.getSendAsset());
      sendMax = Operation.fromXdrAmount(op.getSendMax().getInt64().longValue());
      destination = accountConverter.decode(op.getDestination());
      destAsset = Asset.fromXdr(op.getDestAsset());
      destAmount = Operation.fromXdrAmount(op.getDestAmount().getInt64().longValue());
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
        Asset sendAsset, String sendMax, String destination, Asset destAsset, String destAmount) {
      this.sendAsset = checkNotNull(sendAsset, "sendAsset cannot be null");
      this.sendMax = checkNotNull(sendMax, "sendMax cannot be null");
      this.destination = checkNotNull(destination, "destination cannot be null");
      this.destAsset = checkNotNull(destAsset, "destAsset cannot be null");
      this.destAmount = checkNotNull(destAmount, "destAmount cannot be null");
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
    public PathPaymentStrictReceiveOperation.Builder setPath(Asset[] path) {
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
    public PathPaymentStrictReceiveOperation.Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
      return this;
    }

    /** Builds an operation */
    public PathPaymentStrictReceiveOperation build() {
      PathPaymentStrictReceiveOperation operation =
          new PathPaymentStrictReceiveOperation(
              sendAsset, sendMax, destination, destAsset, destAmount, path);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  public int hashCode() {
    return Objects.hashCode(
        this.getSourceAccount(),
        this.destAmount,
        this.destAsset,
        this.destination,
        Arrays.hashCode(this.path),
        this.sendAsset,
        this.sendMax);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof PathPaymentStrictReceiveOperation)) {
      return false;
    }

    PathPaymentStrictReceiveOperation other = (PathPaymentStrictReceiveOperation) object;
    return Objects.equal(this.getSourceAccount(), other.getSourceAccount())
        && Objects.equal(this.destAmount, other.destAmount)
        && Objects.equal(this.destAsset, other.destAsset)
        && Objects.equal(this.destination, other.destination)
        && Arrays.equals(this.path, other.path)
        && Objects.equal(this.sendAsset, other.sendAsset)
        && Objects.equal(this.sendMax, other.sendMax);
  }
}
