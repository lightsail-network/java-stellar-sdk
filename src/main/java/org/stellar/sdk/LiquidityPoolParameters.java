package org.stellar.sdk;

import org.stellar.sdk.xdr.*;

/**
 * Base LiquidityPoolParameters class.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/liquidity-pool/"
 *     target="_blank">Liquidity Pool</a>
 */
public abstract class LiquidityPoolParameters {
  LiquidityPoolParameters() {}

  public static Integer Fee = 30;

  /**
   * LiquidityPoolParameters represents the parameters specifying a liquidity pool.
   *
   * @param type Type of the liquidity pool
   * @param a First asset in the liquidity pool
   * @param b Second asset in the liquidity pool
   * @param feeBP Fee amount in base-points
   */
  public static LiquidityPoolParameters create(
      LiquidityPoolType type, Asset a, Asset b, int feeBP) {
    if (type != LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT) {
      throw new IllegalArgumentException("Unknown liquidity pool type type " + type);
    }
    return new LiquidityPoolConstantProductParameters(a, b, feeBP);
  }

  /**
   * Generates LiquidityPoolParameters object from a given XDR object
   *
   * @param xdr XDR object
   */
  public static LiquidityPoolParameters fromXdr(org.stellar.sdk.xdr.LiquidityPoolParameters xdr) {
    switch (xdr.getDiscriminant()) {
      case LIQUIDITY_POOL_CONSTANT_PRODUCT:
        return LiquidityPoolConstantProductParameters.fromXdr(xdr.getConstantProduct());
      default:
        throw new IllegalArgumentException("Unknown liquidity pool type " + xdr.getDiscriminant());
    }
  }

  @Override
  public abstract boolean equals(Object object);

  /** Generates XDR object from a given LiquidityPoolParameters object */
  public abstract org.stellar.sdk.xdr.LiquidityPoolParameters toXdr();

  public abstract org.stellar.sdk.LiquidityPoolID getId();
}
