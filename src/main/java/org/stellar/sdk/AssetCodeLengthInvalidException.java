package org.stellar.sdk;

/**
 * Indicates that asset code is not valid for a specified asset class
 *
 * @see AssetTypeCreditAlphaNum4
 * @see AssetTypeCreditAlphaNum12
 */
public class AssetCodeLengthInvalidException extends RuntimeException {
  public AssetCodeLengthInvalidException() {
    super();
  }

  public AssetCodeLengthInvalidException(String message) {
    super(message);
  }
}
