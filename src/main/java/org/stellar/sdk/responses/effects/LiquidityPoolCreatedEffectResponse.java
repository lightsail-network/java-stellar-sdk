package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents liquidity_pool_created effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class LiquidityPoolCreatedEffectResponse extends EffectResponse {
  @SerializedName("liquidity_pool")
  protected final LiquidityPool liquidityPool;

  public LiquidityPoolCreatedEffectResponse(LiquidityPool liquidityPool) {
    this.liquidityPool = liquidityPool;
  }

  public LiquidityPool getLiquidityPool() {
    return liquidityPool;
  }
}
