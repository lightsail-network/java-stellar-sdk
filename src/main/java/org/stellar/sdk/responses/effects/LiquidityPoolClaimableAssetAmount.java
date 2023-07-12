package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;

/**
 * Represents liquidity_pool_claimable_asset_amount used in effect responses.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class LiquidityPoolClaimableAssetAmount {
  @SerializedName("asset")
  protected final Asset asset;

  @SerializedName("amount")
  protected final String amount;

  @SerializedName("claimable_balance_id")
  protected final String claimableBalanceID;

  public LiquidityPoolClaimableAssetAmount(Asset asset, String amount, String claimableBalanceID) {
    this.asset = asset;
    this.amount = amount;
    this.claimableBalanceID = claimableBalanceID;
  }

  public Asset getAsset() {
    return asset;
  }

  public String getAmount() {
    return amount;
  }

  public String getClaimableBalanceID() {
    return claimableBalanceID;
  }
}
