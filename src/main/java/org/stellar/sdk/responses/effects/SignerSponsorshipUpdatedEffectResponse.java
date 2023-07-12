package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents signer_sponsorship_updated effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class SignerSponsorshipUpdatedEffectResponse extends EffectResponse {
  @SerializedName("former_sponsor")
  private final String formerSponsor;

  @SerializedName("new_sponsor")
  private final String newSponsor;

  @SerializedName("signer")
  private final String signer;

  public SignerSponsorshipUpdatedEffectResponse(
      String signer, String formerSponsor, String newSponsor) {
    this.signer = signer;
    this.formerSponsor = formerSponsor;
    this.newSponsor = newSponsor;
  }

  public String getSigner() {
    return signer;
  }

  public String getFormerSponsor() {
    return formerSponsor;
  }

  public String getNewSponsor() {
    return newSponsor;
  }
}
