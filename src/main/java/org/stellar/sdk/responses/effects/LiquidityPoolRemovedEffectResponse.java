package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.LiquidityPoolID;

/**
 * Represents liquidity_pool_removed effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class LiquidityPoolRemovedEffectResponse extends EffectResponse {
  @SerializedName("liquidity_pool_id")
  protected final LiquidityPoolID liquidityPoolID;

  public LiquidityPoolRemovedEffectResponse(LiquidityPoolID liquidityPoolID) {
    this.liquidityPoolID = liquidityPoolID;
  }

  public LiquidityPoolID getLiquidityPoolID() {
    return liquidityPoolID;
  }
}
