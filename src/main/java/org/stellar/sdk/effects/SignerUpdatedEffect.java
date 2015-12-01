package org.stellar.sdk.effects;

public class SignerUpdatedEffect extends SignerEffect {
  SignerUpdatedEffect(Integer weight, String publicKey) {
    super(weight, publicKey);
  }
}
