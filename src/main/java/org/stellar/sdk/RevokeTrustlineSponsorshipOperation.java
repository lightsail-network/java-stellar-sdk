package org.stellar.sdk;

import java.util.Objects;
import lombok.NonNull;
import org.stellar.sdk.xdr.*;

public class RevokeTrustlineSponsorshipOperation extends Operation {
  private final String accountId;
  private final TrustLineAsset asset;

  private RevokeTrustlineSponsorshipOperation(String accountId, TrustLineAsset asset) {
    this.accountId = accountId;
    this.asset = asset;
  }

  public String getAccountId() {
    return accountId;
  }

  public TrustLineAsset getAsset() {
    return asset;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    RevokeSponsorshipOp op = new RevokeSponsorshipOp();
    LedgerKey key = new LedgerKey();
    key.setDiscriminant(LedgerEntryType.TRUSTLINE);
    LedgerKey.LedgerKeyTrustLine trustLine = new LedgerKey.LedgerKeyTrustLine();
    trustLine.setAccountID(StrKey.encodeToXDRAccountId(accountId));
    trustLine.setAsset(asset.toXdr());
    key.setTrustLine(trustLine);

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
    private final TrustLineAsset asset;

    private String mSourceAccount;

    /**
     * Construct a new RevokeTrustlineSponsorshipOperation builder from a RevokeSponsorship XDR.
     *
     * @param op {@link RevokeSponsorshipOp}
     */
    Builder(RevokeSponsorshipOp op) {
      accountId = StrKey.encodeEd25519PublicKey(op.getLedgerKey().getTrustLine().getAccountID());
      asset = TrustLineAsset.fromXdr(op.getLedgerKey().getTrustLine().getAsset());
    }

    /**
     * Creates a new RevokeTrustlineSponsorshipOperation builder.
     *
     * @param accountId The id of the account whose trustline will be revoked.
     * @param asset The asset of the trustline which will be revoked.
     */
    public Builder(String accountId, TrustLineAsset asset) {
      this.accountId = accountId;
      this.asset = asset;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public RevokeTrustlineSponsorshipOperation.Builder setSourceAccount(
        @NonNull String sourceAccount) {
      mSourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public RevokeTrustlineSponsorshipOperation build() {
      RevokeTrustlineSponsorshipOperation operation =
          new RevokeTrustlineSponsorshipOperation(accountId, asset);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.accountId, this.asset, this.getSourceAccount());
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof RevokeTrustlineSponsorshipOperation)) {
      return false;
    }

    RevokeTrustlineSponsorshipOperation other = (RevokeTrustlineSponsorshipOperation) object;
    return Objects.equals(this.accountId, other.accountId)
        && Objects.equals(this.asset, other.asset)
        && Objects.equals(this.getSourceAccount(), other.getSourceAccount());
  }
}
