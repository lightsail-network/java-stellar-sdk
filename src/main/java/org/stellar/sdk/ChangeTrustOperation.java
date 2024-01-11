package org.stellar.sdk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.xdr.ChangeTrustOp;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#change-trust"
 * target="_blank">ChangeTrust</a> operation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ChangeTrustOperation extends Operation {

  /**
   * The asset of the trustline. For example, if a gateway extends a trustline of up to 200 USD to a
   * user, the line is USD.
   */
  @NonNull private final ChangeTrustAsset asset;

  /**
   * The limit of the trustline. For example, if a gateway extends a trustline of up to 200 USD to a
   * user, the limit is 200.
   */
  @NonNull private final String limit;

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    ChangeTrustOp op = new ChangeTrustOp();
    op.setLine(asset.toXdr());
    Int64 limit = new Int64();
    limit.setInt64(Operation.toXdrAmount(this.limit));
    op.setLimit(limit);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.CHANGE_TRUST);
    body.setChangeTrustOp(op);
    return body;
  }

  /**
   * Builds ChangeTrust operation.
   *
   * @see ChangeTrustOperation
   */
  public static class Builder {

    private final ChangeTrustAsset asset;
    private final String limit;

    private String sourceAccount;

    Builder(ChangeTrustOp op) {
      asset = ChangeTrustAsset.fromXdr(op.getLine());
      limit = Operation.fromXdrAmount(op.getLimit().getInt64().longValue());
    }

    /**
     * Creates a new ChangeTrust builder.
     *
     * @param asset The asset of the trustline. For example, if a gateway extends a trustline of up
     *     to 200 USD to a user, the line is USD.
     * @param limit The limit of the trustline. For example, if a gateway extends a trustline of up
     *     to 200 USD to a user, the limit is 200.
     * @throws ArithmeticException when limit has more than 7 decimal places.
     */
    public Builder(@NonNull ChangeTrustAsset asset, @NonNull String limit) {
      this.asset = asset;
      this.limit = limit;
    }

    /**
     * Set source account of this operation
     *
     * @param sourceAccount Source account
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(@NonNull String sourceAccount) {
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public ChangeTrustOperation build() {
      ChangeTrustOperation operation = new ChangeTrustOperation(asset, limit);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
