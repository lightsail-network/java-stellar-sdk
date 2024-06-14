package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.ClawbackOp;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a href="https://developers.stellar.org/docs/start/list-of-operations/#clawback"
 * target="_blank">Clawback</a> operation.
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ClawbackOperation extends Operation {
  /** The account owning of the trustline. */
  @Getter @NonNull private final String from;

  @NonNull private final AssetTypeCreditAlphaNum asset;

  /** The amount to be clawed back. */
  @Getter @NonNull private final String amount;

  /** The asset to be clawed back. */
  public Asset getAsset() {
    return asset;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    ClawbackOp op = new ClawbackOp();

    // trustor
    op.setFrom(accountConverter.encode(from));

    Int64 amount = new Int64();
    amount.setInt64(Operation.toXdrAmount(this.amount));
    op.setAmount(amount);
    op.setAsset(asset.toXdr());

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

    private String sourceAccount;

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
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public ClawbackOperation build() {
      ClawbackOperation operation = new ClawbackOperation(from, asset, amount);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
