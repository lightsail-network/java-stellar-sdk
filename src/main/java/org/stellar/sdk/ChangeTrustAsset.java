package org.stellar.sdk;

import lombok.Value;
import org.stellar.sdk.xdr.AssetType;

@Value
public class ChangeTrustAsset {
  AssetType assetType;
  Asset asset;
  LiquidityPool liquidityPool; // LiquidityPoolParameters

  public ChangeTrustAsset(Asset asset) {
    this.assetType = asset.getType();
    this.asset = asset;
    this.liquidityPool = null;
  }

  public ChangeTrustAsset(LiquidityPool liquidityPool) {
    this.assetType = AssetType.ASSET_TYPE_POOL_SHARE;
    this.asset = null;
    this.liquidityPool = liquidityPool;
  }

  public org.stellar.sdk.xdr.ChangeTrustAsset toXdr() {
    org.stellar.sdk.xdr.ChangeTrustAsset xdr = new org.stellar.sdk.xdr.ChangeTrustAsset();
    if (asset != null) {
      org.stellar.sdk.xdr.Asset assetXdr = asset.toXdr();
      xdr.setDiscriminant(assetXdr.getDiscriminant());
      xdr.setAlphaNum4(assetXdr.getAlphaNum4());
      xdr.setAlphaNum12(assetXdr.getAlphaNum12());
    }
    if (liquidityPool != null) {
      xdr.setDiscriminant(AssetType.ASSET_TYPE_POOL_SHARE);
      xdr.setLiquidityPool(liquidityPool.toXdr());
    }
    return xdr;
  }

  public static ChangeTrustAsset fromXdr(org.stellar.sdk.xdr.ChangeTrustAsset changeTrustAsset) {
    switch (changeTrustAsset.getDiscriminant()) {
      case ASSET_TYPE_NATIVE:
        return new ChangeTrustAsset(new AssetTypeNative());
      case ASSET_TYPE_CREDIT_ALPHANUM4:
        return new ChangeTrustAsset(
            AssetTypeCreditAlphaNum4.fromXdr(changeTrustAsset.getAlphaNum4()));
      case ASSET_TYPE_CREDIT_ALPHANUM12:
        return new ChangeTrustAsset(
            AssetTypeCreditAlphaNum12.fromXdr(changeTrustAsset.getAlphaNum12()));
      case ASSET_TYPE_POOL_SHARE:
        return new ChangeTrustAsset(LiquidityPool.fromXdr(changeTrustAsset.getLiquidityPool()));
      default:
        throw new IllegalArgumentException(
            "Unknown asset type " + changeTrustAsset.getDiscriminant());
    }
  }
}
