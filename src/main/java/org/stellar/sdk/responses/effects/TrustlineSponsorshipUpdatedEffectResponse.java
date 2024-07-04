package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents trustline_sponsorship_updated effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class TrustlineSponsorshipUpdatedEffectResponse extends EffectResponse {
  @SerializedName("asset")
  String assetString;

  @SerializedName("former_sponsor")
  String formerSponsor;

  @SerializedName("new_sponsor")
  String newSponsor;

  // TODO: fix me
  public Asset getAsset() {
    return Asset.create(assetString);
  }
}
