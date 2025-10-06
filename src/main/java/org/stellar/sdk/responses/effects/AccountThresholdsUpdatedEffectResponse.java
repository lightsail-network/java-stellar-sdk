package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents account_thresholds_updated effect response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/resources/effects"
 *     target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class AccountThresholdsUpdatedEffectResponse extends EffectResponse {
  @SerializedName("low_threshold")
  Integer lowThreshold;

  @SerializedName("med_threshold")
  Integer medThreshold;

  @SerializedName("high_threshold")
  Integer highThreshold;
}
