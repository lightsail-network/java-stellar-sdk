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
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.LiquidityPoolWithdrawOp;
import org.stellar.sdk.xdr.Operation.OperationBody;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#liquidity-pool-withdraw"
 * target="_blank">iquidityPoolWithdraw</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class LiquidityPoolWithdrawOperation extends Operation {
  /** The liquidity pool ID. */
  @NonNull private final String liquidityPoolID;

  /** Amount of pool shares to withdraw (max of 7 decimal places). */
  @NonNull private final BigDecimal amount;

  /** Minimum amount of first asset to withdraw (max of 7 decimal places). */
  @NonNull private final BigDecimal minAmountA;

  /** Minimum amount of second asset to withdraw (max of 7 decimal places). */
  @NonNull private final BigDecimal minAmountB;

  public LiquidityPoolWithdrawOperation(
      @NonNull Asset assetA,
      @NonNull BigDecimal minAmountA,
      @NonNull Asset assetB,
      @NonNull BigDecimal minAmountB,
      @NonNull BigDecimal amount) {
    this.liquidityPoolID = new LiquidityPool(assetA, assetB).getLiquidityPoolId();
    this.amount = amount;
    this.minAmountA = minAmountA;
    this.minAmountB = minAmountB;
  }

  /**
   * Construct a new {@link LiquidityPoolWithdrawOperation} object from the {@link
   * LiquidityPoolWithdrawOp} XDR object.
   *
   * @param op {@link LiquidityPoolWithdrawOp} XDR object
   * @return {@link LiquidityPoolWithdrawOperation} object
   */
  public static LiquidityPoolWithdrawOperation fromXdr(LiquidityPoolWithdrawOp op) {
    String liquidityPoolID =
        Util.bytesToHex(op.getLiquidityPoolID().getPoolID().getHash()).toLowerCase();
    BigDecimal amount = Operation.fromXdrAmount(op.getAmount().getInt64());
    BigDecimal minAmountA = Operation.fromXdrAmount(op.getMinAmountA().getInt64());
    BigDecimal minAmountB = Operation.fromXdrAmount(op.getMinAmountB().getInt64());
    return new LiquidityPoolWithdrawOperation(liquidityPoolID, amount, minAmountA, minAmountB);
  }

  @Override
  OperationBody toOperationBody() {
    LiquidityPoolWithdrawOp op = new LiquidityPoolWithdrawOp();
    op.setLiquidityPoolID(
        new org.stellar.sdk.xdr.PoolID(new Hash(Util.hexToBytes(this.getLiquidityPoolID()))));
    op.setAmount(new org.stellar.sdk.xdr.Int64(Operation.toXdrAmount(this.getAmount())));
    op.setMinAmountA(new org.stellar.sdk.xdr.Int64(Operation.toXdrAmount(this.getMinAmountA())));
    op.setMinAmountB(new org.stellar.sdk.xdr.Int64(Operation.toXdrAmount(this.getMinAmountB())));

    OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.LIQUIDITY_POOL_WITHDRAW);
    body.setLiquidityPoolWithdrawOp(op);
    return body;
  }

  public abstract static class LiquidityPoolWithdrawOperationBuilder<
          C extends LiquidityPoolWithdrawOperation,
          B extends LiquidityPoolWithdrawOperationBuilder<C, B>>
      extends OperationBuilder<C, B> {
    public B liquidityPoolID(@NonNull String liquidityPoolID) {
      this.liquidityPoolID = liquidityPoolID.toLowerCase();
      return self();
    }

    public B amount(@NonNull BigDecimal amount) {
      this.amount = Operation.formatAmountScale(amount);
      return self();
    }

    public B minAmountA(@NonNull BigDecimal minAmountA) {
      this.minAmountA = Operation.formatAmountScale(minAmountA);
      return self();
    }

    public B minAmountB(@NonNull BigDecimal minAmountB) {
      this.minAmountB = Operation.formatAmountScale(minAmountB);
      return self();
    }
  }
}
