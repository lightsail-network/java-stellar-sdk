package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.xdr.AssetType;

@EqualsAndHashCode
@Getter
public class TrustLineAsset {
  @NonNull private final AssetType assetType;
  @Nullable private final Asset asset;
  @Nullable private final LiquidityPoolID liquidityPoolId;

  public TrustLineAsset(@NonNull Asset asset) {
    this.assetType = asset.getType();
    this.asset = asset;
    this.liquidityPoolId = null;
  }

  public TrustLineAsset(@NonNull LiquidityPoolID liquidityPoolId) {
    this.assetType = AssetType.ASSET_TYPE_POOL_SHARE;
    this.asset = null;
    this.liquidityPoolId = liquidityPoolId;
  }

  public org.stellar.sdk.xdr.TrustLineAsset toXdr() {
    org.stellar.sdk.xdr.TrustLineAsset xdr = new org.stellar.sdk.xdr.TrustLineAsset();
    if (asset != null) {
      org.stellar.sdk.xdr.Asset assetXdr = asset.toXdr();
      xdr.setDiscriminant(assetXdr.getDiscriminant());
      xdr.setAlphaNum4(assetXdr.getAlphaNum4());
      xdr.setAlphaNum12(assetXdr.getAlphaNum12());
    }
    if (liquidityPoolId != null) {
      xdr.setDiscriminant(AssetType.ASSET_TYPE_POOL_SHARE);
      xdr.setLiquidityPoolID(liquidityPoolId.toXdr());
    }
    return xdr;
  }

  public static TrustLineAsset fromXdr(org.stellar.sdk.xdr.TrustLineAsset trustLineAsset) {
    switch (trustLineAsset.getDiscriminant()) {
      case ASSET_TYPE_NATIVE:
        return new TrustLineAsset(new AssetTypeNative());
      case ASSET_TYPE_CREDIT_ALPHANUM4:
        return new TrustLineAsset(AssetTypeCreditAlphaNum4.fromXdr(trustLineAsset.getAlphaNum4()));
      case ASSET_TYPE_CREDIT_ALPHANUM12:
        return new TrustLineAsset(
            AssetTypeCreditAlphaNum12.fromXdr(trustLineAsset.getAlphaNum12()));
      case ASSET_TYPE_POOL_SHARE:
        return new TrustLineAsset(LiquidityPoolID.fromXdr(trustLineAsset.getLiquidityPoolID()));
      default:
        throw new IllegalArgumentException(
            "Unknown asset type " + trustLineAsset.getDiscriminant());
    }
  }
}
