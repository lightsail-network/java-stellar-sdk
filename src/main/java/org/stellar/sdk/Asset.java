package org.stellar.sdk;

/**
 * Base Asset class.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/assets/" target="_blank">Assets</a>
 */
public abstract class Asset implements Comparable<Asset> {
  Asset() {}

  /**
   * Parses an asset string and returns the equivalent Asset instance. The asset string is expected
   * to either be "native" or a string of Alpha4 or Alpha12 asset code as "CODE:ISSUER"
   *
   * @param canonicalForm Canonical string representation of an Alpha4 or Alpha12 asset
   * @return Asset or throws IllegalArgumentException if not Alpha4 or Alpha12 asset code
   */
  public static Asset create(String canonicalForm) {
    if (canonicalForm.equals("native")) {
      return create(canonicalForm, null, null);
    }
    String[] parts = canonicalForm.split(":");
    if (parts.length != 2) {
      throw new IllegalArgumentException("invalid asset " + canonicalForm);
    }
    return create(null, parts[0], parts[1]);
  }

  /**
   * Creates Asset for Alpha4/Alpha12/Native
   *
   * @param type the type of asset. 'native' will generate its respective asset sub-class, if null
   *     or any other value will attempt to derive the asset sub-class from code and issuer.
   * @param code the asset code or null
   * @param issuer the asset issuer or null
   * @return Asset
   */
  public static Asset create(String type, String code, String issuer) {
    return create(type, code, issuer, null);
  }

  /**
   * Creates Asset for Alpha4/Alpha12/Native/LiquidityPool
   *
   * @param type the type of asset. 'native' and 'liquidity_pool_shares' will generate their
   *     respective asset sub-classes null or any other value will attempt to derive the asset
   *     sub-class from code and issuer.
   * @param code the asset code or null
   * @param issuer the asset issuer or null
   * @param liquidityPoolID required only if type is 'liquidity_pool_shares'
   * @return Asset
   */
  public static Asset create(String type, String code, String issuer, String liquidityPoolID) {
    if (type == null) {
      return createNonNativeAsset(code, issuer);
    }
    if (type.equals("native")) {
      return new AssetTypeNative();
    }
    if (type.equals("liquidity_pool_shares")) {
      return new AssetTypePoolShare(liquidityPoolID);
    }
    return createNonNativeAsset(code, issuer);
  }

  /**
   * Create Asset from a ChangeTrustAsset
   *
   * @param wrapped the ChangeTrustAsset wrapper
   * @return Asset
   */
  public static Asset create(ChangeTrustAsset.Wrapper wrapped) {
    return wrapped.getAsset();
  }

  /**
   * Create Asset from a TrustLineAsset
   *
   * @param wrapped the TrustLineAsset wrapper
   * @return Asset
   */
  public static Asset create(TrustLineAsset.Wrapper wrapped) {
    return wrapped.getAsset();
  }

  /**
   * Generates Asset object from a given XDR object
   *
   * @param xdr XDR object
   */
  public static Asset fromXdr(org.stellar.sdk.xdr.Asset xdr) {
    String accountId;
    switch (xdr.getDiscriminant()) {
      case ASSET_TYPE_NATIVE:
        return new AssetTypeNative();
      case ASSET_TYPE_CREDIT_ALPHANUM4:
        String assetCode4 =
            Util.paddedByteArrayToString(xdr.getAlphaNum4().getAssetCode().getAssetCode4());
        accountId = StrKey.encodeStellarAccountId(xdr.getAlphaNum4().getIssuer());
        return new AssetTypeCreditAlphaNum4(assetCode4, accountId);
      case ASSET_TYPE_CREDIT_ALPHANUM12:
        String assetCode12 =
            Util.paddedByteArrayToString(xdr.getAlphaNum12().getAssetCode().getAssetCode12());
        accountId = StrKey.encodeStellarAccountId(xdr.getAlphaNum12().getIssuer());
        return new AssetTypeCreditAlphaNum12(assetCode12, accountId);
      default:
        throw new IllegalArgumentException("Unknown asset type " + xdr.getDiscriminant());
    }
  }

  /**
   * Returns asset type. Possible types:
   *
   * <ul>
   *   <li><code>native</code>
   *   <li><code>credit_alphanum4</code>
   *   <li><code>credit_alphanum12</code>
   *   <li><code>liquidity_pool_shares</code>
   * </ul>
   */
  public abstract String getType();

  /** Generates XDR object from a given Asset object */
  public abstract org.stellar.sdk.xdr.Asset toXdr();

  @Override
  public abstract boolean equals(Object object);

  @Override
  public abstract int compareTo(Asset other);

  private static Asset createNonNativeAsset(String code, String issuer) {
    if (code.length() >= 1 && code.length() <= 4) {
      return new AssetTypeCreditAlphaNum4(code, issuer);
    } else if (code.length() >= 5 && code.length() <= 12) {
      return new AssetTypeCreditAlphaNum12(code, issuer);
    } else {
      throw new AssetCodeLengthInvalidException();
    }
  }
}
