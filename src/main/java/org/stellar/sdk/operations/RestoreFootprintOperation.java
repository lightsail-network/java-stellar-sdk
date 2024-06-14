package org.stellar.sdk.operations;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.xdr.ExtensionPoint;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.RestoreFootprintOp;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#restore-footprint"
 * target="_blank">RestoreFootprint</a> operation.
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Value
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestoreFootprintOperation extends Operation {
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
    op.setExt(ExtensionPoint.builder().discriminant(0).build());

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.RESTORE_FOOTPRINT);
    body.setRestoreFootprintOp(op);
    return body;
  }
}
