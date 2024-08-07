package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.responses.AssetAmount;

/**
 * Represents liquidity_pool_withdrew effect response.
 *
 * @see <a href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/effects"
 *     target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class LiquidityPoolWithdrewEffectResponse extends EffectResponse {
  @SerializedName("liquidity_pool")
  LiquidityPool liquidityPool;

  @SerializedName("reserves_received")
  List<AssetAmount> reservesReceived;

  @SerializedName("shares_redeemed")
  String sharesRedeemed;
}
