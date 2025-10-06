package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents liquidity_pool_revoked effect response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/resources/effects"
 *     target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class LiquidityPoolRevokedEffectResponse extends EffectResponse {
  @SerializedName("liquidity_pool")
  LiquidityPool liquidityPool;

  @SerializedName("reserves_revoked")
  List<LiquidityPoolClaimableAssetAmount> reservesRevoked;

  @SerializedName("shares_revoked")
  String sharesRevoked;
}
