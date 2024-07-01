package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
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
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class ClawbackOperation extends Operation {
  /** The account owning of the trustline. */
  @NonNull private final String from;

  @NonNull private final Asset asset;

  /** The amount to be clawed back. */
  @NonNull private final String amount;

  /**
   * Construct a new {@link ClawbackOperation} object from the {@link AccountConverter} object and
   * the {@link ClawbackOp} XDR object.
   *
   * @param op {@link ClawbackOp} XDR object
   * @return {@link ClawbackOperation} object
   */
  public static ClawbackOperation fromXdr(AccountConverter accountConverter, ClawbackOp op) {
    String from = accountConverter.decode(op.getFrom());
    String amount = Operation.fromXdrAmount(op.getAmount().getInt64());
    AssetTypeCreditAlphaNum asset = Util.assertNonNativeAsset(Asset.fromXdr(op.getAsset()));
    return new ClawbackOperation(from, asset, amount);
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

  private static final class ClawbackOperationBuilderImpl
      extends ClawbackOperationBuilder<ClawbackOperation, ClawbackOperationBuilderImpl> {
    public ClawbackOperation build() {
      ClawbackOperation op = new ClawbackOperation(this);
      if (!(op.asset instanceof AssetTypeCreditAlphaNum)) {
        throw new IllegalArgumentException("native assets are not supported");
      }
      return op;
    }
  }
}
