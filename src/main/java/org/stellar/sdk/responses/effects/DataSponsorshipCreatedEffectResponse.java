package org.stellar.sdk.responses.effects;


import com.google.gson.annotations.SerializedName;

/**
 * Represents data_sponsorship_created effect response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class DataSponsorshipCreatedEffectResponse extends EffectResponse {
  @SerializedName("sponsor")
  private final String sponsor;

  @SerializedName("data_name")
  private final String dataName;

  public DataSponsorshipCreatedEffectResponse(String dataName, String sponsor) {
    this.dataName = dataName;
    this.sponsor = sponsor;
  }

  public String getSponsor() {
    return sponsor;
  }

  public String getDataName() {
    return dataName;
  }
}
