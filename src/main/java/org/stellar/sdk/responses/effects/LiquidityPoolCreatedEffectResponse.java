package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents liquidity_pool_created effect response.
 *
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class LiquidityPoolCreatedEffectResponse extends EffectResponse {
  @SerializedName("liquidity_pool")
  protected final LiquidityPool liquidityPool;

  public LiquidityPoolCreatedEffectResponse(LiquidityPool liquidityPool) {
    this.liquidityPool = liquidityPool;
  }

  public LiquidityPoolID getLiquidityPoolID() {
    return liquidityPool;
  }
}
