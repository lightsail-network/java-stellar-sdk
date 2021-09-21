package org.stellar.sdk.responses.effects;

import org.stellar.sdk.AssetAmount;

import com.google.gson.annotations.SerializedName;

/**
 * Represents liquidity_pool_withdrew effect response.
 *
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class LiquidityPoolWithdrewEffectResponse extends EffectResponse {
  @SerializedName("liquidity_pool")
  protected final LiquidityPool liquidityPool;
  @SerializedName("reserves_received")
  protected final AssetAmount[] reservesReceived;
  @SerializedName("shares_redeemed")
  protected final String sharesRedeemed;

  public LiquidityPoolWithdrewEffectResponse(LiquidityPool liquidityPool, AssetAmount[] reservesReceived, String sharesRedeemed) {
    this.liquidityPool = liquidityPool;
    this.reservesReceived = reservesReceived;
    this.sharesRedeemed = sharesRedeemed;
  }

  public LiquidityPool getLiquidityPool() {
    return liquidityPool;
  }

  public AssetAmount[] getReservesReceived() {
    return reservesReceived;
  }

  public String getSharesRedeemed() {
    return sharesRedeemed;
  }
}
