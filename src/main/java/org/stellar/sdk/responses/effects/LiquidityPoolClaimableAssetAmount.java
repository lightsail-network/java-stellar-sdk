package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents liquidity_pool_claimable_asset_amount used in effect responses.
 *
 * @see <a href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/effects"
 *     target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
public class LiquidityPoolClaimableAssetAmount {
  @SerializedName("asset")
  Asset asset;

  @SerializedName("amount")
  String amount;

  @SerializedName("claimable_balance_id")
  String claimableBalanceID;
}
