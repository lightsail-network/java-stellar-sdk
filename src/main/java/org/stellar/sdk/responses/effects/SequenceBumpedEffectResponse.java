package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents sequence_bumped effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class SequenceBumpedEffectResponse extends EffectResponse {
  @SerializedName("new_seq")
  protected final Long newSequence;

  public SequenceBumpedEffectResponse(Long newSequence) {
    this.newSequence = newSequence;
  }

  public Long getNewSequence() {
    return newSequence;
  }
}
