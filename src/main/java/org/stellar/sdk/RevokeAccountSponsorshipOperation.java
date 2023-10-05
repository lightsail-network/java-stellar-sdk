package org.stellar.sdk;

import java.util.Objects;
import lombok.NonNull;
import org.stellar.sdk.xdr.*;

public class RevokeAccountSponsorshipOperation extends Operation {
  private final String accountId;

  private RevokeAccountSponsorshipOperation(String accountId) {
    this.accountId = accountId;
  }

  public String getAccountId() {
    return accountId;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    RevokeSponsorshipOp op = new RevokeSponsorshipOp();
    LedgerKey key = new LedgerKey();
    key.setDiscriminant(LedgerEntryType.ACCOUNT);
    LedgerKey.LedgerKeyAccount account = new LedgerKey.LedgerKeyAccount();
    account.setAccountID(StrKey.encodeToXDRAccountId(accountId));
    key.setAccount(account);
    op.setLedgerKey(key);
    op.setDiscriminant(RevokeSponsorshipType.REVOKE_SPONSORSHIP_LEDGER_ENTRY);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.REVOKE_SPONSORSHIP);
    body.setRevokeSponsorshipOp(op);

    return body;
  }

  public static class Builder {
    private final String accountId;

    private String mSourceAccount;

    /**
     * Construct a new RevokeAccountSponsorshipOperation builder from a RevokeSponsorship XDR.
     *
     * @param op {@link RevokeSponsorshipOp}
     */
    Builder(RevokeSponsorshipOp op) {
      accountId = StrKey.encodeEd25519PublicKey(op.getLedgerKey().getAccount().getAccountID());
    }

    /**
     * Creates a new RevokeAccountSponsorshipOperation builder.
     *
     * @param accountId The id of the account whose sponsorship will be revoked.
     */
    public Builder(String accountId) {
      this.accountId = accountId;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public RevokeAccountSponsorshipOperation.Builder setSourceAccount(
        @NonNull String sourceAccount) {
      mSourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public RevokeAccountSponsorshipOperation build() {
      RevokeAccountSponsorshipOperation operation =
          new RevokeAccountSponsorshipOperation(accountId);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.accountId, this.getSourceAccount());
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof RevokeAccountSponsorshipOperation)) {
      return false;
    }

    RevokeAccountSponsorshipOperation other = (RevokeAccountSponsorshipOperation) object;
    return Objects.equals(this.accountId, other.accountId)
        && Objects.equals(this.getSourceAccount(), other.getSourceAccount());
  }
}
