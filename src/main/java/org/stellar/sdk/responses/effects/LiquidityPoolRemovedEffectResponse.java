package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.LiquidityPoolID;

/**
 * Represents liquidity_pool_removed effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class LiquidityPoolRemovedEffectResponse extends EffectResponse {
  @SerializedName("liquidity_pool_id")
  LiquidityPoolID liquidityPoolID;
}
