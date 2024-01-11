package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents claimable_balance_sponsorship_created effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ClaimableBalanceSponsorshipCreatedEffectResponse extends EffectResponse {
  @SerializedName("sponsor")
  String sponsor;

  @SerializedName("balance_id")
  String balanceId;
}
