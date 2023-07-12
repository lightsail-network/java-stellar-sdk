package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

abstract class SignerEffectResponse extends EffectResponse {
  @SerializedName("weight")
  protected final Integer weight;

  @SerializedName("public_key")
  protected final String publicKey;

  public SignerEffectResponse(Integer weight, String publicKey) {
    this.weight = weight;
    this.publicKey = publicKey;
  }

  public Integer getWeight() {
    return weight;
  }

  public String getPublicKey() {
    return publicKey;
  }
}
