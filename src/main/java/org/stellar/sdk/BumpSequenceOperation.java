package org.stellar.sdk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.xdr.BumpSequenceOp;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.SequenceNumber;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#bump-sequence"
 * target="_blank">BumpSequence</a> operation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BumpSequenceOperation extends Operation {
  private final long bumpTo;

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
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

  public static class Builder {

    private final long bumpTo;

    private String sourceAccount;

    /**
     * Construct a new BumpSequence builder from a BumpSequence XDR.
     *
     * @param op {@link BumpSequenceOp}
     */
    Builder(BumpSequenceOp op) {
      bumpTo = op.getBumpTo().getSequenceNumber().getInt64();
    }

    /**
     * Creates a new BumpSequence builder.
     *
     * @param bumpTo Sequence number to bump to
     */
    public Builder(long bumpTo) {
      this.bumpTo = bumpTo;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public BumpSequenceOperation.Builder setSourceAccount(@NonNull String sourceAccount) {
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public BumpSequenceOperation build() {
      BumpSequenceOperation operation = new BumpSequenceOperation(bumpTo);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
