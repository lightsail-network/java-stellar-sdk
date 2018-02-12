package org.stellar.sdk;

/**
 * Base Asset class.
 * @see <a href="https://www.stellar.org/developers/learn/concepts/assets.html" target="_blank">Assets</a>
 */
public abstract class Asset {
  Asset() {}

  public static Asset create(String type, String code, String issuer) {
    if (type.equals("native")) {
      return new AssetTypeNative();
    } else {
      return Asset.createNonNativeAsset(code, KeyPair.fromAccountId(issuer));
    }
  }

  /**
   * Creates one of AssetTypeCreditAlphaNum4 or AssetTypeCreditAlphaNum12 object based on a <code>code</code> length
   * @param code Asset code
   * @param issuer Asset issuer
   */
  public static Asset createNonNativeAsset(String code, KeyPair issuer) {
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
    switch (xdr.getDiscriminant()) {
      case ASSET_TYPE_NATIVE:
        return new AssetTypeNative();
      case ASSET_TYPE_CREDIT_ALPHANUM4:
        String assetCode4 = Util.paddedByteArrayToString(xdr.getAlphaNum4().getAssetCode());
        KeyPair issuer4 = KeyPair.fromXdrPublicKey(
                xdr.getAlphaNum4().getIssuer().getAccountID());
        return new AssetTypeCreditAlphaNum4(assetCode4, issuer4);
      case ASSET_TYPE_CREDIT_ALPHANUM12:
        String assetCode12 = Util.paddedByteArrayToString(xdr.getAlphaNum12().getAssetCode());
        KeyPair issuer12 = KeyPair.fromXdrPublicKey(xdr.getAlphaNum12().getIssuer().getAccountID());
        return new AssetTypeCreditAlphaNum12(assetCode12, issuer12);
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
