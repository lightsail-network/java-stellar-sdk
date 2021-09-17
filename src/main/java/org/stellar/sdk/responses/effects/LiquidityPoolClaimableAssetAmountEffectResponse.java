package org.stellar.sdk.responses.effects;

import org.stellar.sdk.Asset;

import com.google.gson.annotations.SerializedName;

/**
 * Represents liquidity_pool_claimable_asset_amount effect response.
 *
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class LiquidityPoolClaimableAssetAmountEffectResponse extends EffectResponse {
  @SerializedName("asset")
  protected final Asset asset;
  @SerializedName("amount")
  protected final String amount;
  @SerializedName("claimable_balance_id")
  protected final String claimableBalanceID;

  public LiquidityPoolClaimableAssetAmountEffectResponse(Asset asset, String amount, String claimableBalanceID) {
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
