package org.stellar.sdk.operations;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.LiquidityPoolParameters;
import org.stellar.sdk.xdr.LiquidityPoolType;
import org.stellar.sdk.xdr.LiquidityPoolWithdrawOp;
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
public class LiquidityPoolWithdrawOperation extends Operation {
  /** The liquidity pool ID. * */
  @NonNull private final LiquidityPoolID liquidityPoolID;

  /** Amount of pool shares to withdraw. * */
  @NonNull private final String amount;

  /** Minimum amount of first asset to withdraw. * */
  @NonNull private final String minAmountA;

  /** Minimum amount of second asset to withdraw. * */
  @NonNull private final String minAmountB;

  public LiquidityPoolWithdrawOperation(LiquidityPoolWithdrawOp op) {
    this.liquidityPoolID = LiquidityPoolID.fromXdr(op.getLiquidityPoolID());
    this.amount = Operation.fromXdrAmount(op.getAmount().getInt64());
    this.minAmountA = Operation.fromXdrAmount(op.getMinAmountA().getInt64());
    this.minAmountB = Operation.fromXdrAmount(op.getMinAmountB().getInt64());
  }

  public LiquidityPoolWithdrawOperation(AssetAmount a, AssetAmount b, @NonNull String amount) {
    this.liquidityPoolID =
        new LiquidityPoolID(
            LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT,
            a.getAsset(),
            b.getAsset(),
            LiquidityPoolParameters.Fee);
    this.amount = amount;
    this.minAmountA = a.getAmount();
    this.minAmountB = b.getAmount();
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
