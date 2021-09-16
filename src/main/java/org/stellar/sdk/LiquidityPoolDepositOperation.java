package org.stellar.sdk;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import org.stellar.sdk.xdr.LiquidityPoolDepositOp;
import org.stellar.sdk.xdr.LiquidityPoolType;
import org.stellar.sdk.xdr.Operation.OperationBody;
import org.stellar.sdk.xdr.OperationType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html#liquidity-pool-deposit" target="_blank">LiquidityPoolDeposit</a> operation.
 * @see <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html" target="_blank">List of Operations</a>
 */
public class LiquidityPoolDepositOperation extends Operation {
    private final LiquidityPoolID liquidityPoolID;
    private final String maxAmountA;
    private final String maxAmountB;
    private final Price minPrice;
    private final Price maxPrice;

    private LiquidityPoolDepositOperation(LiquidityPoolID liquidityPoolID, String maxAmountA, String maxAmountB, Price minPrice, Price maxPrice) {
      this.liquidityPoolID = checkNotNull(liquidityPoolID, "liquidityPoolID cannot be null");
      this.maxAmountA = checkNotNull(maxAmountA, "amountA cannot be null");
      this.maxAmountB = checkNotNull(maxAmountB, "amountB cannot be null");
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

    /**
     * Builds LiquidityPoolDeposit operation.
     * @see LiquidityPoolDepositOperation
     */
    public static class Builder {
        private Optional<LiquidityPoolID> liquidityPoolID = Optional.absent();
        private Optional<Asset> a = Optional.absent();
        private Optional<Asset> b = Optional.absent();
        private String maxAmountA;
        private String maxAmountB;
        private Price minPrice;
        private Price maxPrice;

        private String mSourceAccount;

        Builder(LiquidityPoolDepositOp op) {
          this.liquidityPoolID = Optional.fromNullable(LiquidityPoolID.fromXdr(op.getLiquidityPoolID()));
          this.maxAmountA = Operation.fromXdrAmount(op.getMaxAmountA().getInt64().longValue());
          this.maxAmountB = Operation.fromXdrAmount(op.getMaxAmountB().getInt64().longValue());
          this.minPrice = Price.fromXdr(op.getMinPrice());
          this.maxPrice = Price.fromXdr(op.getMaxPrice());
        }

        /**
         * Creates a new LiquidityPoolDeposit builder.
         */
        public Builder(AssetAmount a, AssetAmount b, Price minPrice, Price maxPrice) {
          if (a.getAsset().compareTo(b.getAsset()) >= 0) {
            throw new RuntimeException("AssetA must be < AssetB");
          }
          this.liquidityPoolID = Optional.of(new LiquidityPoolID(LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT, a.getAsset(), b.getAsset(), LiquidityPoolParameters.Fee));
          this.a = Optional.of(a.getAsset());
          this.b = Optional.of(b.getAsset());
          this.maxAmountA = a.getAmount();
          this.maxAmountB = b.getAmount();
          this.minPrice = minPrice;
          this.maxPrice = maxPrice;
        }

        /**
         * Creates a new LiquidityPoolDeposit builder.
         */
        public Builder(LiquidityPoolID liquidityPoolID, String amountA, String amountB, Price minPrice, Price maxPrice) {
          this.liquidityPoolID = Optional.fromNullable(liquidityPoolID);
          this.maxAmountA = amountA;
          this.maxAmountB = amountB;
          this.minPrice = minPrice;
          this.maxPrice = maxPrice;
        }

        /**
         * Set asset and amount A of this operation
         * @param asset Asset A
         * @param maxAmount Max amount of asset A to deposit
         * @return Builder object so you can chain methods.
         */
        public Builder setA(Asset asset, String maxAmount) {
          this.liquidityPoolID = Optional.absent();
          this.a = Optional.of(asset);
          this.maxAmountA = maxAmount;
          return this;
        }

        /**
         * Set asset and amount B of this operation
         * @param asset Asset B
         * @param maxAmount Max amount of asset B to deposit
         * @return Builder object so you can chain methods.
         */
        public Builder setB(Asset asset, String maxAmount) {
          this.liquidityPoolID = Optional.absent();
          this.b = Optional.of(asset);
          this.maxAmountB = maxAmount;
          return this;
        }

        /**
         * Set minimum price for this deposit.
         * @param value Min price for this operation
         * @return Builder object so you can chain methods.
         */
        public Builder setMinPrice(Price value) {
          this.minPrice = value;
          return this;
        }

        /**
         * Set maximum price for this deposit.
         * @param value Max price for this operation
         * @return Builder object so you can chain methods.
         */
        public Builder setMaxPrice(Price value) {
          this.maxPrice = value;
          return this;
        }

        /**
         * Set source account of this operation
         * @param sourceAccount Source account
         * @return Builder object so you can chain methods.
         */
        public Builder setSourceAccount(String sourceAccount) {
            mSourceAccount = sourceAccount;
            return this;
        }

        /**
         * Builds an operation
         */
        public LiquidityPoolDepositOperation build() {
            if (a.isPresent() && b.isPresent()) {
                if (a.get().compareTo(b.get()) >= 0) {
                    throw new RuntimeException("AssetA must be < AssetB");
                }

                if (!liquidityPoolID.isPresent()) {
                    liquidityPoolID = Optional.of(new LiquidityPoolID(LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT, a.get(), b.get(), LiquidityPoolParameters.Fee));
                }
            }

            LiquidityPoolDepositOperation op = new LiquidityPoolDepositOperation(liquidityPoolID.get(), maxAmountA, maxAmountB, minPrice, maxPrice);
            if (mSourceAccount != null) {
              op.setSourceAccount(mSourceAccount);
            }
            return op;
        }
    }

    public int hashCode() {
        return Objects.hashCode(liquidityPoolID, maxAmountA, maxAmountB, minPrice, maxPrice);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LiquidityPoolDepositOperation)) {
            return false;
        }

        LiquidityPoolDepositOperation o = (LiquidityPoolDepositOperation) object;
        return Objects.equal(this.getLiquidityPoolID(), o.getLiquidityPoolID()) &&
                Objects.equal(this.getMaxAmountA(), o.getMaxAmountA()) &&
                Objects.equal(this.getMaxAmountB(), o.getMaxAmountB()) &&
                Objects.equal(this.getMinPrice(), o.getMinPrice()) &&
                Objects.equal(this.getMaxPrice(), o.getMaxPrice());
    }
}
