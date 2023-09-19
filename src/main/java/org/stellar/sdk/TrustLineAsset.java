package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TrustLineAsset class.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/assets/" target="_blank">Assets</a>
 */
public abstract class TrustLineAsset implements Comparable<TrustLineAsset> {
  TrustLineAsset() {}

  /**
   * Parses an asset string and returns the equivalent TrustLineAsset instance. The asset string is
   * expected to either be "native" or a string of the form "CODE:ISSUER"
   *
   * @param canonicalForm Canonical string representation of an asset
   */
  public static TrustLineAsset create(String canonicalForm) {
    return new Wrapper(Asset.create(canonicalForm));
  }

  /**
   * Parses type, code, issuer and returns the equivalent TrustLineAsset instance.
   *
   * @param type the asset type
   * @param code the asset code
   * @param issuer the assset issuer
   * @return TrustLineAsset
   */
  public static TrustLineAsset create(String type, String code, String issuer) {
    return new Wrapper(Asset.create(type, code, issuer));
  }

  /**
   * Converts Asset to TrustLineAsset
   *
   * @param asset the Asset
   * @return TrustLineAsset
   */
  public static TrustLineAsset create(Asset asset) {
    return new Wrapper(asset);
  }

  /**
   * Creates a TrustLineAsset from LiquidityPoolParameters
   *
   * @param params the LiquidityPoolParameters
   * @return TrustLineAsset
   */
  public static TrustLineAsset create(LiquidityPoolParameters params) {
    return new LiquidityPoolShareTrustLineAsset(params);
  }

  /**
   * Creates a TrustLineAsset from LiquidityPoolID
   *
   * @param id the LiquidityPoolID
   * @return TrustLineAsset
   */
  public static TrustLineAsset create(LiquidityPoolID id) {
    return new LiquidityPoolShareTrustLineAsset(id);
  }

  /**
   * Creates a TrustLineAsset from ChangeTrustAsset
   *
   * @param wrapper the ChangeTrustAsset wrapper
   * @return TrustLineAsset
   */
  public static TrustLineAsset create(ChangeTrustAsset.Wrapper wrapper) {
    return new Wrapper(wrapper.getAsset());
  }

  /**
   * Create TrustLineAsset from LiquidityPoolShareChangeTrustAsset
   *
   * @param share the LiquidityPoolShareChangeTrustAsset
   * @return TrustLineAsset
   */
  public static TrustLineAsset create(LiquidityPoolShareChangeTrustAsset share) {
    return new LiquidityPoolShareTrustLineAsset(share.getLiquidityPoolParams());
  }

  /**
   * Creates TrustLineAsset based on a <code>code</code> length and issuer only
   *
   * @param code the TrustLineAsset code
   * @param issuer the TrustLineAsset issuer
   */
  public static TrustLineAsset createNonNativeAsset(String code, String issuer) {
    return create(Asset.create(null, code, issuer));
  }

  /**
   * Generates TrustLineAsset object from a given XDR object
   *
   * @param xdr XDR object
   */
  public static TrustLineAsset fromXdr(org.stellar.sdk.xdr.TrustLineAsset xdr) {
    // TODO: Figure out how we can re-use Asset.fromXdr here
    String accountId;
    switch (xdr.getDiscriminant()) {
      case ASSET_TYPE_NATIVE:
        return TrustLineAsset.create(new AssetTypeNative());
      case ASSET_TYPE_CREDIT_ALPHANUM4:
        String assetCode4 =
            Util.paddedByteArrayToString(xdr.getAlphaNum4().getAssetCode().getAssetCode4());
        accountId = StrKey.encodeStellarAccountId(xdr.getAlphaNum4().getIssuer());
        return TrustLineAsset.create(new AssetTypeCreditAlphaNum4(assetCode4, accountId));
      case ASSET_TYPE_CREDIT_ALPHANUM12:
        String assetCode12 =
            Util.paddedByteArrayToString(xdr.getAlphaNum12().getAssetCode().getAssetCode12());
        accountId = StrKey.encodeStellarAccountId(xdr.getAlphaNum12().getIssuer());
        return TrustLineAsset.create(new AssetTypeCreditAlphaNum12(assetCode12, accountId));
      case ASSET_TYPE_POOL_SHARE:
        return new LiquidityPoolShareTrustLineAsset(
            LiquidityPoolID.fromXdr(xdr.getLiquidityPoolID()));
      default:
        throw new IllegalArgumentException("Unknown asset type " + xdr.getDiscriminant());
    }
  }

  @Override
  public abstract boolean equals(Object object);

  @Override
  public abstract int compareTo(TrustLineAsset other);

  /**
   * Get the asset type
   *
   * @return the asset type
   */
  public abstract String getType();

  /**
   * Generates XDR object from a given TrustLineAsset object
   *
   * @return xdr model
   */
  public abstract org.stellar.sdk.xdr.TrustLineAsset toXdr();

  public static final class Wrapper extends TrustLineAsset {
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

      TrustLineAsset.Wrapper o = (TrustLineAsset.Wrapper) object;
      return this.getAsset().equals(o.getAsset());
    }

    @Override
    public int compareTo(TrustLineAsset other) {
      if (other.getType() == "pool_share") {
        return -1;
      }
      return this.getAsset().compareTo(((TrustLineAsset.Wrapper) other).getAsset());
    }

    @Override
    public org.stellar.sdk.xdr.TrustLineAsset toXdr() {
      org.stellar.sdk.xdr.TrustLineAsset xdr = new org.stellar.sdk.xdr.TrustLineAsset();

      org.stellar.sdk.xdr.Asset assetXdr = asset.toXdr();
      xdr.setDiscriminant(assetXdr.getDiscriminant());
      xdr.setAlphaNum4(assetXdr.getAlphaNum4());
      xdr.setAlphaNum12(assetXdr.getAlphaNum12());

      return xdr;
    }
  }
}
