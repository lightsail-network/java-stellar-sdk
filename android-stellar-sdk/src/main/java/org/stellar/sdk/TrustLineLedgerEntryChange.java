package org.stellar.sdk;


import org.stellar.sdk.xdr.Uint32;


public class TrustLineLedgerEntryChange extends LedgerEntryChange {
  private KeyPair accountID;
  private Asset asset;
  private String balance;
  private String limit;
  private Uint32 flags;

  TrustLineLedgerEntryChange() {
  }

  public KeyPair getAccount() {
    return this.accountID;
  }

  public Asset getAsset() {
    return this.asset;
  }

  public String getBalance() {
    return this.balance;
  }

  public String getLimit() {
    return this.limit;
  }

  public static TrustLineLedgerEntryChange fromXdr(org.stellar.sdk.xdr.TrustLineEntry xdr) {
    TrustLineLedgerEntryChange entry = new TrustLineLedgerEntryChange();
    entry.accountID = KeyPair.fromXdrPublicKey(xdr.getAccountID().getAccountID());
    entry.asset = Asset.fromXdr(xdr.getAsset());
    entry.balance = Operation.fromXdrAmount(xdr.getBalance().getInt64());
    entry.limit = Operation.fromXdrAmount(xdr.getLimit().getInt64());
    return entry;
  }

}
