package org.stellar.sdk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Base class for AssetTypeCreditAlphaNum4 and AssetTypeCreditAlphaNum12 subclasses.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/fundamentals-and-concepts/stellar-data-structures/assets"
 *     target="_blank">Assets</a>
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
public abstract class AssetTypeCreditAlphaNum extends Asset {
  /** Asset code */
  @NonNull protected final String code;

  /** Asset issuer */
  @NonNull protected final String issuer;

  @Override
  public String toString() {
    return getCode() + ":" + getIssuer();
  }
}
