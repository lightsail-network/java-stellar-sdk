package org.stellar.sdk;

/**
 * Base Asset class.
 * @see <a href="https://www.stellar.org/developers/learn/concepts/assets.html" target="_blank">Assets</a>
 */
public abstract class Asset {
  Asset() {}

  /**
   * Parses an asset string and returns the equivalent Asset instance.
   * The asset string is expected to either be "native" or a string of the form "CODE:ISSUER"
   *
   * @param canonicalForm Canonical string representation of an asset
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

  public static Asset create(String type, String code, String issuer) {
    if (type.equals("native")) {
      return new AssetTypeNative();
    } else {
      return Asset.createNonNativeAsset(code, issuer);
    }
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
   *   <li><code>pool_share</code></li>
   * </ul>
   */
  public abstract String getType();

  @Override
  public abstract boolean equals(Object object);

  /**
   * Generates XDR object from a given Asset object
   */
  public abstract org.stellar.sdk.xdr.Asset toXdr();
}
