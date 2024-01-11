package org.stellar.sdk;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.xdr.LiquidityPoolDepositOp;
import org.stellar.sdk.xdr.LiquidityPoolType;
import org.stellar.sdk.xdr.Operation.OperationBody;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#liquidity-pool-deposit"
 * target="_blank">LiquidityPoolDeposit</a> operation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LiquidityPoolDepositOperation extends Operation {
  /** The liquidity pool ID. * */
  @NonNull private final LiquidityPoolID liquidityPoolID;

  /** Maximum amount of first asset to deposit. * */
  @NonNull private final String maxAmountA;

  /** Maximum amount of second asset to deposit. * */
  @NonNull private final String maxAmountB;

  /** Minimum deposit_a/deposit_b price. * */
  @NonNull private final Price minPrice;

  /** Maximum deposit_a/deposit_b price. * */
  @NonNull private final Price maxPrice;

  public LiquidityPoolDepositOperation(LiquidityPoolDepositOp op) {
    this.liquidityPoolID = LiquidityPoolID.fromXdr(op.getLiquidityPoolID());
    this.maxAmountA = Operation.fromXdrAmount(op.getMaxAmountA().getInt64());
    this.maxAmountB = Operation.fromXdrAmount(op.getMaxAmountB().getInt64());
    this.minPrice = Price.fromXdr(op.getMinPrice());
    this.maxPrice = Price.fromXdr(op.getMaxPrice());
  }

  public LiquidityPoolDepositOperation(
      AssetAmount a, AssetAmount b, @NonNull Price minPrice, @NonNull Price maxPrice) {
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
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
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
}
