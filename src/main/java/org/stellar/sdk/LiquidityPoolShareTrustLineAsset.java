package org.stellar.sdk;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

import org.stellar.sdk.xdr.*;

/**
 * Class for LiquidityPoolShareTrustLineAsset
 * @see <a href="https://www.stellar.org/developers/learn/concepts/assets.html" target="_blank">Assets</a>
 */
public final class LiquidityPoolShareTrustLineAsset extends TrustLineAsset {
  protected final LiquidityPoolID mId;

  public LiquidityPoolShareTrustLineAsset(LiquidityPoolParameters params) {
    checkNotNull(params, "params cannot be null");
    mId = params.getId();
  }

  public LiquidityPoolShareTrustLineAsset(LiquidityPoolID id) {
    checkNotNull(id, "id cannot be null");
    mId = id;
  }

  public LiquidityPoolID getLiquidityPoolID() {
    return mId;
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
    return Objects.hashCode(this.mId);
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || !this.getClass().equals(object.getClass())) {
        return false;
    }

    LiquidityPoolShareTrustLineAsset o = (LiquidityPoolShareTrustLineAsset) object;

    return this.toString() == o.toString();
  }

  @Override
  public org.stellar.sdk.xdr.TrustLineAsset toXdr() {
    org.stellar.sdk.xdr.TrustLineAsset xdr = new org.stellar.sdk.xdr.TrustLineAsset();
    xdr.setDiscriminant(AssetType.ASSET_TYPE_POOL_SHARE);
    xdr.setLiquidityPoolID(mId.toXdr());
    return xdr;
  }
}
