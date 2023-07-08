package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Predicate;

/**
 * Represents claimable_balance_claimant_created effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class ClaimableBalanceClaimantCreatedEffectResponse extends EffectResponse {
  @SerializedName("asset")
  private final String assetString;

  @SerializedName("amount")
  protected final String amount;

  @SerializedName("balance_id")
  protected final String balanceId;

  @SerializedName("predicate")
  protected final Predicate predicate;

  public ClaimableBalanceClaimantCreatedEffectResponse(
      String assetString, String amount, String balanceId, Predicate predicate) {
    this.assetString = assetString;
    this.amount = amount;
    this.balanceId = balanceId;
    this.predicate = predicate;
  }

  public String getAssetString() {
    return assetString;
  }

  public Asset getAsset() {
    return Asset.create(assetString);
  }

  public String getAmount() {
    return amount;
  }

  public String getBalanceId() {
    return balanceId;
  }

  public Predicate getPredicate() {
    return predicate;
  }
}
