package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.AssetType;

/**
 * Class for LiquidityPoolShareChangeTrustAsset
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/liquidity-pool/"
 *     target="_blank">Liquidity Pool</a>
 */
public final class LiquidityPoolShareChangeTrustAsset extends ChangeTrustAsset {
  protected final LiquidityPoolParameters mParams;

  /**
   * Constructor
   *
   * @param params the liquidity pool parameters
   */
  public LiquidityPoolShareChangeTrustAsset(LiquidityPoolParameters params) {
    checkNotNull(params, "params cannot be null");
    mParams = params;
  }

  /**
   * Get the liquidity pool id
   *
   * @return LiquidityPoolID
   */
  public LiquidityPoolID getLiquidityPoolID() {
    return mParams.getId();
  }

  /**
   * Get the liquidity pool parameters
   *
   * @return LiquidityPoolParameters
   */
  public LiquidityPoolParameters getLiquidityPoolParams() {
    return mParams;
  }

  @Override
  public String getType() {
    return "pool_share";
  }

  @Override
  public String toString() {
    return this.getLiquidityPoolID().toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.mParams);
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || !this.getClass().equals(object.getClass())) {
      return false;
    }

    LiquidityPoolShareChangeTrustAsset o = (LiquidityPoolShareChangeTrustAsset) object;

    return this.getLiquidityPoolParams().equals(o.getLiquidityPoolParams());
  }

  @Override
  public int compareTo(ChangeTrustAsset other) {
    if (other.getType() != "pool_share") {
      return 1;
    }
    return this.toString().compareTo(((LiquidityPoolShareChangeTrustAsset) other).toString());
  }

  @Override
  public org.stellar.sdk.xdr.ChangeTrustAsset toXdr() {
    org.stellar.sdk.xdr.ChangeTrustAsset xdr = new org.stellar.sdk.xdr.ChangeTrustAsset();
    xdr.setDiscriminant(AssetType.ASSET_TYPE_POOL_SHARE);
    org.stellar.sdk.xdr.LiquidityPoolParameters params = mParams.toXdr();
    xdr.setLiquidityPool(params);
    return xdr;
  }
}
