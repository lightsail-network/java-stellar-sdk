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
  /** The liquidity pool ID. * */
  @NonNull private final LiquidityPoolId liquidityPoolID;

  /** Amount of pool shares to withdraw. * */
  @NonNull private final String amount;

  /** Minimum amount of first asset to withdraw. * */
  @NonNull private final String minAmountA;

  /** Minimum amount of second asset to withdraw. * */
  @NonNull private final String minAmountB;

  public LiquidityPoolWithdrawOperation(
      @NonNull AssetAmount a, @NonNull AssetAmount b, @NonNull String amount) {
    this.liquidityPoolID =
        new LiquidityPoolParameters(a.getAsset(), b.getAsset()).getLiquidityPoolId();
    this.amount = amount;
    this.minAmountA = a.getAmount();
    this.minAmountB = b.getAmount();
  }

  /**
   * Construct a new {@link LiquidityPoolWithdrawOperation} object from the {@link AccountConverter}
   * object and the {@link LiquidityPoolWithdrawOp} XDR object.
   *
   * @param op {@link LiquidityPoolWithdrawOp} XDR object
   * @return {@link LiquidityPoolWithdrawOperation} object
   */
  public static LiquidityPoolWithdrawOperation fromXdr(LiquidityPoolWithdrawOp op) {
    LiquidityPoolId liquidityPoolID = LiquidityPoolId.fromXdr(op.getLiquidityPoolID());
    String amount = Operation.fromXdrAmount(op.getAmount().getInt64());
    String minAmountA = Operation.fromXdrAmount(op.getMinAmountA().getInt64());
    String minAmountB = Operation.fromXdrAmount(op.getMinAmountB().getInt64());
    return new LiquidityPoolWithdrawOperation(liquidityPoolID, amount, minAmountA, minAmountB);
  }

  @Override
  OperationBody toOperationBody(AccountConverter accountConverter) {
    LiquidityPoolWithdrawOp op = new LiquidityPoolWithdrawOp();
    op.setLiquidityPoolID(this.getLiquidityPoolID().toXdr());
    op.setAmount(new org.stellar.sdk.xdr.Int64(Operation.toXdrAmount(this.getAmount())));
    op.setMinAmountA(new org.stellar.sdk.xdr.Int64(Operation.toXdrAmount(this.getMinAmountA())));
    op.setMinAmountB(new org.stellar.sdk.xdr.Int64(Operation.toXdrAmount(this.getMinAmountB())));

    OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.LIQUIDITY_POOL_WITHDRAW);
    body.setLiquidityPoolWithdrawOp(op);
    return body;
  }
}
