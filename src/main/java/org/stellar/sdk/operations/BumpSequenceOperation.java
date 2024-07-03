package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.xdr.BumpSequenceOp;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.SequenceNumber;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#bump-sequence"
 * target="_blank">BumpSequence</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class BumpSequenceOperation extends Operation {
  /** desired value for the operation's source account sequence number. */
  private final long bumpTo;

  /**
   * Construct a new {@link BumpSequenceOperation} object from a {@link BumpSequenceOp} XDR object.
   *
   * @param op {@link BumpSequenceOp} XDR object
   * @return {@link BumpSequenceOperation} object
   */
  public static BumpSequenceOperation fromXdr(BumpSequenceOp op) {
    return new BumpSequenceOperation(op.getBumpTo().getSequenceNumber().getInt64());
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    BumpSequenceOp op = new BumpSequenceOp();
    Int64 bumpTo = new Int64();
    bumpTo.setInt64(this.bumpTo);
    SequenceNumber sequenceNumber = new SequenceNumber();
    sequenceNumber.setSequenceNumber(bumpTo);
    op.setBumpTo(sequenceNumber);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.BUMP_SEQUENCE);
    body.setBumpSequenceOp(op);
    return body;
  }
}
