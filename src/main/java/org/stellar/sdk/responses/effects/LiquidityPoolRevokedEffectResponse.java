package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents liquidity_pool_revoked effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class LiquidityPoolRevokedEffectResponse extends EffectResponse {
  @SerializedName("liquidity_pool")
  protected final LiquidityPool liquidityPool;

  @SerializedName("reserves_revoked")
  protected final LiquidityPoolClaimableAssetAmount reservesRevoked;

  @SerializedName("shares_revoked")
  protected final String sharesRevoked;

  public LiquidityPoolRevokedEffectResponse(
      LiquidityPool liquidityPool,
      LiquidityPoolClaimableAssetAmount reservesRevoked,
      String sharesRevoked) {
    this.liquidityPool = liquidityPool;
    this.reservesRevoked = reservesRevoked;
    this.sharesRevoked = sharesRevoked;
  }

  public LiquidityPool getLiquidityPool() {
    return liquidityPool;
  }

  public LiquidityPoolClaimableAssetAmount getReservesRevoked() {
    return reservesRevoked;
  }

  public String getSharesRevoked() {
    return sharesRevoked;
  }
}
