package org.stellar.sdk;

import org.stellar.sdk.xdr.CreateAccountOp;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.ManageOfferOp;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.Uint64;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html#manage-offer" target="_blank">ManageOffer</a> operation.
 * @see <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html" target="_blank">List of Operations</a>
 */
public class ManageOfferOperation extends Operation {

  private final Asset selling;
  private final Asset buying;
  private final String amount;
  private final String price;
  private final long offerId;

  private ManageOfferOperation(Asset selling, Asset buying, String amount, String price, long offerId) {
    this.selling = checkNotNull(selling, "selling cannot be null");
    this.buying = checkNotNull(buying, "buying cannot be null");
    this.amount = checkNotNull(amount, "amount cannot be null");
    this.price = checkNotNull(price, "price cannot be null");
    // offerId can be null
    this.offerId = offerId;
  }

  /**
   * The asset being sold in this operation
   */
  public Asset getSelling() {
    return selling;
  }

  /**
   * The asset being bought in this operation
   */
  public Asset getBuying() {
    return buying;
  }

  /**
   * Amount of selling being sold.
   */
  public String getAmount() {
    return amount;
  }

  /**
   * Price of 1 unit of selling in terms of buying.
   */
  public String getPrice() {
    return price;
  }

  /**
   * The ID of the offer.
   */
  public long getOfferId() {
    return offerId;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    ManageOfferOp op = new ManageOfferOp();
    op.setSelling(selling.toXdr());
    op.setBuying(buying.toXdr());
    Int64 amount = new Int64();
    amount.setInt64(Operation.toXdrAmount(this.amount));
    op.setAmount(amount);
    Price price = Price.fromString(this.price);
    op.setPrice(price.toXdr());
    Uint64 offerId = new Uint64();
    offerId.setUint64(Long.valueOf(this.offerId));
    op.setOfferID(offerId);

    org.stellar.sdk.xdr.Operation.OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.MANAGE_OFFER);
    body.setManageOfferOp(op);

    return body;
  }

  /**
   * Builds ManageOffer operation. If you want to update existing offer use
   * {@link org.stellar.sdk.ManageOfferOperation.Builder#setOfferId(long)}.
   * @see ManageOfferOperation
   */
  public static class Builder {

    private final Asset selling;
    private final Asset buying;
    private final String amount;
    private final String price;
    private long offerId = 0;

    private KeyPair mSourceAccount;

    /**
     * Construct a new CreateAccount builder from a CreateAccountOp XDR.
     * @param op {@link CreateAccountOp}
     */
    Builder(ManageOfferOp op) {
      selling = Asset.fromXdr(op.getSelling());
      buying = Asset.fromXdr(op.getBuying());
      amount = Operation.fromXdrAmount(op.getAmount().getInt64().longValue());
      int n = op.getPrice().getN().getInt32().intValue();
      int d = op.getPrice().getD().getInt32().intValue();
      price = new BigDecimal(n).divide(new BigDecimal(d)).toString();
      offerId = op.getOfferID().getUint64().longValue();
    }

    /**
     * Creates a new ManageOffer builder. If you want to update existing offer use
     * {@link org.stellar.sdk.ManageOfferOperation.Builder#setOfferId(long)}.
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
     * @param offerId
     */
    public Builder setOfferId(long offerId) {
      this.offerId = offerId;
      return this;
    }

    /**
     * Sets the source account for this operation.
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(KeyPair sourceAccount) {
      mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
      return this;
    }

    /**
     * Builds an operation
     */
    public ManageOfferOperation build() {
      ManageOfferOperation operation = new ManageOfferOperation(selling, buying, amount, price, offerId);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }
}
