package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.io.BaseEncoding;
import java.io.IOException;
import org.stellar.sdk.xdr.ClaimableBalanceID;
import org.stellar.sdk.xdr.ClawbackClaimableBalanceOp;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents a Clawback Claimable Balance operation.
 *
 * @see <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">List
 *     of Operations</a>
 */
public class ClawbackClaimableBalanceOperation extends Operation {
  private final String balanceId;

  private ClawbackClaimableBalanceOperation(String balanceId) {
    this.balanceId = checkNotNull(balanceId, "balanceId cannot be null");
  }

  /** The id of the claimable balance which will be clawed back. */
  public String getBalanceId() {
    return balanceId;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    byte[] balanceIdBytes = BaseEncoding.base16().lowerCase().decode(this.balanceId.toLowerCase());
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

    private String mSourceAccount;

    Builder(ClawbackClaimableBalanceOp op) {
      try {
        balanceId = BaseEncoding.base16().lowerCase().encode(op.getBalanceID().toXdrByteArray());
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
      mSourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public ClawbackClaimableBalanceOperation build() {
      ClawbackClaimableBalanceOperation operation =
          new ClawbackClaimableBalanceOperation(balanceId);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getSourceAccount(), this.balanceId);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ClawbackClaimableBalanceOperation)) {
      return false;
    }

    ClawbackClaimableBalanceOperation other = (ClawbackClaimableBalanceOperation) object;
    return Objects.equal(this.balanceId, other.balanceId)
        && Objects.equal(this.getSourceAccount(), other.getSourceAccount());
  }
}
