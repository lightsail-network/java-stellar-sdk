package org.stellar.sdk;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

import org.stellar.sdk.xdr.*;

/**
 * Class for LiquidityPoolShareChangeTrustAsset
 * @see <a href="https://www.stellar.org/developers/learn/concepts/assets.html" target="_blank">Assets</a>
 */
public final class LiquidityPoolShareChangeTrustAsset extends ChangeTrustAsset {
  protected final LiquidityPoolParameters mParams;

  public LiquidityPoolShareChangeTrustAsset(LiquidityPoolParameters params) {
    checkNotNull(params, "params cannot be null");
    mParams = params;
  }

  public LiquidityPoolID getLiquidityPoolID() {
    return mParams.getId();
  }

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
  public org.stellar.sdk.xdr.ChangeTrustAsset toXdr() {
    org.stellar.sdk.xdr.ChangeTrustAsset xdr = new org.stellar.sdk.xdr.ChangeTrustAsset();
    xdr.setDiscriminant(AssetType.ASSET_TYPE_POOL_SHARE);
    org.stellar.sdk.xdr.LiquidityPoolParameters params = mParams.toXdr();
    xdr.setLiquidityPool(params);
    return xdr;
  }
}
