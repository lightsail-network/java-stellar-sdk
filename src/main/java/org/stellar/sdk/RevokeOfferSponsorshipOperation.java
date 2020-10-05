package org.stellar.sdk;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class RevokeOfferSponsorshipOperation extends Operation {
  private final Long offerId;

  private RevokeOfferSponsorshipOperation(Long offerId) {
    this.offerId = offerId;
  }

  public Long getOfferId() {
    return offerId;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    RevokeSponsorshipOp op = new RevokeSponsorshipOp();
    LedgerKey key = new LedgerKey();
    LedgerKey.LedgerKeyOffer offer = new LedgerKey.LedgerKeyOffer();
    Int64 id = new Int64();
    id.setInt64(offerId);
    offer.setOfferID(id);
    key.setOffer(offer);

    op.setLedgerKey(key);
    op.setDiscriminant(RevokeSponsorshipType.REVOKE_SPONSORSHIP_LEDGER_ENTRY);

    org.stellar.sdk.xdr.Operation.OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.REVOKE_SPONSORSHIP);
    body.setRevokeSponsorshipOp(op);

    return body;
  }

  public static class Builder {
    private final Long offerId;

    private String mSourceAccount;

    /**
     * Construct a new RevokeOfferSponsorshipOperation builder from a RevokeSponsorship XDR.
     * @param op {@link RevokeSponsorshipOp}
     */
    Builder(RevokeSponsorshipOp op) {
      offerId = op.getLedgerKey().getOffer().getOfferID().getInt64();
    }

    /**
     * Creates a new RevokeOfferSponsorshipOperation builder.
     * @param offerId The id of the offer whose sponsorship will be revoked.
     */
    public Builder(Long offerId) {
      this.offerId = offerId;
    }

    /**
     * Sets the source account for this operation.
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public RevokeOfferSponsorshipOperation.Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
      return this;
    }

    /**
     * Builds an operation
     */
    public RevokeOfferSponsorshipOperation build() {
      RevokeOfferSponsorshipOperation operation = new RevokeOfferSponsorshipOperation(offerId);
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
    if (object == null || !(object instanceof RevokeOfferSponsorshipOperation)) {
      return false;
    }

    RevokeOfferSponsorshipOperation other = (RevokeOfferSponsorshipOperation) object;
    return Objects.equal(this.offerId, other.offerId) &&
        Objects.equal(this.getSourceAccount(), other.getSourceAccount());
  }
}
