package org.stellar.sdk;

import java.io.IOException;
import lombok.NonNull;
import lombok.Value;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.xdr.LiquidityPoolConstantProductParameters;
import org.stellar.sdk.xdr.LiquidityPoolType;

/**
 * Represents a LiquidityPoolParameters object on the Stellar network.
 *
 * <p>See: <a
 * href="https://developers.stellar.org/docs/learn/encyclopedia/sdex/liquidity-on-stellar-sdex-liquidity-pools#liquidity-pools">Liquidity
 * Pool</a>
 */
@Value
public class LiquidityPool {
  public static int FEE = 30;

  /**
   * The first asset in the pool, it must respect the rule assetA &lt; assetB. Check {@link
   * Asset#compareTo(Asset)} for details.
   */
  @NonNull Asset assetA;

  /**
   * The second asset in the pool, it must respect the rule assetA &lt; assetB. Check {@link
   * Asset#compareTo(Asset)} for details.
   */
  @NonNull Asset assetB;

  /** The liquidity pool fee. For now the only fee supported is {@link LiquidityPool#FEE}. */
  int fee;

  /**
   * Constructs a new LiquidityPool with specified assets and fee.
   *
   * @param assetA The first asset in the pool, it must respect the rule assetA &lt; assetB.
   * @param assetB The second asset in the pool, it must respect the rule assetA &lt; assetB.
   * @param fee The liquidity pool fee. For now the only fee supported is {@link LiquidityPool#FEE}.
   * @throws IllegalArgumentException if assets are not in lexicographic order.
   */
  private LiquidityPool(@NonNull Asset assetA, @NonNull Asset assetB, int fee) {
    if (assetA.compareTo(assetB) >= 0) {
      throw new IllegalArgumentException("Assets are not in lexicographic order");
    }
    this.assetA = assetA;
    this.assetB = assetB;
    this.fee = fee;
  }

  /**
   * Constructs a new LiquidityPool with specified assets and default fee.
   *
   * @param assetA The first asset in the pool, it must respect the rule assetA &lt; assetB.
   * @param assetB The second asset in the pool, it must respect the rule assetA &lt; assetB.
   * @throws IllegalArgumentException if assets are not in lexicographic order.
   */
  public LiquidityPool(Asset assetA, Asset assetB) {
    this(assetA, assetB, FEE);
  }

  /**
   * Converts this LiquidityPool to its XDR representation.
   *
   * @return The XDR object representing this LiquidityPool.
   */
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
   * Creates a LiquidityPool from its XDR representation.
   *
   * @param xdr The XDR object to convert from.
   * @return A new LiquidityPool object.
   * @throws IllegalArgumentException if the XDR object is not of type
   *     LIQUIDITY_POOL_CONSTANT_PRODUCT.
   */
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

  /**
   * Generates the LiquidityPoolID for this LiquidityPool.
   *
   * @return The LiquidityPoolID object.
   */
  public LiquidityPoolID getLiquidityPoolId() {
    org.stellar.sdk.xdr.LiquidityPoolParameters liquidityPoolParameters = toXdr();
    try {
      byte[] poolId = Util.hash(liquidityPoolParameters.toXdrByteArray());
      return new LiquidityPoolID(poolId);
    } catch (IOException e) {
      throw new UnexpectedException(e);
    }
  }
}
