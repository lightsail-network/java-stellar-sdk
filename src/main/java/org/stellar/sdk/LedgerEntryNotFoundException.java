package org.stellar.sdk;

public class LedgerEntryNotFoundException extends Exception {
  private final String key;

  public LedgerEntryNotFoundException(String key) {
    super("Ledger entry not found: " + key);
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
