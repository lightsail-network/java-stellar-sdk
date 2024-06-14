package org.stellar.sdk.operations;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.ClaimClaimableBalanceOp;
import org.stellar.sdk.xdr.ClaimableBalanceID;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#claim-claimable-balance"
 * target="_blank">ClaimClaimableBalance</a> operation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ClaimClaimableBalanceOperation extends Operation {
  /** The claimable balance id to be claimed. */
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

    private String sourceAccount;

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
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public ClaimClaimableBalanceOperation build() {
      ClaimClaimableBalanceOperation operation = new ClaimClaimableBalanceOperation(balanceId);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
