package org.stellar.sdk.effects;

public class SignerRemovedEffect extends SignerEffect {
  public SignerRemovedEffect(Integer weight, String publicKey) {
    super(weight, publicKey);
  }
}
