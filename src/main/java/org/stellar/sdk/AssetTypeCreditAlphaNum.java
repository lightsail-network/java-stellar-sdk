package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Base class for {@link AssetTypeCreditAlphaNum4} and {@link AssetTypeCreditAlphaNum12} subclasses.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/learn/fundamentals/stellar-data-structures/assets"
 *     target="_blank">Assets</a>
 */
@EqualsAndHashCode(callSuper = false)
@Getter
public abstract class AssetTypeCreditAlphaNum extends Asset {
  /** Asset code */
  @NonNull protected final String code;

  /** Asset issuer */
  @NonNull protected final String issuer;

  /**
   * Class constructor
   *
   * @param code Asset code
   * @param issuer Asset issuer
   * @throws IllegalArgumentException when the issuer is not a valid Ed25519 public key.
   */
  public AssetTypeCreditAlphaNum(@NonNull String code, @NonNull String issuer) {
    if (!StrKey.isValidEd25519PublicKey(issuer)) {
      throw new IllegalArgumentException("Invalid issuer: " + issuer);
    }

    this.code = code;
    this.issuer = issuer;
  }

  @Override
  public String toString() {
    return getCode() + ":" + getIssuer();
  }
}
