package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@ToString(callSuper = true)
abstract class TrustlineAuthorizationResponse extends EffectResponse {
  @SerializedName("trustor")
  private final String trustor;

  @SerializedName("asset_type")
  private final String assetType;

  @SerializedName("asset_code")
  private final String assetCode;
}
