package org.stellar.sdk;

import java.util.Objects;
import lombok.NonNull;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.PaymentOp;

/**
 * Represents <a href="https://developers.stellar.org/docs/start/list-of-operations/#payment"
 * target="_blank">Payment</a> operation.
 *
 * @see <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">List
 *     of Operations</a>
 */
public class PaymentOperation extends Operation {

  private final String destination;
  private final Asset asset;
  private final String amount;

  private PaymentOperation(
      @NonNull String destination, @NonNull Asset asset, @NonNull String amount) {
    this.destination = destination;
    this.asset = asset;
    this.amount = amount;
  }

  /** Account that receives the payment. */
  public String getDestination() {
    return destination;
  }

  /** Asset to send to the destination account. */
  public Asset getAsset() {
    return asset;
  }

  /** Amount of the asset to send. */
  public String getAmount() {
    return amount;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    PaymentOp op = new PaymentOp();

    // destination
    op.setDestination(accountConverter.encode(this.destination));
    // asset
    op.setAsset(asset.toXdr());
    // amount
    Int64 amount = new Int64();
    amount.setInt64(Operation.toXdrAmount(this.amount));
    op.setAmount(amount);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.PAYMENT);
    body.setPaymentOp(op);
    return body;
  }

  /**
   * Builds Payment operation.
   *
   * @see PaymentOperation
   */
  public static class Builder {
    private final String destination;
    private final Asset asset;
    private final String amount;

    private String mSourceAccount;

    /**
     * Construct a new PaymentOperation builder from a PaymentOp XDR.
     *
     * @param op {@link PaymentOp}
     */
    Builder(AccountConverter accountConverter, PaymentOp op) {
      destination = accountConverter.decode(op.getDestination());
      asset = Asset.fromXdr(op.getAsset());
      amount = Operation.fromXdrAmount(op.getAmount().getInt64().longValue());
    }

    /**
     * Creates a new PaymentOperation builder.
     *
     * @param destination The destination account id
     * @param asset The asset to send.
     * @param amount The amount to send in lumens.
     * @throws ArithmeticException when amount has more than 7 decimal places.
     */
    public Builder(String destination, Asset asset, String amount) {
      this.destination = destination;
      this.asset = asset;
      this.amount = amount;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param account The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(String account) {
      mSourceAccount = account;
      return this;
    }

    /** Builds an operation */
    public PaymentOperation build() {
      PaymentOperation operation = new PaymentOperation(destination, asset, amount);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  public int hashCode() {
    return Objects.hash(this.getSourceAccount(), this.asset, this.amount, this.destination);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof PaymentOperation)) {
      return false;
    }

    PaymentOperation other = (PaymentOperation) object;
    return Objects.equals(this.getSourceAccount(), other.getSourceAccount())
        && Objects.equals(this.asset, other.asset)
        && Objects.equals(this.amount, other.amount)
        && Objects.equals(this.destination, other.destination);
  }
}
