package org.stellar.sdk.effects;

/**
 * Represents signed_updated effect response.
 */
public class SignerUpdatedEffect extends SignerEffect {
  SignerUpdatedEffect(Integer weight, String publicKey) {
    super(weight, publicKey);
  }
}
