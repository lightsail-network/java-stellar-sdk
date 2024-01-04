package org.stellar.sdk;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.xdr.ClaimableBalanceID;
import org.stellar.sdk.xdr.ClawbackClaimableBalanceOp;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/start/list-of-operations/#clawback-claimable-balance"
 * target="_blank">ClawbackClaimableBalance</a> operation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ClawbackClaimableBalanceOperation extends Operation {
  /** The id of the claimable balance which will be clawed back. */
  @NonNull private final String balanceId;

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    byte[] balanceIdBytes = Util.hexToBytes(this.balanceId);
    ClaimableBalanceID balanceId;
    try {
      balanceId = ClaimableBalanceID.fromXdrByteArray(balanceIdBytes);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid balanceId: " + this.balanceId, e);
    }

    ClawbackClaimableBalanceOp op = new ClawbackClaimableBalanceOp();

    op.setBalanceID(balanceId);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.CLAWBACK_CLAIMABLE_BALANCE);
    body.setClawbackClaimableBalanceOp(op);
    return body;
  }

  /**
   * Builds ClawbackClaimableBalanceOperation.
   *
   * @see ClawbackClaimableBalanceOperation
   */
  public static class Builder {

    private final String balanceId;

    private String sourceAccount;

    Builder(ClawbackClaimableBalanceOp op) {
      try {
        balanceId = Util.bytesToHex(op.getBalanceID().toXdrByteArray()).toLowerCase();
      } catch (IOException e) {
        throw new IllegalArgumentException("Invalid balanceId in the operation", e);
      }
    }

    /**
     * Creates a new ClawbackClaimableBalanceOperation builder.
     *
     * @param balanceId The id of the claimable balance which will be clawed back.
     */
    public Builder(String balanceId) {
      this.balanceId = balanceId;
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
    public ClawbackClaimableBalanceOperation build() {
      ClawbackClaimableBalanceOperation operation =
          new ClawbackClaimableBalanceOperation(balanceId);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
