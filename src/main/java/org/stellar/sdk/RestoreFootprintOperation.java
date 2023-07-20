package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.xdr.ExtensionPoint;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.RestoreFootprintOp;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#restore-footprint"
 * target="_blank">RestoreFootprint</a> operation.
 *
 * @see <a href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations"
 *     target="_blank">List of Operations</a>
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Value
public class RestoreFootprintOperation extends Operation {

  private RestoreFootprintOperation() {}

  /**
   * Constructs a new RestoreFootprintOperation object from the XDR representation of the {@link
   * RestoreFootprintOperation}.
   *
   * @param op the XDR representation of the {@link RestoreFootprintOperation}.
   */
  public static RestoreFootprintOperation fromXdr(RestoreFootprintOp op) {
    return new RestoreFootprintOperation();
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    RestoreFootprintOp op = new RestoreFootprintOp();
    op.setExt(new ExtensionPoint.Builder().discriminant(0).build());

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.RESTORE_FOOTPRINT);
    body.setRestoreFootprintOp(op);
    return body;
  }
}
