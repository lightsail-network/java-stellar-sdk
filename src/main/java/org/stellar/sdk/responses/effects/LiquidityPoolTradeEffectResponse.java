package org.stellar.sdk.responses.effects;

import org.stellar.sdk.AssetAmount;

import com.google.gson.annotations.SerializedName;

/**
 * Represents liquidity_pool_trade effect response.
 *
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class LiquidityPoolTradeEffectResponse extends EffectResponse {
  @SerializedName("liquidity_pool")
  protected final LiquidityPool liquidityPool;
  @SerializedName("sold")
  protected final AssetAmount sold;
  @SerializedName("bought")
  protected final AssetAmount bought;

  public LiquidityPoolTradeEffectResponse(LiquidityPool liquidityPool, AssetAmount sold, AssetAmount bought) {
    this.liquidityPool = liquidityPool;
    this.sold = sold;
    this.bought = bought;
  }

  public LiquidityPool getLiquidityPool() {
    return liquidityPool;
  }

  public AssetAmount getSold() {
    return sold;
  }

  public AssetAmount getBought() {
    return bought;
  }
}
