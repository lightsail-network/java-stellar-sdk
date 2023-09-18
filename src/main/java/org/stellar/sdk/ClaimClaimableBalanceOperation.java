package org.stellar.sdk;

import java.io.IOException;
import java.util.Objects;
import lombok.NonNull;
import org.stellar.sdk.xdr.*;

public class ClaimClaimableBalanceOperation extends Operation {
  private final String balanceId;

  private ClaimClaimableBalanceOperation(@NonNull String balanceId) {
    this.balanceId = balanceId;
  }

  public String getBalanceId() {
    return balanceId;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    byte[] balanceIdBytes = Util.hexToBytes(this.balanceId);
    ClaimableBalanceID balanceId;
    try {
      balanceId = ClaimableBalanceID.fromXdrByteArray(balanceIdBytes);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid balanceId: " + this.balanceId, e);
    }

    ClaimClaimableBalanceOp op = new ClaimClaimableBalanceOp();
    op.setBalanceID(balanceId);
    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.CLAIM_CLAIMABLE_BALANCE);
    body.setClaimClaimableBalanceOp(op);
    return body;
  }

  public static class Builder {
    private final String balanceId;

    private String mSourceAccount;

    /**
     * Construct a new ClaimClaimableBalance builder from a ClaimClaimableBalance XDR.
     *
     * @param op {@link ClaimClaimableBalanceOp}
     */
    Builder(ClaimClaimableBalanceOp op) {
      try {
        balanceId = Util.bytesToHex(op.getBalanceID().toXdrByteArray()).toLowerCase();
      } catch (IOException e) {
        throw new IllegalArgumentException("Invalid balanceId in the operation", e);
      }
    }

    /**
     * Creates a new ClaimClaimableBalance builder.
     *
     * @param balanceId The id of the claimable balance.
     */
    public Builder(String balanceId) {
      this.balanceId = balanceId;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public ClaimClaimableBalanceOperation.Builder setSourceAccount(@NonNull String sourceAccount) {
      mSourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public ClaimClaimableBalanceOperation build() {
      ClaimClaimableBalanceOperation operation = new ClaimClaimableBalanceOperation(balanceId);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.balanceId, this.getSourceAccount());
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ClaimClaimableBalanceOperation)) {
      return false;
    }

    ClaimClaimableBalanceOperation other = (ClaimClaimableBalanceOperation) object;
    return Objects.equals(this.balanceId, other.balanceId)
        && Objects.equals(this.getSourceAccount(), other.getSourceAccount());
  }
}
