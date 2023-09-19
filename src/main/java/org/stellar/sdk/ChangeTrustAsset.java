package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ChangeTrustAsset class.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/assets/" target="_blank">Assets</a>
 */
public abstract class ChangeTrustAsset implements Comparable<ChangeTrustAsset> {
  ChangeTrustAsset() {}

  /**
   * Parses an asset string and returns the equivalent ChangeTrustAsset instance. The asset string
   * is expected to either be "native" or a string of the form "CODE:ISSUER"
   *
   * @param canonicalForm Canonical string representation of an asset
   * @return ChangeTrustAsset
   */
  public static ChangeTrustAsset create(String canonicalForm) {
    return new Wrapper(Asset.create(canonicalForm));
  }

  /**
   * Create ChangeTrustAsset from asset primitive values
   *
   * @param type the asset type
   * @param code the asset code
   * @param issuer the asset issuer
   * @return ChangeTrustAsset
   */
  public static ChangeTrustAsset create(String type, String code, String issuer) {
    return new Wrapper(Asset.create(type, code, issuer));
  }

  /**
   * Create ChangeTrustAsset from another Asset
   *
   * @param asset the Asset
   * @return ChangeTrustAsset
   */
  public static ChangeTrustAsset create(Asset asset) {
    return new Wrapper(asset);
  }

  /**
   * Create a ChangeTrustAsset from LiquidityPoolParameters
   *
   * @param params the LiquidityPoolParameters
   * @return ChangeTrustAsset
   */
  public static ChangeTrustAsset create(LiquidityPoolParameters params) {
    return new LiquidityPoolShareChangeTrustAsset(params);
  }

  /**
   * Create a ChangeTrustAsset from TrustLineAsset
   *
   * @param wrapper the TrustLineAsset wrapper
   * @return ChangeTrustAsset
   */
  public static ChangeTrustAsset create(TrustLineAsset.Wrapper wrapper) {
    return new Wrapper(wrapper.getAsset());
  }

  /**
   * Creates one of AssetTypeCreditAlphaNum4 or AssetTypeCreditAlphaNum12 object based on a <code>
   * code</code> length
   *
   * @param code ChangeTrustAsset code
   * @param issuer ChangeTrustAsset issuer
   */
  public static ChangeTrustAsset createNonNativeAsset(String code, String issuer) {
    return create(Asset.create(null, code, issuer));
  }

  /**
   * Generates ChangeTrustAsset object from a given XDR object
   *
   * @param xdr XDR object
   * @return ChangeTrustAsset
   */
  public static ChangeTrustAsset fromXdr(org.stellar.sdk.xdr.ChangeTrustAsset xdr) {
    // TODO: Figure out how we can re-use Asset.fromXdr here
    String accountId;
    switch (xdr.getDiscriminant()) {
      case ASSET_TYPE_NATIVE:
        return ChangeTrustAsset.create(new AssetTypeNative());
      case ASSET_TYPE_CREDIT_ALPHANUM4:
        String assetCode4 =
            Util.paddedByteArrayToString(xdr.getAlphaNum4().getAssetCode().getAssetCode4());
        accountId = StrKey.encodeStellarAccountId(xdr.getAlphaNum4().getIssuer());
        return ChangeTrustAsset.create(new AssetTypeCreditAlphaNum4(assetCode4, accountId));
      case ASSET_TYPE_CREDIT_ALPHANUM12:
        String assetCode12 =
            Util.paddedByteArrayToString(xdr.getAlphaNum12().getAssetCode().getAssetCode12());
        accountId = StrKey.encodeStellarAccountId(xdr.getAlphaNum12().getIssuer());
        return ChangeTrustAsset.create(new AssetTypeCreditAlphaNum12(assetCode12, accountId));
      case ASSET_TYPE_POOL_SHARE:
        return new LiquidityPoolShareChangeTrustAsset(
            LiquidityPoolParameters.fromXdr(xdr.getLiquidityPool()));
      default:
        throw new IllegalArgumentException("Unknown asset type " + xdr.getDiscriminant());
    }
  }

  @Override
  public abstract boolean equals(Object object);

  @Override
  public abstract int compareTo(ChangeTrustAsset other);

  /**
   * Get the asset type
   *
   * @return the asset type
   */
  public abstract String getType();

  /**
   * Generates XDR object from a given ChangeTrustAsset object
   *
   * @return xdr model
   */
  public abstract org.stellar.sdk.xdr.ChangeTrustAsset toXdr();

  public static final class Wrapper extends ChangeTrustAsset {
    private Asset asset;

    public Wrapper(Asset baseAsset) {
      super();
      checkNotNull(baseAsset, "asset cannot be null");
      asset = baseAsset;
    }

    public Asset getAsset() {
      return asset;
    }

    @Override
    public String getType() {
      return asset.getType();
    }

    @Override
    public final boolean equals(Object object) {
      if (object == null || !this.getClass().equals(object.getClass())) {
        return false;
      }

      ChangeTrustAsset.Wrapper o = (ChangeTrustAsset.Wrapper) object;
      return this.getAsset().equals(o.getAsset());
    }

    @Override
    public int compareTo(ChangeTrustAsset other) {
      if (other.getType() == "pool_share") {
        return -1;
      }
      return this.getAsset().compareTo(((ChangeTrustAsset.Wrapper) other).getAsset());
    }

    @Override
    public org.stellar.sdk.xdr.ChangeTrustAsset toXdr() {
      org.stellar.sdk.xdr.ChangeTrustAsset xdr = new org.stellar.sdk.xdr.ChangeTrustAsset();

      org.stellar.sdk.xdr.Asset assetXdr = asset.toXdr();
      xdr.setDiscriminant(assetXdr.getDiscriminant());
      xdr.setAlphaNum4(assetXdr.getAlphaNum4());
      xdr.setAlphaNum12(assetXdr.getAlphaNum12());

      return xdr;
    }
  }
}
