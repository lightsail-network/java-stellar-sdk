package org.stellar.sdk.exception;

import org.stellar.sdk.AssetTypeCreditAlphaNum12;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;

/**
 * Indicates that asset code is not valid for a specified asset class
 *
 * @see AssetTypeCreditAlphaNum4
 * @see AssetTypeCreditAlphaNum12
 */
public class AssetCodeLengthInvalidException extends IllegalArgumentException {
  public AssetCodeLengthInvalidException() {
    super();
  }

  public AssetCodeLengthInvalidException(String message) {
    super(message);
  }
}
