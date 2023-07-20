package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.xdr.BumpFootprintExpirationOp;
import org.stellar.sdk.xdr.ExtensionPoint;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.Uint32;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#bump-footprint-expiration"
 * target="_blank">BumpFootprintExpiration</a> operation.
 *
 * <p>Bump the expiration of a footprint (read and written ledger keys).
 *
 * @see <a href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations"
 *     target="_blank">List of Operations</a>
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Value
public class BumpFootprintExpirationOperation extends Operation {

  /**
   * the number of ledgers past the LCL (last closed ledger) by which to extend the validity of the
   * ledger keys in this transaction
   */
  @NonNull Integer ledgersToExpire;

  /**
   * Constructs a new BumpFootprintExpirationOperation object from the XDR representation of the
   * {@link BumpFootprintExpirationOperation}.
   *
   * @param op the XDR representation of the {@link BumpFootprintExpirationOperation}.
   */
  public static BumpFootprintExpirationOperation fromXdr(BumpFootprintExpirationOp op) {
    return BumpFootprintExpirationOperation.builder()
        .ledgersToExpire(op.getLedgersToExpire().getUint32())
        .build();
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    BumpFootprintExpirationOp op = new BumpFootprintExpirationOp();
    op.setExt(new ExtensionPoint.Builder().discriminant(0).build());
    op.setLedgersToExpire(new Uint32(ledgersToExpire));

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.BUMP_FOOTPRINT_EXPIRATION);
    body.setBumpFootprintExpirationOp(op);
    return body;
  }
}
