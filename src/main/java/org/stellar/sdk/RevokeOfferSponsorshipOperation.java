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
 * <p>Revokes the sponsorship of an offer entry.
 *
 * @see <a href="https://developers.stellar.org/docs/encyclopedia/sponsored-reserves"
 *     target="_blank">Sponsored Reserves</a>
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RevokeOfferSponsorshipOperation extends Operation {
  /** The account whose offer will be revoked. */
  @NonNull private final String seller;

  /** The id of the offer whose sponsorship will be revoked. */
  @NonNull private final Long offerId;

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

  public static class Builder {
    private final String seller;
    private final Long offerId;

    private String sourceAccount;

    /**
     * Construct a new RevokeOfferSponsorshipOperation builder from a RevokeSponsorship XDR.
     *
     * @param op {@link RevokeSponsorshipOp}
     */
    Builder(RevokeSponsorshipOp op) {
      offerId = op.getLedgerKey().getOffer().getOfferID().getInt64();
      seller = StrKey.encodeEd25519PublicKey(op.getLedgerKey().getOffer().getSellerID());
    }

    /**
     * Creates a new RevokeOfferSponsorshipOperation builder.
     *
     * @param seller The account ID which created the offer.
     * @param offerId The id of the offer whose sponsorship will be revoked.
     */
    public Builder(String seller, Long offerId) {
      this.seller = seller;
      this.offerId = offerId;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public RevokeOfferSponsorshipOperation.Builder setSourceAccount(@NonNull String sourceAccount) {
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public RevokeOfferSponsorshipOperation build() {
      RevokeOfferSponsorshipOperation operation =
          new RevokeOfferSponsorshipOperation(seller, offerId);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
