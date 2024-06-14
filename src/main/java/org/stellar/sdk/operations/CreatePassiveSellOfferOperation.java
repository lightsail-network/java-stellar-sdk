package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Price;
import org.stellar.sdk.xdr.CreatePassiveSellOfferOp;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#create-passive-sell-offer"
 * target="_blank">CreatePassiveSellOffer</a> operation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CreatePassiveSellOfferOperation extends Operation {

  /** The asset being sold in this operation */
  @NonNull private final Asset selling;

  /** The asset being bought in this operation */
  @NonNull private final Asset buying;

  /** Amount of selling being sold. */
  @NonNull private final String amount;

  /** Price of 1 unit of selling in terms of buying. */
  @NonNull private final Price price;

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    CreatePassiveSellOfferOp op = new CreatePassiveSellOfferOp();
    op.setSelling(selling.toXdr());
    op.setBuying(buying.toXdr());
    Int64 amount = new Int64();
    amount.setInt64(Operation.toXdrAmount(this.amount));
    op.setAmount(amount);
    op.setPrice(price.toXdr());

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.CREATE_PASSIVE_SELL_OFFER);
    body.setCreatePassiveSellOfferOp(op);

    return body;
  }

  /**
   * Builds CreatePassiveSellOffer operation.
   *
   * @see CreatePassiveSellOfferOperation
   */
  public static class Builder {

    private final Asset selling;
    private final Asset buying;
    private final String amount;
    private final Price price;

    private String sourceAccount;

    /**
     * Construct a new CreatePassiveOffer builder from a CreatePassiveOfferOp XDR.
     *
     * @param op
     */
    Builder(CreatePassiveSellOfferOp op) {
      selling = Asset.fromXdr(op.getSelling());
      buying = Asset.fromXdr(op.getBuying());
      amount = Operation.fromXdrAmount(op.getAmount().getInt64());
      price = Price.fromXdr(op.getPrice());
    }

    /**
     * Creates a new CreatePassiveSellOffer builder.
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
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public CreatePassiveSellOfferOperation.Builder setSourceAccount(@NonNull String sourceAccount) {
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public CreatePassiveSellOfferOperation build() {
      CreatePassiveSellOfferOperation operation =
          new CreatePassiveSellOfferOperation(selling, buying, amount, price);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
