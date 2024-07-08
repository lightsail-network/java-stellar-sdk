package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.AssetAmount;

/**
 * Represents liquidity_pool_deposited effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class LiquidityPoolDepositedEffectResponse extends EffectResponse {
  @SerializedName("liquidity_pool")
  LiquidityPool liquidityPool;

  @SerializedName("reserves_deposited")
  List<AssetAmount> reservesDeposited;

  @SerializedName("shares_received")
  String sharesReceived;
}
