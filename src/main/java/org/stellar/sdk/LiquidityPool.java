package org.stellar.sdk;

import lombok.NonNull;
import lombok.Value;
import org.stellar.sdk.xdr.LiquidityPoolConstantProductParameters;
import org.stellar.sdk.xdr.LiquidityPoolType;

@Value
public class LiquidityPool {
  public static Integer FEE = 30;

  @NonNull Asset assetA;
  @NonNull Asset assetB;
  int fee;

  public LiquidityPool(@NonNull Asset assetA, @NonNull Asset assetB, int fee) {
    if (assetA.compareTo(assetB) >= 0) {
      throw new IllegalArgumentException("Assets are not in lexicographic order");
    }

    this.assetA = assetA;
    this.assetB = assetB;
    this.fee = fee;
  }

  public LiquidityPool(Asset assetA, Asset assetB) {
    this(assetA, assetB, FEE);
  }

  public org.stellar.sdk.xdr.LiquidityPoolParameters toXdr() {
    org.stellar.sdk.xdr.LiquidityPoolParameters xdr =
        new org.stellar.sdk.xdr.LiquidityPoolParameters();
    xdr.setDiscriminant(LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT);

    org.stellar.sdk.xdr.LiquidityPoolConstantProductParameters params =
        new org.stellar.sdk.xdr.LiquidityPoolConstantProductParameters();
    params.setAssetA(assetA.toXdr());
    params.setAssetB(assetB.toXdr());
    params.setFee(new org.stellar.sdk.xdr.Int32(fee));
    xdr.setConstantProduct(params);
    return xdr;
  }

  public static LiquidityPool fromXdr(org.stellar.sdk.xdr.LiquidityPoolParameters xdr) {

    if (xdr.getDiscriminant() != LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT) {
      throw new IllegalArgumentException(
          String.format("Invalid LiquidityPoolType: %s", xdr.getDiscriminant()));
    }
    LiquidityPoolConstantProductParameters parameters = xdr.getConstantProduct();
    return new LiquidityPool(
        Asset.fromXdr(parameters.getAssetA()),
        Asset.fromXdr(parameters.getAssetB()),
        parameters.getFee().getInt32());
  }

  public LiquidityPoolID getLiquidityPoolId() {
    return new LiquidityPoolID(this);
  }
}
