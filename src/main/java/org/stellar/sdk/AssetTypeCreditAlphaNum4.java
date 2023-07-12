package org.stellar.sdk;

import org.stellar.sdk.xdr.*;

/**
 * Represents all assets with codes 1-4 characters long.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/assets/" target="_blank">Assets</a>
 */
public final class AssetTypeCreditAlphaNum4 extends AssetTypeCreditAlphaNum {

  /**
   * Class constructor
   *
   * @param code Asset code
   * @param issuer Asset issuer
   */
  public AssetTypeCreditAlphaNum4(String code, String issuer) {
    super(code, issuer);
    if (code.length() < 1 || code.length() > 4) {
      throw new AssetCodeLengthInvalidException();
    }
  }

  @Override
  public String getType() {
    return "credit_alphanum4";
  }

  @Override
  public org.stellar.sdk.xdr.Asset toXdr() {
    org.stellar.sdk.xdr.Asset xdr = new org.stellar.sdk.xdr.Asset();
    xdr.setDiscriminant(AssetType.ASSET_TYPE_CREDIT_ALPHANUM4);
    org.stellar.sdk.xdr.AlphaNum4 credit = new org.stellar.sdk.xdr.AlphaNum4();
    AssetCode4 assetCode4 = new AssetCode4();
    assetCode4.setAssetCode4(Util.paddedByteArray(mCode, 4));
    credit.setAssetCode(assetCode4);
    credit.setIssuer(StrKey.encodeToXDRAccountId(mIssuer));
    xdr.setAlphaNum4(credit);
    return xdr;
  }

  @Override
  public int compareTo(Asset other) {
    if (other.getType() == "credit_alphanum12") {
      return -1;
    } else if (other.getType() == "native") {
      return 1;
    }

    AssetTypeCreditAlphaNum o = (AssetTypeCreditAlphaNum) other;

    if (!this.getCode().equals(o.getCode())) {
      return this.getCode().compareTo(o.getCode());
    }

    return this.getIssuer().compareTo(o.getIssuer());
  }
}
