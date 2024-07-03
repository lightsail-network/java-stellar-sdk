package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.LiquidityPoolId;
import org.stellar.sdk.LiquidityPoolParameters;
import org.stellar.sdk.Price;
import org.stellar.sdk.xdr.LiquidityPoolDepositOp;
import org.stellar.sdk.xdr.Operation.OperationBody;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#liquidity-pool-deposit"
 * target="_blank">LiquidityPoolDeposit</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class LiquidityPoolDepositOperation extends Operation {
  /** The liquidity pool ID. * */
  @NonNull private final LiquidityPoolId liquidityPoolID;

  /** Maximum amount of first asset to deposit. * */
  @NonNull private final String maxAmountA;

  /** Maximum amount of second asset to deposit. * */
  @NonNull private final String maxAmountB;

  /** Minimum deposit_a/deposit_b price. * */
  @NonNull private final Price minPrice;

  /** Maximum deposit_a/deposit_b price. * */
  @NonNull private final Price maxPrice;

  public LiquidityPoolDepositOperation(
      @NonNull AssetAmount a,
      @NonNull AssetAmount b,
      @NonNull Price minPrice,
      @NonNull Price maxPrice) {
    if (a.getAsset().compareTo(b.getAsset()) >= 0) {
      throw new IllegalArgumentException("AssetA must be < AssetB");
    }
    this.liquidityPoolID =
        new LiquidityPoolParameters(a.getAsset(), b.getAsset()).getLiquidityPoolId();
    this.maxAmountA = a.getAmount();
    this.maxAmountB = b.getAmount();
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
  }

  /**
   * Construct a new {@link LiquidityPoolDepositOperation} object from the {@link AccountConverter}
   * object and the {@link LiquidityPoolDepositOp} XDR object.
   *
   * @param op {@link LiquidityPoolDepositOp} XDR object
   * @return {@link LiquidityPoolDepositOperation} object
   */
  public static LiquidityPoolDepositOperation fromXdr(LiquidityPoolDepositOp op) {
    LiquidityPoolId liquidityPoolID = LiquidityPoolId.fromXdr(op.getLiquidityPoolID());
    String maxAmountA = Operation.fromXdrAmount(op.getMaxAmountA().getInt64());
    String maxAmountB = Operation.fromXdrAmount(op.getMaxAmountB().getInt64());
    Price minPrice = Price.fromXdr(op.getMinPrice());
    Price maxPrice = Price.fromXdr(op.getMaxPrice());
    return new LiquidityPoolDepositOperation(
        liquidityPoolID, maxAmountA, maxAmountB, minPrice, maxPrice);
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
