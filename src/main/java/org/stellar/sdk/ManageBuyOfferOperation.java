package org.stellar.sdk;

import java.util.Objects;
import lombok.NonNull;
import org.stellar.sdk.xdr.*;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/start/list-of-operations/#manage-buy-offer"
 * target="_blank">ManageBuyOffer</a> operation.
 *
 * @see <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">List
 *     of Operations</a>
 */
public class ManageBuyOfferOperation extends Operation {

  private final Asset selling;
  private final Asset buying;
  private final String amount;
  private final Price price;
  private final long offerId;

  private ManageBuyOfferOperation(
      @NonNull Asset selling,
      @NonNull Asset buying,
      @NonNull String amount,
      @NonNull Price price,
      long offerId) {
    this.selling = selling;
    this.buying = buying;
    this.amount = amount;
    this.price = price;
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

  /** Amount of asset to be bought. */
  public String getAmount() {
    return amount;
  }

  /** Price of thing being bought in terms of what you are selling. */
  public Price getPrice() {
    return price;
  }

  /** The ID of the offer. */
  public long getOfferId() {
    return offerId;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    ManageBuyOfferOp op = new ManageBuyOfferOp();
    op.setSelling(selling.toXdr());
    op.setBuying(buying.toXdr());
    Int64 amount = new Int64();
    amount.setInt64(Operation.toXdrAmount(this.amount));
    op.setBuyAmount(amount);
    op.setPrice(price.toXdr());
    Int64 offerId = new Int64();
    offerId.setInt64(this.offerId);
    op.setOfferID(offerId);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.MANAGE_BUY_OFFER);
    body.setManageBuyOfferOp(op);

    return body;
  }

  /**
   * Builds ManageBuyOffer operation. If you want to update existing offer use {@link
   * org.stellar.sdk.ManageBuyOfferOperation.Builder#setOfferId(long)}.
   *
   * @see ManageBuyOfferOperation
   */
  public static class Builder {

    private final Asset selling;
    private final Asset buying;
    private final String amount;
    private final Price price;
    private long offerId = 0;

    private String mSourceAccount;

    /**
     * Construct a new ManageBuyOffer builder from a ManageBuyOfferOp XDR.
     *
     * @param op {@link ManageBuyOfferOp}
     */
    Builder(ManageBuyOfferOp op) {
      selling = Asset.fromXdr(op.getSelling());
      buying = Asset.fromXdr(op.getBuying());
      amount = Operation.fromXdrAmount(op.getBuyAmount().getInt64());
      price = Price.fromXdr(op.getPrice());
      offerId = op.getOfferID().getInt64();
    }

    /**
     * Creates a new ManageBuyOffer builder. If you want to update existing offer use {@link
     * org.stellar.sdk.ManageBuyOfferOperation.Builder#setOfferId(long)}.
     *
     * @param selling The asset being sold in this operation
     * @param buying The asset being bought in this operation
     * @param amount Amount of asset to be bought.
     * @param price Price of thing being bought in terms of what you are selling.
     * @throws ArithmeticException when amount has more than 7 decimal places.
     */
    public Builder(
        @NonNull Asset selling,
        @NonNull Asset buying,
        @NonNull String amount,
        @NonNull Price price) {
      this.selling = selling;
      this.buying = buying;
      this.amount = amount;
      this.price = price;
    }

    /** An alias for {@link Builder#Builder(Asset, Asset, String, Price)} */
    public Builder(
        @NonNull Asset selling,
        @NonNull Asset buying,
        @NonNull String amount,
        @NonNull String price) {
      this(selling, buying, amount, Price.fromString(price));
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
    public Builder setSourceAccount(@NonNull String sourceAccount) {
      mSourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public ManageBuyOfferOperation build() {
      ManageBuyOfferOperation operation =
          new ManageBuyOfferOperation(selling, buying, amount, price, offerId);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.getSourceAccount(), this.amount, this.buying, this.offerId, this.price, this.selling);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ManageBuyOfferOperation)) {
      return false;
    }

    ManageBuyOfferOperation other = (ManageBuyOfferOperation) object;
    return Objects.equals(this.getSourceAccount(), other.getSourceAccount())
        && Objects.equals(this.amount, other.amount)
        && Objects.equals(this.buying, other.buying)
        && Objects.equals(this.offerId, other.offerId)
        && Objects.equals(this.price, other.price)
        && Objects.equals(this.selling, other.selling);
  }
}
