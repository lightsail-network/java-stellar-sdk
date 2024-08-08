package org.stellar.sdk.operations;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.ClaimClaimableBalanceOp;
import org.stellar.sdk.xdr.ClaimableBalanceID;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#claim-claimable-balance"
 * target="_blank">ClaimClaimableBalance</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class ClaimClaimableBalanceOperation extends Operation {
  /** The claimable balance id to be claimed. */
  @NonNull private final String balanceId;

  /**
   * Construct a new {@link ClaimClaimableBalanceOperation} object from a {@link
   * ClaimClaimableBalanceOp} XDR object.
   *
   * @param op {@link ClaimClaimableBalanceOp} XDR object
   * @return {@link ClaimClaimableBalanceOperation} object
   */
  public static ClaimClaimableBalanceOperation fromXdr(ClaimClaimableBalanceOp op) {
    try {
      String balanceId = Util.bytesToHex(op.getBalanceID().toXdrByteArray()).toLowerCase();
      return new ClaimClaimableBalanceOperation(balanceId);
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid balanceId in the operation", e);
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

    ClaimClaimableBalanceOp op = new ClaimClaimableBalanceOp();
    op.setBalanceID(balanceId);
    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.CLAIM_CLAIMABLE_BALANCE);
    body.setClaimClaimableBalanceOp(op);
    return body;
  }

  public abstract static class ClaimClaimableBalanceOperationBuilder<
          C extends ClaimClaimableBalanceOperation,
          B extends ClaimClaimableBalanceOperationBuilder<C, B>>
      extends OperationBuilder<C, B> {
    public B balanceId(@NonNull String balanceId) {
      this.balanceId = balanceId.toLowerCase();
      return self();
    }
  }
}
