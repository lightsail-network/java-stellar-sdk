package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.LiquidityPoolType;

/**
 * Base LiquidityPoolConstantProductParameters class.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/liquidity-pool/"
 *     target="_blank">Liquidity Pool</a>
 */
public final class LiquidityPoolConstantProductParameters extends LiquidityPoolParameters {
  protected final Asset assetA;
  protected final Asset assetB;
  protected final int fee;

  /**
   * LiquidityPoolConstantProductParameters represents the parameters specifying a constant-product
   * liquidity pool.
   *
   * @param a First asset in the liquidity pool
   * @param b Second asset in the liquidity pool
   * @param feeBP Fee amount in base-points
   */
  public LiquidityPoolConstantProductParameters(Asset a, Asset b, int feeBP) {
    checkNotNull(a, "asset A cannot be null");
    checkNotNull(b, "asset B cannot be null");
    checkNotNull(feeBP, "fee cannot be null");
    assetA = a;
    assetB = b;
    fee = feeBP;
  }

  public Asset getAssetA() {
    return assetA;
  }

  public Asset getAssetB() {
    return assetB;
  }

  public int getFee() {
    return fee;
  }

  @Override
  public final boolean equals(Object object) {
    if (object == null || !this.getClass().equals(object.getClass())) {
      return false;
    }

    LiquidityPoolConstantProductParameters o = (LiquidityPoolConstantProductParameters) object;
    return Objects.equal(this.getAssetA(), o.getAssetA())
        && Objects.equal(this.getAssetB(), o.getAssetB())
        && Objects.equal(this.getFee(), o.getFee());
  }

  /** Generates XDR object from a given LiquidityPoolParameters object */
  @Override
  public final org.stellar.sdk.xdr.LiquidityPoolParameters toXdr() {
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
        Asset.fromXdr(xdr.getAssetA()),
        Asset.fromXdr(xdr.getAssetB()),
        xdr.getFee().getInt32().intValue());
  }

  @Override
  public final LiquidityPoolID getId() {
    return new LiquidityPoolID(
        LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT, assetA, assetB, fee);
  }
}
