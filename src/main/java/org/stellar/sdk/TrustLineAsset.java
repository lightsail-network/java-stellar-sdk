package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.xdr.AssetType;
import org.stellar.sdk.xdr.Hash;

/**
 * Represents a trustline asset in the Stellar network. A trustline is a declaration that an account
 * trusts an issuer of an asset up to a certain limit. This class can represent both regular assets
 * and liquidity pool shares.
 */
@EqualsAndHashCode
@Getter
public class TrustLineAsset {
  /** The type of the asset. */
  @NonNull private final AssetType assetType;

  /**
   * The asset for which the trustline is established. This is null if the trustline is for a
   * liquidity pool share.
   *
   * <p>If assetType is one of {@link AssetType#ASSET_TYPE_NATIVE}, {@link
   * AssetType#ASSET_TYPE_CREDIT_ALPHANUM4} or {@link AssetType#ASSET_TYPE_CREDIT_ALPHANUM12} then
   * this field will be set.
   */
  @Nullable private final Asset asset;

  /**
   * The ID of the liquidity pool for which the trustline is established. This is null if the
   * trustline is for a regular asset.
   *
   * <p>If assetType is {@link AssetType#ASSET_TYPE_POOL_SHARE} then this field will be set.
   */
  @Nullable private final String liquidityPoolId;

  /**
   * Creates a TrustLineAsset for a regular asset.
   *
   * @param asset The asset for which the trustline is created.
   */
  public TrustLineAsset(@NonNull Asset asset) {
    this.assetType = asset.getType();
    this.asset = asset;
    this.liquidityPoolId = null;
  }

  /**
   * Creates a TrustLineAsset for a liquidity pool share.
   *
   * @param liquidityPoolId The ID of the liquidity pool.
   */
  public TrustLineAsset(@NonNull String liquidityPoolId) {
    this.assetType = AssetType.ASSET_TYPE_POOL_SHARE;
    this.asset = null;
    this.liquidityPoolId = liquidityPoolId.toLowerCase();
  }

  /**
   * Converts this TrustLineAsset to its XDR representation.
   *
   * @return The XDR representation of this TrustLineAsset.
   */
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
      xdr.setLiquidityPoolID(
          new org.stellar.sdk.xdr.PoolID(new Hash(Util.hexToBytes(liquidityPoolId))));
    }
    return xdr;
  }

  /**
   * Creates a TrustLineAsset from its XDR representation.
   *
   * @param trustLineAsset The XDR representation of the TrustLineAsset.
   * @return A new TrustLineAsset instance.
   * @throws IllegalArgumentException if the asset type is unknown.
   */
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
        return new TrustLineAsset(
            Util.bytesToHex(trustLineAsset.getLiquidityPoolID().getPoolID().getHash())
                .toLowerCase());
      default:
        throw new IllegalArgumentException(
            "Unknown asset type " + trustLineAsset.getDiscriminant());
    }
  }
}
