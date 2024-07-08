package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Getter
abstract class SignerEffectResponse extends EffectResponse {
  @SerializedName("weight")
  private final Integer weight;

  @SerializedName("public_key")
  private final String publicKey;

  // Note: Key       string `json:"key"` in Go
}
