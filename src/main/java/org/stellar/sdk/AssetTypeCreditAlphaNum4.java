package org.stellar.sdk;

import lombok.NonNull;
import org.stellar.sdk.exception.AssetCodeLengthInvalidException;
import org.stellar.sdk.xdr.*;

/**
 * Represents all assets with codes 1-4 characters long.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/learn/fundamentals/stellar-data-structures/assets"
 *     target="_blank">Assets</a>
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
    if (code.isEmpty() || code.length() > 4) {
      throw new AssetCodeLengthInvalidException();
    }
  }

  @Override
  public AssetType getType() {
    return AssetType.ASSET_TYPE_CREDIT_ALPHANUM4;
  }

  public static AssetTypeCreditAlphaNum4 fromXdr(org.stellar.sdk.xdr.AlphaNum4 alphaNum4) {
    String assetCode4 = Util.paddedByteArrayToString(alphaNum4.getAssetCode().getAssetCode4());
    String accountId = StrKey.encodeEd25519PublicKey(alphaNum4.getIssuer());
    return new AssetTypeCreditAlphaNum4(assetCode4, accountId);
  }

  @Override
  public org.stellar.sdk.xdr.Asset toXdr() {
    org.stellar.sdk.xdr.Asset xdr = new org.stellar.sdk.xdr.Asset();
    xdr.setDiscriminant(AssetType.ASSET_TYPE_CREDIT_ALPHANUM4);
    org.stellar.sdk.xdr.AlphaNum4 credit = new org.stellar.sdk.xdr.AlphaNum4();
    AssetCode4 assetCode4 = new AssetCode4();
    assetCode4.setAssetCode4(Util.paddedByteArray(code, 4));
    credit.setAssetCode(assetCode4);
    credit.setIssuer(StrKey.encodeToXDRAccountId(issuer));
    xdr.setAlphaNum4(credit);
    return xdr;
  }

  @Override
  public int compareTo(@NonNull Asset other) {
    if (AssetType.ASSET_TYPE_CREDIT_ALPHANUM12.equals(other.getType())) {
      return -1;
    } else if (AssetType.ASSET_TYPE_NATIVE.equals(other.getType())) {
      return 1;
    }

    AssetTypeCreditAlphaNum o = (AssetTypeCreditAlphaNum) other;

    if (!this.getCode().equals(o.getCode())) {
      return this.getCode().compareTo(o.getCode());
    }

    return this.getIssuer().compareTo(o.getIssuer());
  }
}
