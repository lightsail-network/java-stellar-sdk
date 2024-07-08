package org.stellar.sdk.responses.effects;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents trustline_flags_updated effect response.
 *
 * @see <a href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/effects"
 *     target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class TrustlineFlagsUpdatedEffectResponse extends EffectResponse {
  @SerializedName("trustor")
  String trustor;

  @SerializedName("asset_type")
  String assetType;

  @SerializedName("asset_code")
  String assetCode;

  @SerializedName("asset_issuer")
  String assetIssuer;

  @SerializedName("authorized_flag")
  Boolean authorizedFlag;

  // A typo in Go implementation
  @SerializedName("authorized_to_maintain_liabilites_flag")
  Boolean authorizedToMaintainLiabilitiesFlag;

  @SerializedName("clawback_enabled_flag")
  Boolean clawbackEnabledFlag;

  public Asset getAsset() {
    return create(assetType, assetCode, assetIssuer);
  }
}
