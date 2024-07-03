package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.ChangeTrustAsset;
import org.stellar.sdk.xdr.ChangeTrustOp;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#change-trust"
 * target="_blank">ChangeTrust</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
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

  /**
   * Construct a new {@link ChangeTrustOperation} object from a {@link ChangeTrustOp} XDR object.
   *
   * @param op {@link ChangeTrustOp} XDR object
   * @return {@link ChangeTrustOperation} object
   */
  public static ChangeTrustOperation fromXdr(ChangeTrustOp op) {
    ChangeTrustAsset asset = ChangeTrustAsset.fromXdr(op.getLine());
    String limit = Operation.fromXdrAmount(op.getLimit().getInt64());
    return new ChangeTrustOperation(asset, limit);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
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
}
