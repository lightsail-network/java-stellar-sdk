package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents account_sponsorship_removed effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class AccountSponsorshipRemovedEffectResponse extends EffectResponse {
  @SerializedName("former_sponsor")
  private final String formerSponsor;

  public AccountSponsorshipRemovedEffectResponse(String formerSponsor) {
    this.formerSponsor = formerSponsor;
  }

  public String getFormerSponsor() {
    return formerSponsor;
  }
}
