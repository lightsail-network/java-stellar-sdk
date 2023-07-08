package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.CreatePassiveSellOfferOp;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/start/list-of-operations/#create-passive-sell-offer"
 * target="_blank">CreatePassiveSellOffer</a> operation.
 *
 * @see <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">List
 *     of Operations</a>
 */
public class CreatePassiveSellOfferOperation extends Operation {
  private final Asset selling;
  private final Asset buying;
  private final String amount;
  private final String price;

  private CreatePassiveSellOfferOperation(
      Asset selling, Asset buying, String amount, String price) {
    this.selling = checkNotNull(selling, "selling cannot be null");
    this.buying = checkNotNull(buying, "buying cannot be null");
    this.amount = checkNotNull(amount, "amount cannot be null");
    this.price = checkNotNull(price, "price cannot be null");
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

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    CreatePassiveSellOfferOp op = new CreatePassiveSellOfferOp();
    op.setSelling(selling.toXdr());
    op.setBuying(buying.toXdr());
    Int64 amount = new Int64();
    amount.setInt64(Operation.toXdrAmount(this.amount));
    op.setAmount(amount);
    Price price = Price.fromString(this.price);
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
    private final String price;

    private String mSourceAccount;

    /**
     * Construct a new CreatePassiveOffer builder from a CreatePassiveOfferOp XDR.
     *
     * @param op
     */
    Builder(CreatePassiveSellOfferOp op) {
      selling = Asset.fromXdr(op.getSelling());
      buying = Asset.fromXdr(op.getBuying());
      amount = Operation.fromXdrAmount(op.getAmount().getInt64().longValue());
      price = Price.fromXdr(op.getPrice()).toString();
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
    public Builder(Asset selling, Asset buying, String amount, String price) {
      this.selling = checkNotNull(selling, "selling cannot be null");
      this.buying = checkNotNull(buying, "buying cannot be null");
      this.amount = checkNotNull(amount, "amount cannot be null");
      this.price = checkNotNull(price, "price cannot be null");
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public CreatePassiveSellOfferOperation.Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
      return this;
    }

    /** Builds an operation */
    public CreatePassiveSellOfferOperation build() {
      CreatePassiveSellOfferOperation operation =
          new CreatePassiveSellOfferOperation(selling, buying, amount, price);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        this.amount, this.buying, this.price, this.selling, this.getSourceAccount());
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof CreatePassiveSellOfferOperation)) {
      return false;
    }

    CreatePassiveSellOfferOperation other = (CreatePassiveSellOfferOperation) object;
    return Objects.equal(this.amount, other.amount)
        && Objects.equal(this.buying, other.buying)
        && Objects.equal(this.price, other.price)
        && Objects.equal(this.selling, other.selling)
        && Objects.equal(this.getSourceAccount(), other.getSourceAccount());
  }
}
