package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;

public class LedgersPage extends Page {
  @SerializedName("records")
  private Ledger[] ledgers;

  LedgersPage() {}

  public Ledger[] getRecords() {
    return ledgers;
  }
}
