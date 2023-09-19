package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.*;

public class RevokeDataSponsorshipOperation extends Operation {
  private final String accountId;
  private final String dataName;

  private RevokeDataSponsorshipOperation(String accountId, String dataName) {
    this.accountId = accountId;
    this.dataName = dataName;
  }

  public String getAccountId() {
    return accountId;
  }

  public String getDataName() {
    return dataName;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    RevokeSponsorshipOp op = new RevokeSponsorshipOp();
    LedgerKey key = new LedgerKey();
    key.setDiscriminant(LedgerEntryType.DATA);
    LedgerKey.LedgerKeyData data = new LedgerKey.LedgerKeyData();
    data.setAccountID(StrKey.encodeToXDRAccountId(accountId));
    String64 dn = new String64();
    dn.setString64(new XdrString(dataName));
    data.setDataName(dn);
    key.setData(data);

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
    private final String dataName;

    private String mSourceAccount;

    /**
     * Construct a new RevokeDataSponsorshipOperation builder from a RevokeSponsorship XDR.
     *
     * @param op {@link RevokeSponsorshipOp}
     */
    Builder(RevokeSponsorshipOp op) {
      accountId = StrKey.encodeStellarAccountId(op.getLedgerKey().getData().getAccountID());
      dataName = op.getLedgerKey().getData().getDataName().getString64().toString();
    }

    /**
     * Creates a new RevokeDataSponsorshipOperation builder.
     *
     * @param accountId The id of the account whose data entry will be revoked.
     * @param dataName The name of the data entry which will be revoked.
     */
    public Builder(String accountId, String dataName) {
      this.accountId = accountId;
      this.dataName = dataName;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public RevokeDataSponsorshipOperation.Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
      return this;
    }

    /** Builds an operation */
    public RevokeDataSponsorshipOperation build() {
      RevokeDataSponsorshipOperation operation =
          new RevokeDataSponsorshipOperation(accountId, dataName);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.accountId, this.dataName, this.getSourceAccount());
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof RevokeDataSponsorshipOperation)) {
      return false;
    }

    RevokeDataSponsorshipOperation other = (RevokeDataSponsorshipOperation) object;
    return Objects.equal(this.accountId, other.accountId)
        && Objects.equal(this.dataName, other.dataName)
        && Objects.equal(this.getSourceAccount(), other.getSourceAccount());
  }
}
