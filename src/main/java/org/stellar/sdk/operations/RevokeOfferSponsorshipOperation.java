package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.xdr.Int64;
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
 * <p>Revokes the sponsorship of an offer entry.
 *
 * @see <a href="https://developers.stellar.org/docs/encyclopedia/sponsored-reserves"
 *     target="_blank">Sponsored Reserves</a>
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class RevokeOfferSponsorshipOperation extends Operation {
  /** The account whose offer will be revoked. */
  @NonNull private final String seller;

  /** The id of the offer whose sponsorship will be revoked. */
  @NonNull private final Long offerId;

  /**
   * Construct a new {@link RevokeOfferSponsorshipOperation} object from a {@link
   * RevokeSponsorshipOp} XDR object.
   *
   * @param op {@link RevokeSponsorshipOp} XDR object
   * @return {@link RevokeOfferSponsorshipOperation} object
   */
  public static RevokeOfferSponsorshipOperation fromXdr(RevokeSponsorshipOp op) {
    long offerId = op.getLedgerKey().getOffer().getOfferID().getInt64();
    String seller = StrKey.encodeEd25519PublicKey(op.getLedgerKey().getOffer().getSellerID());
    return new RevokeOfferSponsorshipOperation(seller, offerId);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    RevokeSponsorshipOp op = new RevokeSponsorshipOp();
    LedgerKey key = new LedgerKey();
    key.setDiscriminant(LedgerEntryType.OFFER);
    LedgerKey.LedgerKeyOffer offer = new LedgerKey.LedgerKeyOffer();
    Int64 id = new Int64();
    id.setInt64(offerId);
    offer.setOfferID(id);
    offer.setSellerID(StrKey.encodeToXDRAccountId(seller));
    key.setOffer(offer);

    op.setLedgerKey(key);
    op.setDiscriminant(RevokeSponsorshipType.REVOKE_SPONSORSHIP_LEDGER_ENTRY);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.REVOKE_SPONSORSHIP);
    body.setRevokeSponsorshipOp(op);

    return body;
  }
}
