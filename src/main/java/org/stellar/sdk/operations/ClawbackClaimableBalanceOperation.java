package org.stellar.sdk.operations;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.ClaimableBalanceID;
import org.stellar.sdk.xdr.ClawbackClaimableBalanceOp;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#clawback-claimable-balance"
 * target="_blank">ClawbackClaimableBalance</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class ClawbackClaimableBalanceOperation extends Operation {
  /** The id of the claimable balance which will be clawed back. */
  @NonNull private final String balanceId;

  /**
   * Construct a new {@link ClawbackClaimableBalanceOperation} object from a {@link
   * ClawbackClaimableBalanceOp} XDR object.
   *
   * @param op {@link ClawbackClaimableBalanceOp} XDR object
   * @return {@link ClawbackClaimableBalanceOperation} object
   */
  public static ClawbackClaimableBalanceOperation fromXdr(ClawbackClaimableBalanceOp op) {
    try {
      String balanceId = Util.bytesToHex(op.getBalanceID().toXdrByteArray()).toLowerCase();
      return new ClawbackClaimableBalanceOperation(balanceId);
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid balanceId in the operation", e);
    }
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

    ClawbackClaimableBalanceOp op = new ClawbackClaimableBalanceOp();

    op.setBalanceID(balanceId);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.CLAWBACK_CLAIMABLE_BALANCE);
    body.setClawbackClaimableBalanceOp(op);
    return body;
  }
}
