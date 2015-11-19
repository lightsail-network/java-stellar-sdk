package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;

public class AccountsPage extends Page {
  @SerializedName("records")
  private Account[] accounts;

  AccountsPage() {}

  public Account[] getRecords() {
    return accounts;
  }
}
