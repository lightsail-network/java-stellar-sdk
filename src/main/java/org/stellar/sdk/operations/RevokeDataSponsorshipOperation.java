package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.xdr.LedgerEntryType;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.RevokeSponsorshipOp;
import org.stellar.sdk.xdr.RevokeSponsorshipType;
import org.stellar.sdk.xdr.String64;
import org.stellar.sdk.xdr.XdrString;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#revoke-sponsorship"
 * target="_blank">Revoke sponsorship</a> operation.
 *
 * <p>Revokes the sponsorship of a data
 *
 * @see <a href="https://developers.stellar.org/docs/encyclopedia/sponsored-reserves"
 *     target="_blank">Sponsored Reserves</a>
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class RevokeDataSponsorshipOperation extends Operation {
  /** The account whose data entry will be revoked. */
  @NonNull private final String accountId;

  /** The name of the data entry which will be revoked. */
  @NonNull private final String dataName;

  /**
   * Construct a new {@link RevokeDataSponsorshipOperation} object from a {@link
   * RevokeSponsorshipOp} XDR object.
   *
   * @param op {@link RevokeSponsorshipOp} XDR object
   * @return {@link RevokeDataSponsorshipOperation} object
   */
  public static RevokeDataSponsorshipOperation fromXdr(RevokeSponsorshipOp op) {
    String accountId =
        StrKey.encodeEd25519PublicKey(
            op.getLedgerKey().getData().getAccountID().getAccountID().getEd25519().getUint256());
    String dataName = op.getLedgerKey().getData().getDataName().getString64().toString();
    return new RevokeDataSponsorshipOperation(accountId, dataName);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    RevokeSponsorshipOp op = new RevokeSponsorshipOp();
    LedgerKey key = new LedgerKey();
    key.setDiscriminant(LedgerEntryType.DATA);
    LedgerKey.LedgerKeyData data = new LedgerKey.LedgerKeyData();
    data.setAccountID(KeyPair.fromAccountId(accountId).getXdrAccountId());
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
}
