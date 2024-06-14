package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Price;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.ManageSellOfferOp;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#manage-sell-offer"
 * target="_blank">ManageSellOffer</a> operation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ManageSellOfferOperation extends Operation {

  /** The asset being sold in this operation */
  @NonNull private final Asset selling;

  /** The asset being bought in this operation */
  @NonNull private final Asset buying;

  /** Amount of selling being sold. */
  @NonNull private final String amount;

  /** Price of 1 unit of selling in terms of buying. */
  @NonNull private final Price price;

  /**
   * The ID of the offer, when it is 0, represents creating a new offer, otherwise updates an
   * existing offer.
   */
  private final long offerId;

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    ManageSellOfferOp op = new ManageSellOfferOp();
    op.setSelling(selling.toXdr());
    op.setBuying(buying.toXdr());
    Int64 amount = new Int64();
    amount.setInt64(Operation.toXdrAmount(this.amount));
    op.setAmount(amount);
    op.setPrice(price.toXdr());
    Int64 offerId = new Int64();
    offerId.setInt64(this.offerId);
    op.setOfferID(offerId);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.MANAGE_SELL_OFFER);
    body.setManageSellOfferOp(op);

    return body;
  }

  /**
   * Builds ManageSellOffer operation. If you want to update existing offer use {@link
   * ManageSellOfferOperation.Builder#setOfferId(long)}.
   *
   * @see ManageSellOfferOperation
   */
  public static class Builder {

    private final Asset selling;
    private final Asset buying;
    private final String amount;
    private final Price price;
    private long offerId = 0;

    private String sourceAccount;

    /**
     * Construct a new ManageSellOffer builder from a ManageSellOfferOp XDR.
     *
     * @param op {@link ManageSellOfferOp}
     */
    Builder(ManageSellOfferOp op) {
      selling = Asset.fromXdr(op.getSelling());
      buying = Asset.fromXdr(op.getBuying());
      amount = Operation.fromXdrAmount(op.getAmount().getInt64());
      price = Price.fromXdr(op.getPrice());
      offerId = op.getOfferID().getInt64();
    }

    /**
     * Creates a new ManageSellOffer builder. If you want to update existing offer use {@link
     * org.stellar.sdk.operations.ManageSellOfferOperation.Builder#setOfferId(long)}.
     *
     * @param selling The asset being sold in this operation
     * @param buying The asset being bought in this operation
     * @param amount Amount of selling being sold.
     * @param price Price of 1 unit of selling in terms of buying.
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
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public ManageSellOfferOperation build() {
      ManageSellOfferOperation operation =
          new ManageSellOfferOperation(selling, buying, amount, price, offerId);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
