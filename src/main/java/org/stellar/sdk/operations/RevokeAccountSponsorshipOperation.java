package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.StrKey;
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
 * <p>Revokes the sponsorship of an account entry.
 *
 * @see <a href="https://developers.stellar.org/docs/encyclopedia/sponsored-reserves"
 *     target="_blank">Sponsored Reserves</a>
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RevokeAccountSponsorshipOperation extends Operation {

  /** The account whose sponsorship will be revoked. */
  @NonNull private final String accountId;

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

    private String sourceAccount;

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
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public RevokeAccountSponsorshipOperation build() {
      RevokeAccountSponsorshipOperation operation =
          new RevokeAccountSponsorshipOperation(accountId);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
