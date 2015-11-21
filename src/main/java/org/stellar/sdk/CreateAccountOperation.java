package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;

import org.stellar.base.Keypair;

public class CreateAccountOperation extends Operation {
  @SerializedName("account")
  protected final Keypair account;
  @SerializedName("funder")
  protected final Keypair funder;
  @SerializedName("starting_balance")
  protected final String startingBalance;

  CreateAccountOperation(Keypair funder, String startingBalance, Keypair account) {
    this.funder = funder;
    this.startingBalance = startingBalance;
    this.account = account;
  }

  public Keypair getAccount() {
    return account;
  }

  public String getStartingBalance() {
    return startingBalance;
  }

  public Keypair getFunder() {
    return funder;
  }
}
