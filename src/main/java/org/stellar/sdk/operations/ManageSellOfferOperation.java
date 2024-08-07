package org.stellar.sdk.operations;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Price;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.ManageSellOfferOp;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#manage-sell-offer"
 * target="_blank">ManageSellOffer</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class ManageSellOfferOperation extends Operation {

  /** The asset being sold in this operation */
  @NonNull private final Asset selling;

  /** The asset being bought in this operation */
  @NonNull private final Asset buying;

  /** Amount of selling being sold (max of 7 decimal places). */
  @NonNull private final BigDecimal amount;

  /** Price of 1 unit of selling in terms of buying. */
  @NonNull private final Price price;

  /**
   * The ID of the offer, when it is 0, represents creating a new offer, otherwise updates an
   * existing offer.
   */
  private final long offerId;

  /**
   * Construct a new {@link ManageSellOfferOperation} object from a {@link ManageSellOfferOp} XDR
   * object.
   *
   * @param op {@link ManageSellOfferOp} XDR object
   * @return {@link ManageSellOfferOperation} object
   */
  public static ManageSellOfferOperation fromXdr(ManageSellOfferOp op) {
    Asset selling = Asset.fromXdr(op.getSelling());
    Asset buying = Asset.fromXdr(op.getBuying());
    BigDecimal amount = Operation.fromXdrAmount(op.getAmount().getInt64());
    Price price = Price.fromXdr(op.getPrice());
    long offerId = op.getOfferID().getInt64();
    return new ManageSellOfferOperation(selling, buying, amount, price, offerId);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
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
}
