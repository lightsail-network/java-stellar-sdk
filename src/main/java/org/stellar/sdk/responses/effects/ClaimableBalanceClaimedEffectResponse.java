package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents claimable_balance_claimed effect response.
 *
 * @see <a href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/effects"
 *     target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ClaimableBalanceClaimedEffectResponse extends EffectResponse {
  @SerializedName("asset")
  String assetString;

  @SerializedName("amount")
  String amount;

  @SerializedName("balance_id")
  String balanceId;

  // TODO
  public Asset getAsset() {
    return Asset.create(assetString);
  }
}
