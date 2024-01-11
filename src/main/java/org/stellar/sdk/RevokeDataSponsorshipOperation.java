package org.stellar.sdk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.xdr.*;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#revoke-sponsorship"
 * target="_blank">Revoke sponsorship</a> operation.
 *
 * <p>Revokes the sponsorship of a data
 *
 * @see <a href="https://developers.stellar.org/docs/encyclopedia/sponsored-reserves"
 *     target="_blank">Sponsored Reserves</a>
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RevokeDataSponsorshipOperation extends Operation {
  /** The account whose data entry will be revoked. */
  @NonNull private final String accountId;

  /** The name of the data entry which will be revoked. */
  @NonNull private final String dataName;

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

    private String sourceAccount;

    /**
     * Construct a new RevokeDataSponsorshipOperation builder from a RevokeSponsorship XDR.
     *
     * @param op {@link RevokeSponsorshipOp}
     */
    Builder(RevokeSponsorshipOp op) {
      accountId = StrKey.encodeEd25519PublicKey(op.getLedgerKey().getData().getAccountID());
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
    public RevokeDataSponsorshipOperation.Builder setSourceAccount(@NonNull String sourceAccount) {
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public RevokeDataSponsorshipOperation build() {
      RevokeDataSponsorshipOperation operation =
          new RevokeDataSponsorshipOperation(accountId, dataName);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
