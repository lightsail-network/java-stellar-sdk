package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents trustline_sponsorship_created effect response.
 *
 * @see <a href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/effects"
 *     target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class TrustlineSponsorshipCreatedEffectResponse extends EffectResponse {
  @SerializedName("asset_type")
  String assetType;

  @SerializedName("asset")
  Asset asset;

  @SerializedName("liquidity_pool_id")
  String liquidityPoolId;

  @SerializedName("sponsor")
  String sponsor;
}
