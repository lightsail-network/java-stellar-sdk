package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;

/**
 * Represents trustline_sponsorship_updated effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class TrustlineSponsorshipUpdatedEffectResponse extends EffectResponse {
  @SerializedName("asset")
  private final String assetString;

  @SerializedName("former_sponsor")
  protected final String formerSponsor;

  @SerializedName("new_sponsor")
  protected final String newSponsor;

  public TrustlineSponsorshipUpdatedEffectResponse(
      String assetString, String formerSponsor, String newSponsor) {
    this.assetString = assetString;
    this.formerSponsor = formerSponsor;
    this.newSponsor = newSponsor;
  }

  public String getAssetString() {
    return assetString;
  }

  public Asset getAsset() {
    return Asset.create(assetString);
  }

  public String getFormerSponsor() {
    return formerSponsor;
  }

  public String getNewSponsor() {
    return newSponsor;
  }
}
