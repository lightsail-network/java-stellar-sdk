package org.stellar.sdk;

/**
 * Base Asset class.
 * @see <a href="https://developers.stellar.org/docs/glossary/assets/" target="_blank">Assets</a>
 */
public abstract class Asset implements Comparable<Asset> {
  Asset() {}

  /**
   * Parses an asset string and returns the equivalent Asset instance.
   * The asset string is expected to either be "native" or a string of Alpha4 or Alpha12
   * asset code as "CODE:ISSUER"
   *
   * @param canonicalForm Canonical string representation of an Alpha4 or Alpha12 asset
   * @return Asset or throws IllegalArgumentException if not Alpha4 or Alpha12 asset code
   */
  public static Asset create(String canonicalForm) {
    if (canonicalForm.equals("native")) {
      return new AssetTypeNative();
    }
    String [] parts = canonicalForm.split(":");
    if (parts.length != 2) {
      throw new IllegalArgumentException("invalid asset "+ canonicalForm);
    }
    return Asset.createNonNativeAsset(parts[0], parts[1]);
  }

  /**
   * Creates Asset for Alpha4/Alpha5/Native
   *
   * @param type the type of asset can be 'native', 'alpha4', 'alpha12'
   * @param code the asset code that conforms to type or null
   * @param issuer the asset issuer the conforms to type or null
   * @return
   */
  public static Asset create(String type, String code, String issuer) {
    return create(type, code, issuer, null);
  }

  /**
   * Creates Asset for Alpha4/Alpha5/Native/LiquidityPool
   *
   * @param type the type of asset can be 'native', 'alpha4', 'alpha12' or 'liquidity_pool_shares'
   * @param code the asset code that conforms to type or null
   * @param issuer the asset issuer the conforms to type or null
   * @param liquidityPoolID provided only if type is 'liquidity_pool_shares'
   * @return Asset
   */
  public static Asset create(String type, String code, String issuer, String liquidityPoolID) {
    if (type.equals("native")) {
      return new AssetTypeNative();
    }
    if (type.equals("liquidity_pool_shares")) {
      return new AssetTypePoolShare(liquidityPoolID);
    }

    return Asset.createNonNativeAsset(code, issuer);
  }

  public static Asset create(ChangeTrustAsset.Wrapper wrapped) {
    return wrapped.getAsset();
  }
  public static Asset create(TrustLineAsset.Wrapper wrapped) {
    return wrapped.getAsset();
  }

  /**
   * Creates one of AssetTypeCreditAlphaNum4 or AssetTypeCreditAlphaNum12 object based on a <code>code</code> length
   * @param code Asset code
   * @param issuer Asset issuer
   */
  public static Asset createNonNativeAsset(String code, String issuer) {
    if (code.length() >= 1 && code.length() <= 4) {
      return new AssetTypeCreditAlphaNum4(code, issuer);
    } else if (code.length() >= 5 && code.length() <= 12) {
      return new AssetTypeCreditAlphaNum12(code, issuer);
    } else {
      throw new AssetCodeLengthInvalidException();
    }
  }

  /**
   * Generates Asset object from a given XDR object
   * @param xdr XDR object
   */
  public static Asset fromXdr(org.stellar.sdk.xdr.Asset xdr) {
    String accountId;
    switch (xdr.getDiscriminant()) {
      case ASSET_TYPE_NATIVE:
        return new AssetTypeNative();
      case ASSET_TYPE_CREDIT_ALPHANUM4:
        String assetCode4 = Util.paddedByteArrayToString(xdr.getAlphaNum4().getAssetCode().getAssetCode4());
        accountId = StrKey.encodeStellarAccountId(xdr.getAlphaNum4().getIssuer());
        return new AssetTypeCreditAlphaNum4(assetCode4, accountId);
      case ASSET_TYPE_CREDIT_ALPHANUM12:
        String assetCode12 = Util.paddedByteArrayToString(xdr.getAlphaNum12().getAssetCode().getAssetCode12());
        accountId = StrKey.encodeStellarAccountId(xdr.getAlphaNum12().getIssuer());
        return new AssetTypeCreditAlphaNum12(assetCode12, accountId);
      default:
        throw new IllegalArgumentException("Unknown asset type " + xdr.getDiscriminant());
    }
  }

  /**
   * Returns asset type. Possible types:
   * <ul>
   *   <li><code>native</code></li>
   *   <li><code>credit_alphanum4</code></li>
   *   <li><code>credit_alphanum12</code></li>
   *   <li><code>liquidity_pool_shares</code></li>
   * </ul>
   */
  public abstract String getType();

  @Override
  public abstract boolean equals(Object object);

  /**
   * Generates XDR object from a given Asset object
   */
  public abstract org.stellar.sdk.xdr.Asset toXdr();

  public abstract int compareTo(Asset other);
}
