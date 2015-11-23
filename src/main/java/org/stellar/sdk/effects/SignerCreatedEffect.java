package org.stellar.sdk.effects;

public class SignerCreatedEffect extends SignerEffect {
  public SignerCreatedEffect(Integer weight, String publicKey) {
    super(weight, publicKey);
  }
}
