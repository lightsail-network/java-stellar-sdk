package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.*;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/start/list-of-operations/#manage-sell-offer"
 * target="_blank">ManageSellOffer</a> operation.
 *
 * @see <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">List
 *     of Operations</a>
 */
public class ManageSellOfferOperation extends Operation {

  private final Asset selling;
  private final Asset buying;
  private final String amount;
  private final String price;
  private final long offerId;

  private ManageSellOfferOperation(
      Asset selling, Asset buying, String amount, String price, long offerId) {
    this.selling = checkNotNull(selling, "selling cannot be null");
    this.buying = checkNotNull(buying, "buying cannot be null");
    this.amount = checkNotNull(amount, "amount cannot be null");
    this.price = checkNotNull(price, "price cannot be null");
    // offerId can be null
    this.offerId = offerId;
  }

  /** The asset being sold in this operation */
  public Asset getSelling() {
    return selling;
  }

  /** The asset being bought in this operation */
  public Asset getBuying() {
    return buying;
  }

  /** Amount of selling being sold. */
  public String getAmount() {
    return amount;
  }

  /** Price of 1 unit of selling in terms of buying. */
  public String getPrice() {
    return price;
  }

  /** The ID of the offer. */
  public long getOfferId() {
    return offerId;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    ManageSellOfferOp op = new ManageSellOfferOp();
    op.setSelling(selling.toXdr());
    op.setBuying(buying.toXdr());
    Int64 amount = new Int64();
    amount.setInt64(Operation.toXdrAmount(this.amount));
    op.setAmount(amount);
    Price price = Price.fromString(this.price);
    op.setPrice(price.toXdr());
    Int64 offerId = new Int64();
    offerId.setInt64(Long.valueOf(this.offerId));
    op.setOfferID(offerId);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.MANAGE_SELL_OFFER);
    body.setManageSellOfferOp(op);

    return body;
  }

  /**
   * Builds ManageSellOffer operation. If you want to update existing offer use {@link
   * org.stellar.sdk.ManageSellOfferOperation.Builder#setOfferId(long)}.
   *
   * @see ManageSellOfferOperation
   */
  public static class Builder {

    private final Asset selling;
    private final Asset buying;
    private final String amount;
    private final String price;
    private long offerId = 0;

    private String mSourceAccount;

    /**
     * Construct a new ManageSellOffer builder from a ManageSellOfferOp XDR.
     *
     * @param op {@link ManageSellOfferOp}
     */
    Builder(ManageSellOfferOp op) {
      selling = Asset.fromXdr(op.getSelling());
      buying = Asset.fromXdr(op.getBuying());
      amount = Operation.fromXdrAmount(op.getAmount().getInt64().longValue());
      price = Price.fromXdr(op.getPrice()).toString();
      offerId = op.getOfferID().getInt64().longValue();
    }

    /**
     * Creates a new ManageSellOffer builder. If you want to update existing offer use {@link
     * org.stellar.sdk.ManageSellOfferOperation.Builder#setOfferId(long)}.
     *
     * @param selling The asset being sold in this operation
     * @param buying The asset being bought in this operation
     * @param amount Amount of selling being sold.
     * @param price Price of 1 unit of selling in terms of buying.
     * @throws ArithmeticException when amount has more than 7 decimal places.
     */
    public Builder(Asset selling, Asset buying, String amount, String price) {
      this.selling = checkNotNull(selling, "selling cannot be null");
      this.buying = checkNotNull(buying, "buying cannot be null");
      this.amount = checkNotNull(amount, "amount cannot be null");
      this.price = checkNotNull(price, "price cannot be null");
    }

    /**
     * Sets offer ID. <code>0</code> creates a new offer. Set to existing offer ID to change it.
     *
     * @param offerId
     */
    public Builder setOfferId(long offerId) {
      this.offerId = offerId;
      return this;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
      return this;
    }

    /** Builds an operation */
    public ManageSellOfferOperation build() {
      ManageSellOfferOperation operation =
          new ManageSellOfferOperation(selling, buying, amount, price, offerId);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        this.getSourceAccount(), this.amount, this.buying, this.offerId, this.price, this.selling);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ManageSellOfferOperation)) {
      return false;
    }

    ManageSellOfferOperation other = (ManageSellOfferOperation) object;
    return Objects.equal(this.getSourceAccount(), other.getSourceAccount())
        && Objects.equal(this.amount, other.amount)
        && Objects.equal(this.buying, other.buying)
        && Objects.equal(this.offerId, other.offerId)
        && Objects.equal(this.price, other.price)
        && Objects.equal(this.selling, other.selling);
  }
}
