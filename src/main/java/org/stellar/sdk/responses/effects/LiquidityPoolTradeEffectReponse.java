package org.stellar.sdk.responses.effects;

import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.LiquidityPoolID;

import com.google.gson.annotations.SerializedName;

/**
 * Represents liquidity_pool_trade effect response.
 *
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class LiquidityPoolTradeEffectReponse extends EffectResponse {
  @SerializedName("liquidity_pool")
  protected final LiquidityPoolID liquidityPoolID;
  @SerializedName("sold")
  protected final AssetAmount sold;
  @SerializedName("bought")
  protected final AssetAmount bought;

  public LiquidityPoolTradeEffectReponse(LiquidityPoolID liquidityPoolID, AssetAmount sold, AssetAmount bought) {
    this.liquidityPoolID = liquidityPoolID;
    this.sold = sold;
    this.bought = bought;
  }

  public LiquidityPoolID getLiquidityPoolID() {
    return liquidityPoolID;
  }

  public AssetAmount getSold() {
    return sold;
  }

  public AssetAmount getBought() {
    return bought;
  }
}
