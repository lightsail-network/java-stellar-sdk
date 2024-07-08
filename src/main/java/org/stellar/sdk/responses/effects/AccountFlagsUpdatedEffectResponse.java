package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents account_flags_updated effect response.
 *
 * @see <a href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/effects"
 *     target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class AccountFlagsUpdatedEffectResponse extends EffectResponse {
  @SerializedName("auth_required_flag")
  Boolean authRequiredFlag;

  @SerializedName("auth_revokable_flag")
  Boolean authRevokableFlag;
}
