package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.*;

public class RevokeOfferSponsorshipOperation extends Operation {
  private final String seller;
  private final Long offerId;

  private RevokeOfferSponsorshipOperation(String seller, Long offerId) {
    this.seller = seller;
    this.offerId = offerId;
  }

  public String getSeller() {
    return seller;
  }

  public Long getOfferId() {
    return offerId;
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

  public static class Builder {
    private final String seller;
    private final Long offerId;

    private String mSourceAccount;

    /**
     * Construct a new RevokeOfferSponsorshipOperation builder from a RevokeSponsorship XDR.
     *
     * @param op {@link RevokeSponsorshipOp}
     */
    Builder(RevokeSponsorshipOp op) {
      offerId = op.getLedgerKey().getOffer().getOfferID().getInt64();
      seller = StrKey.encodeStellarAccountId(op.getLedgerKey().getOffer().getSellerID());
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
    public RevokeOfferSponsorshipOperation.Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
      return this;
    }

    /** Builds an operation */
    public RevokeOfferSponsorshipOperation build() {
      RevokeOfferSponsorshipOperation operation =
          new RevokeOfferSponsorshipOperation(seller, offerId);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.offerId, this.getSourceAccount());
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof RevokeOfferSponsorshipOperation)) {
      return false;
    }

    RevokeOfferSponsorshipOperation other = (RevokeOfferSponsorshipOperation) object;
    return Objects.equal(this.seller, other.seller)
        && Objects.equal(this.offerId, other.offerId)
        && Objects.equal(this.getSourceAccount(), other.getSourceAccount());
  }
}
