package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.TrustLineAsset;
import org.stellar.sdk.xdr.LedgerEntryType;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.RevokeSponsorshipOp;
import org.stellar.sdk.xdr.RevokeSponsorshipType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#revoke-sponsorship"
 * target="_blank">Revoke sponsorship</a> operation.
 *
 * <p>Revokes the sponsorship of a trustline entry.
 *
 * @see <a href="https://developers.stellar.org/docs/encyclopedia/sponsored-reserves"
 *     target="_blank">Sponsored Reserves</a>
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class RevokeTrustlineSponsorshipOperation extends Operation {
  /** The account whose trustline will be revoked. */
  @NonNull private final String accountId;

  /** The asset of the trustline which will be revoked. */
  @NonNull private final TrustLineAsset asset;

  /**
   * Construct a new {@link RevokeTrustlineSponsorshipOperation} object from a {@link
   * RevokeSponsorshipOp} XDR object.
   *
   * @param op {@link RevokeSponsorshipOp} XDR object
   * @return {@link RevokeTrustlineSponsorshipOperation} object
   */
  public static RevokeTrustlineSponsorshipOperation fromXdr(RevokeSponsorshipOp op) {
    String accountId =
        StrKey.encodeEd25519PublicKey(op.getLedgerKey().getTrustLine().getAccountID());
    TrustLineAsset asset = TrustLineAsset.fromXdr(op.getLedgerKey().getTrustLine().getAsset());
    return new RevokeTrustlineSponsorshipOperation(accountId, asset);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
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
}
