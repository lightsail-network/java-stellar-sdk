package org.stellar.sdk;

import java.util.Objects;
import lombok.NonNull;
import org.stellar.sdk.xdr.LiquidityPoolType;
import org.stellar.sdk.xdr.LiquidityPoolWithdrawOp;
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
public class LiquidityPoolWithdrawOperation extends Operation {
  private final LiquidityPoolID liquidityPoolID;
  private final String amount;
  private final String minAmountA;
  private final String minAmountB;

  public LiquidityPoolWithdrawOperation(
      @NonNull LiquidityPoolID liquidityPoolID,
      @NonNull String amount,
      @NonNull String minAmountA,
      @NonNull String minAmountB) {
    this.liquidityPoolID = liquidityPoolID;
    this.amount = amount;
    this.minAmountA = minAmountA;
    this.minAmountB = minAmountB;
  }

  public LiquidityPoolWithdrawOperation(LiquidityPoolWithdrawOp op) {
    this.liquidityPoolID = LiquidityPoolID.fromXdr(op.getLiquidityPoolID());
    this.amount = Operation.fromXdrAmount(op.getAmount().getInt64().longValue());
    this.minAmountA = Operation.fromXdrAmount(op.getMinAmountA().getInt64().longValue());
    this.minAmountB = Operation.fromXdrAmount(op.getMinAmountB().getInt64().longValue());
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

  public LiquidityPoolID getLiquidityPoolID() {
    return liquidityPoolID;
  }

  public String getAmount() {
    return amount;
  }

  public String getMinAmountA() {
    return minAmountA;
  }

  public String getMinAmountB() {
    return minAmountB;
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

  public int hashCode() {
    return Objects.hash(this.getSourceAccount(), liquidityPoolID, amount, minAmountA, minAmountB);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof LiquidityPoolWithdrawOperation)) {
      return false;
    }

    LiquidityPoolWithdrawOperation o = (LiquidityPoolWithdrawOperation) object;
    return Objects.equals(this.getLiquidityPoolID(), o.getLiquidityPoolID())
        && Objects.equals(this.getAmount(), o.getAmount())
        && Objects.equals(this.getMinAmountA(), o.getMinAmountA())
        && Objects.equals(this.getMinAmountB(), o.getMinAmountB())
        && Objects.equals(this.getSourceAccount(), o.getSourceAccount());
  }
}
