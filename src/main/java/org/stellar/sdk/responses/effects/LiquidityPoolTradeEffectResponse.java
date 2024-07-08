package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.AssetAmount;

/**
 * Represents liquidity_pool_trade effect response.
 *
 * @see <a href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/effects"
 *     target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class LiquidityPoolTradeEffectResponse extends EffectResponse {
  @SerializedName("liquidity_pool")
  LiquidityPool liquidityPool;

  @SerializedName("sold")
  AssetAmount sold;

  @SerializedName("bought")
  AssetAmount bought;
}
