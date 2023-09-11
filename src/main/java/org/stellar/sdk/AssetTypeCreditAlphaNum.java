package org.stellar.sdk;

import java.util.Objects;
import lombok.NonNull;

/**
 * Base class for AssetTypeCreditAlphaNum4 and AssetTypeCreditAlphaNum12 subclasses.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/assets/" target="_blank">Assets</a>
 */
public abstract class AssetTypeCreditAlphaNum extends Asset {
  protected final String mCode;
  protected final String mIssuer;

  public AssetTypeCreditAlphaNum(@NonNull String code, @NonNull String issuer) {
    mCode = code;
    mIssuer = issuer;
  }

  /** Returns asset code */
  public String getCode() {
    return mCode;
  }

  /** Returns asset issuer */
  public String getIssuer() {
    return mIssuer;
  }

  @Override
  public String toString() {
    return getCode() + ":" + getIssuer();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.mCode, this.mIssuer);
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || !this.getClass().equals(object.getClass())) {
      return false;
    }

    AssetTypeCreditAlphaNum o = (AssetTypeCreditAlphaNum) object;

    return this.getCode().equals(o.getCode()) && this.getIssuer().equals(o.getIssuer());
  }
}
