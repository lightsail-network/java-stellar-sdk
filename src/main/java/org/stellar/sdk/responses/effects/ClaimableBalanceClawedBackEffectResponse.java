package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents claimable_balance_clawed_back effect response.
 *
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class ClaimableBalanceClawedBackEffectResponse extends EffectResponse {
  @SerializedName("balance_id")
  protected final String balanceId;

  public ClaimableBalanceClawedBackEffectResponse(String balanceId) {
    this.balanceId = balanceId;
  }

  public String getBalanceId() {
    return balanceId;
  }

}
