package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents claimable_balance_created effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ClaimableBalanceCreatedEffectResponse extends EffectResponse {
  @SerializedName("asset")
  String assetString;

  @SerializedName("amount")
  String amount;

  @SerializedName("balance_id")
  String balanceId;

  public Asset getAsset() {
    return Asset.create(assetString);
  }
}
