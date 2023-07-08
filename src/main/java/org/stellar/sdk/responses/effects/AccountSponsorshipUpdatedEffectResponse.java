package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents account_sponsorship_updated effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class AccountSponsorshipUpdatedEffectResponse extends EffectResponse {
  @SerializedName("former_sponsor")
  private final String formerSponsor;

  @SerializedName("new_sponsor")
  private final String newSponsor;

  public AccountSponsorshipUpdatedEffectResponse(String formerSponsor, String newSponsor) {
    this.formerSponsor = formerSponsor;
    this.newSponsor = newSponsor;
  }

  public String getFormerSponsor() {
    return formerSponsor;
  }

  public String getNewSponsor() {
    return newSponsor;
  }
}
