package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.LiquidityPoolDepositOp;
import org.stellar.sdk.xdr.LiquidityPoolType;
import org.stellar.sdk.xdr.Operation.OperationBody;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/start/list-of-operations/#liquidity-pool-deposit"
 * target="_blank">LiquidityPoolDeposit</a> operation.
 *
 * @see <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">List
 *     of Operations</a>
 */
public class LiquidityPoolDepositOperation extends Operation {
  private final LiquidityPoolID liquidityPoolID;
  private final String maxAmountA;
  private final String maxAmountB;
  private final Price minPrice;
  private final Price maxPrice;

  public LiquidityPoolDepositOperation(
      LiquidityPoolID liquidityPoolID,
      String maxAmountA,
      String maxAmountB,
      Price minPrice,
      Price maxPrice) {
    this.liquidityPoolID = checkNotNull(liquidityPoolID, "liquidityPoolID cannot be null");
    this.maxAmountA = checkNotNull(maxAmountA, "amountA cannot be null");
    this.maxAmountB = checkNotNull(maxAmountB, "amountB cannot be null");
    this.minPrice = checkNotNull(minPrice, "minPrice cannot be null");
    this.maxPrice = checkNotNull(maxPrice, "maxPrice cannot be null");
  }

  public LiquidityPoolDepositOperation(LiquidityPoolDepositOp op) {
    this.liquidityPoolID = LiquidityPoolID.fromXdr(op.getLiquidityPoolID());
    this.maxAmountA = Operation.fromXdrAmount(op.getMaxAmountA().getInt64().longValue());
    this.maxAmountB = Operation.fromXdrAmount(op.getMaxAmountB().getInt64().longValue());
    this.minPrice = Price.fromXdr(op.getMinPrice());
    this.maxPrice = Price.fromXdr(op.getMaxPrice());
  }

  public LiquidityPoolDepositOperation(
      AssetAmount a, AssetAmount b, Price minPrice, Price maxPrice) {
    if (a.getAsset().compareTo(b.getAsset()) >= 0) {
      throw new RuntimeException("AssetA must be < AssetB");
    }
    this.liquidityPoolID =
        new LiquidityPoolID(
            LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT,
            a.getAsset(),
            b.getAsset(),
            LiquidityPoolParameters.Fee);
    this.maxAmountA = a.getAmount();
    this.maxAmountB = b.getAmount();
    this.minPrice = checkNotNull(minPrice, "minPrice cannot be null");
    this.maxPrice = checkNotNull(maxPrice, "maxPrice cannot be null");
  }

  public LiquidityPoolID getLiquidityPoolID() {
    return liquidityPoolID;
  }

  public String getMaxAmountA() {
    return maxAmountA;
  }

  public String getMaxAmountB() {
    return maxAmountB;
  }

  public Price getMinPrice() {
    return minPrice;
  }

  public Price getMaxPrice() {
    return maxPrice;
  }

  @Override
  OperationBody toOperationBody(AccountConverter accountConverter) {
    LiquidityPoolDepositOp op = new LiquidityPoolDepositOp();
    op.setLiquidityPoolID(this.getLiquidityPoolID().toXdr());
    op.setMaxAmountA(new org.stellar.sdk.xdr.Int64(Operation.toXdrAmount(this.getMaxAmountA())));
    op.setMaxAmountB(new org.stellar.sdk.xdr.Int64(Operation.toXdrAmount(this.getMaxAmountB())));
    op.setMinPrice(this.getMinPrice().toXdr());
    op.setMaxPrice(this.getMaxPrice().toXdr());

    OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.LIQUIDITY_POOL_DEPOSIT);
    body.setLiquidityPoolDepositOp(op);
    return body;
  }

  public int hashCode() {
    return Objects.hashCode(
        this.getSourceAccount(), liquidityPoolID, maxAmountA, maxAmountB, minPrice, maxPrice);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof LiquidityPoolDepositOperation)) {
      return false;
    }

    LiquidityPoolDepositOperation o = (LiquidityPoolDepositOperation) object;
    return Objects.equal(this.getLiquidityPoolID(), o.getLiquidityPoolID())
        && Objects.equal(this.getMaxAmountA(), o.getMaxAmountA())
        && Objects.equal(this.getMaxAmountB(), o.getMaxAmountB())
        && Objects.equal(this.getMinPrice(), o.getMinPrice())
        && Objects.equal(this.getMaxPrice(), o.getMaxPrice())
        && Objects.equal(this.getSourceAccount(), o.getSourceAccount());
  }
}
