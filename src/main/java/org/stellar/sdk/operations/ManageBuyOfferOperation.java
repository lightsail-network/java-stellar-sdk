package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Price;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.ManageBuyOfferOp;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#manage-buy-offer"
 * target="_blank">ManageBuyOffer</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class ManageBuyOfferOperation extends Operation {
  /** The asset being sold in this operation */
  @NonNull private final Asset selling;

  /** The asset being bought in this operation */
  @NonNull private final Asset buying;

  /** Amount of asset to be bought. */
  @NonNull private final String amount;

  /** Price of thing being bought in terms of what you are selling. */
  @NonNull private final Price price;

  /**
   * The ID of the offer, when it is 0, represents creating a new offer, otherwise updates an
   * existing offer.
   */
  private final long offerId;

  /**
   * Construct a new {@link ManageBuyOfferOperation} object from a {@link ManageBuyOfferOp} XDR
   * object.
   *
   * @param op {@link ManageBuyOfferOp} XDR object
   * @return {@link ManageBuyOfferOperation} object
   */
  public static ManageBuyOfferOperation fromXdr(ManageBuyOfferOp op) {
    Asset selling = Asset.fromXdr(op.getSelling());
    Asset buying = Asset.fromXdr(op.getBuying());
    String amount = Operation.fromXdrAmount(op.getBuyAmount().getInt64());
    Price price = Price.fromXdr(op.getPrice());
    long offerId = op.getOfferID().getInt64();
    return new ManageBuyOfferOperation(selling, buying, amount, price, offerId);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
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
}
