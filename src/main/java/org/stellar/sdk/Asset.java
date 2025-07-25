package org.stellar.sdk;

import java.io.IOException;
import lombok.NonNull;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.xdr.AssetType;
import org.stellar.sdk.xdr.ContractIDPreimage;
import org.stellar.sdk.xdr.ContractIDPreimageType;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.HashIDPreimage;

/**
 * Base Asset class.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/learn/fundamentals/stellar-data-structures/assets"
 *     target="_blank">Assets</a>
 */
public abstract class Asset implements Comparable<Asset> {
  Asset() {}

  /**
   * Parses an asset string and returns the equivalent Asset instance. The asset string is expected
   * to either be "native" or a string of Alpha4 or Alpha12 asset code as "CODE:ISSUER"
   *
   * @param canonicalForm Canonical string representation of an Alpha4 or Alpha12 asset
   * @return Asset or throws IllegalArgumentException if not Alpha4 or Alpha12 asset code
   * @throws IllegalArgumentException if the asset string is invalid
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
    if (type == null) {
      return createNonNativeAsset(code, issuer);
    }
    if (type.equals("native")) {
      return new AssetTypeNative();
    }
    return createNonNativeAsset(code, issuer);
  }

  /**
   * Generates Asset object from a given XDR object
   *
   * @param xdr XDR object
   */
  public static Asset fromXdr(org.stellar.sdk.xdr.Asset xdr) {
    switch (xdr.getDiscriminant()) {
      case ASSET_TYPE_NATIVE:
        return new AssetTypeNative();
      case ASSET_TYPE_CREDIT_ALPHANUM4:
        return AssetTypeCreditAlphaNum4.fromXdr(xdr.getAlphaNum4());
      case ASSET_TYPE_CREDIT_ALPHANUM12:
        return AssetTypeCreditAlphaNum12.fromXdr(xdr.getAlphaNum12());
      default:
        throw new IllegalArgumentException("Unknown asset type " + xdr.getDiscriminant());
    }
  }

  /** Returns asset type. */
  public abstract AssetType getType();

  /** Generates XDR object from a given Asset object */
  public abstract org.stellar.sdk.xdr.Asset toXdr();

  @Override
  public abstract boolean equals(Object object);

  /**
   * Compares two assets.
   *
   * <ol>
   *   <li>First compare the type (eg. native before alphanum4 before alphanum12).
   *   <li>If the types are equal, compare the assets codes.
   *   <li>If the asset codes are equal, compare the issuers.
   * </ol>
   *
   * @param other the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   *     or greater than the specified object.
   */
  @Override
  public abstract int compareTo(@NonNull Asset other);

  /**
   * Creates a new non-native asset.
   *
   * @param code The asset code.
   * @param issuer The issuer account ID.
   * @return Asset (alphanum4 or alphanum12)
   * @throws IllegalArgumentException if the asset code is invalid
   */
  public static Asset createNonNativeAsset(@NonNull String code, @NonNull String issuer) {
    if (!code.isEmpty() && code.length() <= 4) {
      return new AssetTypeCreditAlphaNum4(code, issuer);
    } else if (code.length() >= 5 && code.length() <= 12) {
      return new AssetTypeCreditAlphaNum12(code, issuer);
    } else {
      throw new IllegalArgumentException("The length of code must be between 1 and 12 characters.");
    }
  }

  /**
   * Creates a new native asset.
   *
   * @return Asset (native)
   */
  public static Asset createNativeAsset() {
    return new AssetTypeNative();
  }

  /**
   * Returns the contract Id for the asset contract.
   *
   * @param network The network where the asset is located.
   * @return The contract Id for the asset contract.
   */
  public String getContractId(Network network) {
    HashIDPreimage preimage =
        HashIDPreimage.builder()
            .discriminant(EnvelopeType.ENVELOPE_TYPE_CONTRACT_ID)
            .contractID(
                HashIDPreimage.HashIDPreimageContractID.builder()
                    .networkID(new Hash(network.getNetworkId()))
                    .contractIDPreimage(
                        ContractIDPreimage.builder()
                            .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ASSET)
                            .fromAsset(this.toXdr())
                            .build())
                    .build())
            .build();
    byte[] rawContractId = null;
    try {
      rawContractId = Util.hash(preimage.toXdrByteArray());
    } catch (IOException e) {
      throw new UnexpectedException(e);
    }
    return StrKey.encodeContract(rawContractId);
  }
}
