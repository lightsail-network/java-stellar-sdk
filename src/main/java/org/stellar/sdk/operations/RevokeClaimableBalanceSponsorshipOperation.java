package org.stellar.sdk.operations;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.ClaimableBalanceID;
import org.stellar.sdk.xdr.LedgerEntryType;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.RevokeSponsorshipOp;
import org.stellar.sdk.xdr.RevokeSponsorshipType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#revoke-sponsorship"
 * target="_blank">Revoke sponsorship</a> operation.
 *
 * <p>Revokes the sponsorship of a claimable balance entry.
 *
 * @see <a href="https://developers.stellar.org/docs/encyclopedia/sponsored-reserves"
 *     target="_blank">Sponsored Reserves</a>
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class RevokeClaimableBalanceSponsorshipOperation extends Operation {
  /** The id of the claimable balance whose sponsorship will be revoked. */
  @NonNull private final String balanceId;

  /**
   * Construct a new {@link RevokeClaimableBalanceSponsorshipOperation} object from a {@link
   * RevokeSponsorshipOp} XDR object.
   *
   * @param op {@link RevokeSponsorshipOp} XDR object
   * @return {@link RevokeClaimableBalanceSponsorshipOperation} object
   */
  public static RevokeClaimableBalanceSponsorshipOperation fromXdr(RevokeSponsorshipOp op) {
    try {
      String balanceId =
          Util.bytesToHex(op.getLedgerKey().getClaimableBalance().getBalanceID().toXdrByteArray())
              .toLowerCase();
      return new RevokeClaimableBalanceSponsorshipOperation(balanceId);
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid claimableBalance in the operation", e);
    }
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
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
}
