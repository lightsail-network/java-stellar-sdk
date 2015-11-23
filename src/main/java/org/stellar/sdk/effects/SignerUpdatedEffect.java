package org.stellar.sdk.effects;

public class SignerUpdatedEffect extends SignerEffect {
  public SignerUpdatedEffect(Integer weight, String publicKey) {
    super(weight, publicKey);
  }
}
