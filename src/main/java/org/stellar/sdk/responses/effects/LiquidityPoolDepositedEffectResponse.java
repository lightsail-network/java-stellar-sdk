package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.AssetAmount;

/**
 * Represents liquidity_pool_deposited effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class LiquidityPoolDepositedEffectResponse extends EffectResponse {
  @SerializedName("liquidity_pool")
  protected final LiquidityPool liquidityPool;

  @SerializedName("reserves_deposited")
  protected final AssetAmount[] reservesDeposited;

  @SerializedName("shares_received")
  protected final String sharesReceived;

  public LiquidityPoolDepositedEffectResponse(
      LiquidityPool liquidityPool, AssetAmount[] reservesDeposited, String sharesReceived) {
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
