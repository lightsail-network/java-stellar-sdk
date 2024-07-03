package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Price;
import org.stellar.sdk.xdr.CreatePassiveSellOfferOp;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#create-passive-sell-offer"
 * target="_blank">CreatePassiveSellOffer</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class CreatePassiveSellOfferOperation extends Operation {

  /** The asset being sold in this operation */
  @NonNull private final Asset selling;

  /** The asset being bought in this operation */
  @NonNull private final Asset buying;

  /** Amount of selling being sold. */
  @NonNull private final String amount;

  /** Price of 1 unit of selling in terms of buying. */
  @NonNull private final Price price;

  /**
   * Construct a new {@link CreatePassiveSellOfferOperation} object from a {@link
   * CreatePassiveSellOfferOp} XDR object.
   *
   * @param op {@link CreatePassiveSellOfferOp} XDR object
   * @return {@link CreatePassiveSellOfferOperation} object
   */
  public static CreatePassiveSellOfferOperation fromXdr(CreatePassiveSellOfferOp op) {
    Asset selling = Asset.fromXdr(op.getSelling());
    Asset buying = Asset.fromXdr(op.getBuying());
    String amount = Operation.fromXdrAmount(op.getAmount().getInt64());
    Price price = Price.fromXdr(op.getPrice());
    return new CreatePassiveSellOfferOperation(selling, buying, amount, price);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
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
}
