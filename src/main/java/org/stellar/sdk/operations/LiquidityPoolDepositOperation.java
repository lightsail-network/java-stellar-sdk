package org.stellar.sdk.operations;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.Asset;
import org.stellar.sdk.LiquidityPool;
import org.stellar.sdk.Price;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.Hash;
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
  /** The liquidity pool ID. */
  @NonNull private final String liquidityPoolId;

  /** Maximum amount of first asset to deposit (max of 7 decimal places). */
  @NonNull private final BigDecimal maxAmountA;

  /** Maximum amount of second asset to deposit (max of 7 decimal places). */
  @NonNull private final BigDecimal maxAmountB;

  /** Minimum deposit_a/deposit_b price. */
  @NonNull private final Price minPrice;

  /** Maximum deposit_a/deposit_b price. */
  @NonNull private final Price maxPrice;

  public LiquidityPoolDepositOperation(
      @NonNull Asset assetA,
      @NonNull BigDecimal maxAmountA,
      @NonNull Asset assetB,
      @NonNull BigDecimal maxAmountB,
      @NonNull Price minPrice,
      @NonNull Price maxPrice) {
    this.liquidityPoolId = new LiquidityPool(assetA, assetB).getLiquidityPoolId();
    this.maxAmountA = maxAmountA;
    this.maxAmountB = maxAmountB;
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
  }

  /**
   * Construct a new {@link LiquidityPoolDepositOperation} object from the {@link
   * LiquidityPoolDepositOp} XDR object.
   *
   * @param op {@link LiquidityPoolDepositOp} XDR object
   * @return {@link LiquidityPoolDepositOperation} object
   */
  public static LiquidityPoolDepositOperation fromXdr(LiquidityPoolDepositOp op) {
    String liquidityPoolId =
        Util.bytesToHex(op.getLiquidityPoolID().getPoolID().getHash()).toLowerCase();
    BigDecimal maxAmountA = Operation.fromXdrAmount(op.getMaxAmountA().getInt64());
    BigDecimal maxAmountB = Operation.fromXdrAmount(op.getMaxAmountB().getInt64());
    Price minPrice = Price.fromXdr(op.getMinPrice());
    Price maxPrice = Price.fromXdr(op.getMaxPrice());
    return new LiquidityPoolDepositOperation(
        liquidityPoolId, maxAmountA, maxAmountB, minPrice, maxPrice);
  }

  @Override
  OperationBody toOperationBody() {
    LiquidityPoolDepositOp op = new LiquidityPoolDepositOp();
    op.setLiquidityPoolID(
        new org.stellar.sdk.xdr.PoolID(new Hash(Util.hexToBytes(this.getLiquidityPoolId()))));
    op.setMaxAmountA(new org.stellar.sdk.xdr.Int64(Operation.toXdrAmount(this.getMaxAmountA())));
    op.setMaxAmountB(new org.stellar.sdk.xdr.Int64(Operation.toXdrAmount(this.getMaxAmountB())));
    op.setMinPrice(this.getMinPrice().toXdr());
    op.setMaxPrice(this.getMaxPrice().toXdr());

    OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.LIQUIDITY_POOL_DEPOSIT);
    body.setLiquidityPoolDepositOp(op);
    return body;
  }

  public abstract static class LiquidityPoolDepositOperationBuilder<
          C extends LiquidityPoolDepositOperation,
          B extends LiquidityPoolDepositOperationBuilder<C, B>>
      extends OperationBuilder<C, B> {
    public B liquidityPoolId(@NonNull String liquidityPoolId) {
      this.liquidityPoolId = liquidityPoolId.toLowerCase();
      return self();
    }

    public B maxAmountA(@NonNull BigDecimal maxAmountA) {
      this.maxAmountA = Operation.formatAmountScale(maxAmountA);
      return self();
    }

    public B maxAmountB(@NonNull BigDecimal maxAmountB) {
      this.maxAmountB = Operation.formatAmountScale(maxAmountB);
      return self();
    }
  }
}
