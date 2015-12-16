package org.stellar.sdk.effects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents account_created effect response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class AccountCreatedEffect extends Effect {
  @SerializedName("starting_balance")
  protected final String startingBalance;

  AccountCreatedEffect(String startingBalance) {
    this.startingBalance = startingBalance;
  }

  public String getStartingBalance() {
    return startingBalance;
  }
}
