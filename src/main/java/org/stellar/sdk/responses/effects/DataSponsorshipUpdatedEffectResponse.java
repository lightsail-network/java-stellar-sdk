package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents data_sponsorship_updated effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class DataSponsorshipUpdatedEffectResponse extends EffectResponse {
  @SerializedName("former_sponsor")
  private final String formerSponsor;

  @SerializedName("new_sponsor")
  private final String newSponsor;

  @SerializedName("data_name")
  private final String dataName;

  public DataSponsorshipUpdatedEffectResponse(
      String dataName, String formerSponsor, String newSponsor) {
    this.dataName = dataName;
    this.formerSponsor = formerSponsor;
    this.newSponsor = newSponsor;
  }

  public String getFormerSponsor() {
    return formerSponsor;
  }

  public String getNewSponsor() {
    return newSponsor;
  }

  public String getDataName() {
    return dataName;
  }
}
