package org.stellar.sdk;

import lombok.NonNull;
import org.stellar.sdk.exception.AssetCodeLengthInvalidException;
import org.stellar.sdk.xdr.*;

/**
 * Represents all assets with codes 5-12 characters long.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/fundamentals-and-concepts/stellar-data-structures/assets"
 *     target="_blank">Assets</a>
 */
public final class AssetTypeCreditAlphaNum12 extends AssetTypeCreditAlphaNum {

  /**
   * Class constructor
   *
   * @param code Asset code
   * @param issuer Asset issuer
   */
  public AssetTypeCreditAlphaNum12(String code, String issuer) {
    super(code, issuer);
    if (code.length() < 5 || code.length() > 12) {
      throw new AssetCodeLengthInvalidException();
    }
  }

  @Override
  public AssetType getType() {
    return AssetType.ASSET_TYPE_CREDIT_ALPHANUM12;
  }

  public static AssetTypeCreditAlphaNum12 fromXdr(org.stellar.sdk.xdr.AlphaNum12 alphaNum12) {
    String assetCode12 = Util.paddedByteArrayToString(alphaNum12.getAssetCode().getAssetCode12());
    String accountId = StrKey.encodeEd25519PublicKey(alphaNum12.getIssuer());
    return new AssetTypeCreditAlphaNum12(assetCode12, accountId);
  }

  @Override
  public org.stellar.sdk.xdr.Asset toXdr() {
    org.stellar.sdk.xdr.Asset xdr = new org.stellar.sdk.xdr.Asset();
    xdr.setDiscriminant(AssetType.ASSET_TYPE_CREDIT_ALPHANUM12);
    org.stellar.sdk.xdr.AlphaNum12 credit = new org.stellar.sdk.xdr.AlphaNum12();
    AssetCode12 assetCode12 = new AssetCode12();
    assetCode12.setAssetCode12(Util.paddedByteArray(code, 12));
    credit.setAssetCode(assetCode12);
    credit.setIssuer(StrKey.encodeToXDRAccountId(issuer));
    xdr.setAlphaNum12(credit);
    return xdr;
  }

  @Override
  public int compareTo(@NonNull Asset other) {
    if (!AssetType.ASSET_TYPE_CREDIT_ALPHANUM12.equals(other.getType())) {
      return 1;
    }

    AssetTypeCreditAlphaNum o = (AssetTypeCreditAlphaNum) other;

    if (!this.getCode().equals(o.getCode())) {
      return this.getCode().compareTo(o.getCode());
    }
    return this.getIssuer().compareTo(o.getIssuer());
  }
}
