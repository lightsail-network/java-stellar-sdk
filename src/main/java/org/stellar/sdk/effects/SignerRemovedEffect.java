package org.stellar.sdk.effects;

/**
 * Represents signer_removed effect response.
 */
public class SignerRemovedEffect extends SignerEffect {
  SignerRemovedEffect(Integer weight, String publicKey) {
    super(weight, publicKey);
  }
}
