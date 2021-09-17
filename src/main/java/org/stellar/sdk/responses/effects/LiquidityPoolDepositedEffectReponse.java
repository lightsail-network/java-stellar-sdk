package org.stellar.sdk.responses.effects;

import org.stellar.sdk.AssetAmount;

import com.google.gson.annotations.SerializedName;

/**
 * Represents liquidity_pool_deposited effect response.
 *
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class LiquidityPoolDepositedEffectReponse extends EffectResponse {
  @SerializedName("liquidity_pool")
  protected final LiquidityPool liquidityPool;
  @SerializedName("reserves_deposited")
  protected final AssetAmount[] reservesDeposited;
  @SerializedName("shares_received")
  protected final String sharesReceived;

  public LiquidityPoolDepositedEffectReponse(LiquidityPool liquidityPool, AssetAmount[] reservesDeposited, String sharesReceived) {
    this.liquidityPool = liquidityPool;
    this.reservesDeposited = reservesDeposited;
    this.sharesReceived = sharesReceived;
  }

  public LiquidityPool getLiquidityPool() {
    return liquidityPool;
  }

  public AssetAmount[] getReservesDeposited() {
    return reservesDeposited;
  }

  public String getSharesReceived() {
    return sharesReceived;
  }
}
