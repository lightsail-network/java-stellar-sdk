package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.xdr.LiquidityPoolType;

/**
 * Base LiquidityPoolConstantProductParameters class.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/encyclopedia/liquidity-on-stellar-sdex-liquidity-pools#liquidity-pools"
 *     target="_blank">Liquidity Pool</a>
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class LiquidityPoolConstantProductParameters extends LiquidityPoolParameters {
  /** First asset in the liquidity pool */
  Asset assetA;

  /** Second asset in the liquidity pool */
  Asset assetB;

  /** Fee amount in base-points */
  int fee;

  /** Generates XDR object from a given LiquidityPoolParameters object */
  @Override
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

  /**
   * Generates LiquidityPoolConstantProductParameters object from a given XDR object
   *
   * @param xdr XDR object
   */
  public static LiquidityPoolConstantProductParameters fromXdr(
      org.stellar.sdk.xdr.LiquidityPoolConstantProductParameters xdr) {
    return new LiquidityPoolConstantProductParameters(
        Asset.fromXdr(xdr.getAssetA()), Asset.fromXdr(xdr.getAssetB()), xdr.getFee().getInt32());
  }

  @Override
  public LiquidityPoolID getId() {
    return new LiquidityPoolID(
        LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT, assetA, assetB, fee);
  }
}
