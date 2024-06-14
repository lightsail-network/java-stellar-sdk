package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.TrustLineAsset;
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
 * <p>Revokes the sponsorship of a trustline entry.
 *
 * @see <a href="https://developers.stellar.org/docs/encyclopedia/sponsored-reserves"
 *     target="_blank">Sponsored Reserves</a>
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RevokeTrustlineSponsorshipOperation extends Operation {
  /** The account whose trustline will be revoked. */
  @NonNull private final String accountId;

  /** The asset of the trustline which will be revoked. */
  @NonNull private final TrustLineAsset asset;

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

    private String sourceAccount;

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
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public RevokeTrustlineSponsorshipOperation build() {
      RevokeTrustlineSponsorshipOperation operation =
          new RevokeTrustlineSponsorshipOperation(accountId, asset);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
