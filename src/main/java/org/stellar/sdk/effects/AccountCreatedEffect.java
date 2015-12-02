package org.stellar.sdk.effects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents account_created effect response.
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
