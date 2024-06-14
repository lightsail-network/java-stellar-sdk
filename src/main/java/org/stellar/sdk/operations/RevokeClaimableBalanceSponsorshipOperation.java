package org.stellar.sdk.operations;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.ClaimableBalanceID;
import org.stellar.sdk.xdr.LedgerEntryType;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.RevokeSponsorshipOp;
import org.stellar.sdk.xdr.RevokeSponsorshipType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#revoke-sponsorship"
 * target="_blank">Revoke sponsorship</a> operation.
 *
 * <p>Revokes the sponsorship of a claimable balance entry.
 *
 * @see <a href="https://developers.stellar.org/docs/encyclopedia/sponsored-reserves"
 *     target="_blank">Sponsored Reserves</a>
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RevokeClaimableBalanceSponsorshipOperation extends Operation {
  /** The id of the claimable balance whose sponsorship will be revoked. */
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

    RevokeSponsorshipOp op = new RevokeSponsorshipOp();
    LedgerKey key = new LedgerKey();
    key.setDiscriminant(LedgerEntryType.CLAIMABLE_BALANCE);
    LedgerKey.LedgerKeyClaimableBalance claimableBalance =
        new LedgerKey.LedgerKeyClaimableBalance();

    claimableBalance.setBalanceID(balanceId);
    key.setClaimableBalance(claimableBalance);
    op.setLedgerKey(key);
    op.setDiscriminant(RevokeSponsorshipType.REVOKE_SPONSORSHIP_LEDGER_ENTRY);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.REVOKE_SPONSORSHIP);
    body.setRevokeSponsorshipOp(op);

    return body;
  }

  public static class Builder {
    private final String balanceId;

    private String sourceAccount;

    /**
     * Construct a new RevokeClaimableBalanceSponsorshipOperation builder from a RevokeSponsorship
     * XDR.
     *
     * @param op {@link RevokeSponsorshipOp}
     */
    Builder(RevokeSponsorshipOp op) {
      try {
        balanceId =
            Util.bytesToHex(op.getLedgerKey().getClaimableBalance().getBalanceID().toXdrByteArray())
                .toLowerCase();
      } catch (IOException e) {
        throw new IllegalArgumentException("Invalid claimableBalance in the operation", e);
      }
    }

    /**
     * Creates a new RevokeClaimableBalanceSponsorshipOperation builder.
     *
     * @param balanceId The id of the claimable balance whose sponsorship will be revoked.
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
    public RevokeClaimableBalanceSponsorshipOperation.Builder setSourceAccount(
        @NonNull String sourceAccount) {
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public RevokeClaimableBalanceSponsorshipOperation build() {
      RevokeClaimableBalanceSponsorshipOperation operation =
          new RevokeClaimableBalanceSponsorshipOperation(balanceId);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
