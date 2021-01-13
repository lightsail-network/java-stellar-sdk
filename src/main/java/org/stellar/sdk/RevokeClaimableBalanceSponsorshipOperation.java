package org.stellar.sdk;

import com.google.common.base.Objects;
import com.google.common.io.BaseEncoding;
import org.stellar.sdk.xdr.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class RevokeClaimableBalanceSponsorshipOperation extends Operation {
  private final String balanceId;

  private RevokeClaimableBalanceSponsorshipOperation(String balanceId) {
    this.balanceId = balanceId;
  }

  public String getBalanceId() {
    return balanceId;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    RevokeSponsorshipOp op = new RevokeSponsorshipOp();
    LedgerKey key = new LedgerKey();
    key.setDiscriminant(LedgerEntryType.CLAIMABLE_BALANCE);
    LedgerKey.LedgerKeyClaimableBalance claimableBalance = new LedgerKey.LedgerKeyClaimableBalance();

    byte[] balanceIdBytes = BaseEncoding.base16().lowerCase().decode(balanceId.toLowerCase());
    XdrDataInputStream balanceIdXdrDataInputStream = new XdrDataInputStream(new ByteArrayInputStream(balanceIdBytes));
    ClaimableBalanceID id;
    try {
      id = ClaimableBalanceID.decode(balanceIdXdrDataInputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid balanceId: " + balanceId, e);
    }

    claimableBalance.setBalanceID(id);
    key.setClaimableBalance(claimableBalance);
    op.setLedgerKey(key);
    op.setDiscriminant(RevokeSponsorshipType.REVOKE_SPONSORSHIP_LEDGER_ENTRY);

    org.stellar.sdk.xdr.Operation.OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.REVOKE_SPONSORSHIP);
    body.setRevokeSponsorshipOp(op);

    return body;
  }

  public static class Builder {
    private final String balanceId;

    private String mSourceAccount;

    /**
     * Construct a new RevokeClaimableBalanceSponsorshipOperation builder from a RevokeSponsorship XDR.
     * @param op {@link RevokeSponsorshipOp}
     */
    Builder(RevokeSponsorshipOp op) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
      try {
        op.getLedgerKey().getClaimableBalance().getBalanceID().encode(xdrDataOutputStream);
      } catch (IOException e) {
        throw new IllegalArgumentException("invalid revokeSponsorshipOp.", e);
      }
      balanceId = BaseEncoding.base16().lowerCase().encode(byteArrayOutputStream.toByteArray());
    }

    /**
     * Creates a new RevokeClaimableBalanceSponsorshipOperation builder.
     * @param balanceId The id of the claimable balance whose sponsorship will be revoked.
     */
    public Builder(String balanceId) {
      this.balanceId = balanceId;
    }

    /**
     * Sets the source account for this operation.
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public RevokeClaimableBalanceSponsorshipOperation.Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
      return this;
    }

    /**
     * Builds an operation
     */
    public RevokeClaimableBalanceSponsorshipOperation build() {
      RevokeClaimableBalanceSponsorshipOperation operation = new RevokeClaimableBalanceSponsorshipOperation(balanceId);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.balanceId, this.getSourceAccount());
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || !(object instanceof RevokeClaimableBalanceSponsorshipOperation)) {
      return false;
    }

    RevokeClaimableBalanceSponsorshipOperation other = (RevokeClaimableBalanceSponsorshipOperation) object;
    return Objects.equal(this.balanceId, other.balanceId) &&
        Objects.equal(this.getSourceAccount(), other.getSourceAccount());
  }
}
