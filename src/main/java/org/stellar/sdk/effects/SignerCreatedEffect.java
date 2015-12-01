package org.stellar.sdk.effects;

public class SignerCreatedEffect extends SignerEffect {
  SignerCreatedEffect(Integer weight, String publicKey) {
    super(weight, publicKey);
  }
}
