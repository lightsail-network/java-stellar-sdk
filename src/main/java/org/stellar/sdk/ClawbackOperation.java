package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.*;

/**
 * Represents a Clawback operation.
 *
 * @see <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">List
 *     of Operations</a>
 */
public class ClawbackOperation extends Operation {
  private final String mFrom;
  private final AssetTypeCreditAlphaNum mAsset;
  private final String mAmount;

  private ClawbackOperation(String from, AssetTypeCreditAlphaNum asset, String amount) {
    mFrom = checkNotNull(from, "from cannot be null");
    mAsset = checkNotNull(asset, "asset cannot be null");
    mAmount = checkNotNull(amount, "amount cannot be null");
  }

  /** The account owning of the trustline. */
  public String getFrom() {
    return mFrom;
  }

  /** The amount to be clawed back. */
  public String getAmount() {
    return mAmount;
  }

  /** The asset to be clawed back. */
  public Asset getAsset() {
    return mAsset;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    ClawbackOp op = new ClawbackOp();

    // trustor
    op.setFrom(accountConverter.encode(mFrom));

    Int64 amount = new Int64();
    amount.setInt64(Operation.toXdrAmount(mAmount));
    op.setAmount(amount);
    op.setAsset(mAsset.toXdr());

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.CLAWBACK);
    body.setClawbackOp(op);
    return body;
  }

  /**
   * Builds ClawbackOperation operation.
   *
   * @see ClawbackOperation
   */
  public static class Builder {
    private final String from;
    private final AssetTypeCreditAlphaNum asset;
    private final String amount;

    private String mSourceAccount;

    Builder(AccountConverter accountConverter, ClawbackOp op) {
      from = accountConverter.decode(op.getFrom());
      amount = Operation.fromXdrAmount(op.getAmount().getInt64().longValue());
      asset = Util.assertNonNativeAsset(Asset.fromXdr(op.getAsset()));
    }

    /**
     * Creates a new ClawbackOperation builder.
     *
     * @param from The account holding the trustline.
     * @param asset The asset held in the trustline.
     * @param amount The amount to be clawed back.
     */
    public Builder(String from, Asset asset, String amount) {
      this.from = from;
      this.asset = Util.assertNonNativeAsset(asset);
      this.amount = amount;
    }

    /**
     * Set source account of this operation
     *
     * @param sourceAccount Source account
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public ClawbackOperation build() {
      ClawbackOperation operation = new ClawbackOperation(from, asset, amount);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getSourceAccount(), this.mFrom, this.mAsset, this.mAmount);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ClawbackOperation)) {
      return false;
    }

    ClawbackOperation other = (ClawbackOperation) object;
    return Objects.equal(this.mFrom, other.mFrom)
        && Objects.equal(this.mAsset, other.mAsset)
        && Objects.equal(this.mAmount, other.mAmount)
        && Objects.equal(this.getSourceAccount(), other.getSourceAccount());
  }
}
