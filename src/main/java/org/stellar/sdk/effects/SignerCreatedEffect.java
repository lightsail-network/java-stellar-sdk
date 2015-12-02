package org.stellar.sdk.effects;

/**
 * Represents signer_created effect response.
 */
public class SignerCreatedEffect extends SignerEffect {
  SignerCreatedEffect(Integer weight, String publicKey) {
    super(weight, publicKey);
  }
}
