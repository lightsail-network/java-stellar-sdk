package org.stellar.sdk;

import lombok.NonNull;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.xdr.AssetType;

/**
 * Represents an asset in a change trust operation on the Stellar network. This class can represent
 * both regular assets and liquidity pool share for change trustline operation.
 *
 * @see org.stellar.sdk.operations.ChangeTrustOperation
 */
@Value
public class ChangeTrustAsset {
  /** The type of the asset. */
  @NonNull AssetType assetType;

  /**
   * The asset for which the trustline is being changed. This is null if the change trust operation
   * is for a liquidity pool share.
   *
   * <p>If assetType is one of {@link AssetType#ASSET_TYPE_NATIVE}, {@link
   * AssetType#ASSET_TYPE_CREDIT_ALPHANUM4} or {@link AssetType#ASSET_TYPE_CREDIT_ALPHANUM12} then
   * this field will be set.
   */
  @Nullable Asset asset;

  /**
   * The liquidity pool for which the trustline is being changed. This is null if the change trust
   * operation is for a regular asset.
   *
   * <p>If assetType is {@link AssetType#ASSET_TYPE_POOL_SHARE} then this field will be set.
   */
  @Nullable LiquidityPoolParameters liquidityPool;

  /**
   * Creates a ChangeTrustAsset for a regular asset.
   *
   * @param asset The asset for which the trust is being changed.
   */
  public ChangeTrustAsset(@NonNull Asset asset) {
    this.assetType = asset.getType();
    this.asset = asset;
    this.liquidityPool = null;
  }

  /**
   * Creates a ChangeTrustAsset for a liquidity pool share.
   *
   * @param liquidityPool The liquidity pool for which the trust is being changed.
   */
  public ChangeTrustAsset(@NonNull LiquidityPoolParameters liquidityPool) {
    this.assetType = AssetType.ASSET_TYPE_POOL_SHARE;
    this.asset = null;
    this.liquidityPool = liquidityPool;
  }

  /**
   * Converts this ChangeTrustAsset to its XDR representation.
   *
   * @return The XDR representation of this ChangeTrustAsset.
   */
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

  /**
   * Creates a ChangeTrustAsset from its XDR representation.
   *
   * @param changeTrustAsset The XDR representation of the ChangeTrustAsset.
   * @return A new ChangeTrustAsset instance.
   * @throws IllegalArgumentException if the asset type is unknown.
   */
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
        return new ChangeTrustAsset(
            LiquidityPoolParameters.fromXdr(changeTrustAsset.getLiquidityPool()));
      default:
        throw new IllegalArgumentException(
            "Unknown asset type " + changeTrustAsset.getDiscriminant());
    }
  }
}
