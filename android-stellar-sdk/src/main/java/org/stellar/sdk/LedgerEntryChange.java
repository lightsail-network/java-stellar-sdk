package org.stellar.sdk;


import org.stellar.sdk.xdr.LedgerEntry;


public class LedgerEntryChange {
  LedgerEntryChange() {
  }

  private Long lastModifiedLedgerSequence;

  public Long getLastModifiedLedgerSequence() {
    return this.lastModifiedLedgerSequence;
  }

  public static LedgerEntryChange fromXdr(LedgerEntry xdr) {
    LedgerEntryChange entryChange = null;
    switch (xdr.getData().getDiscriminant()) {
      case ACCOUNT:
        break;
      case TRUSTLINE:
        entryChange = TrustLineLedgerEntryChange.fromXdr(xdr.getData().getTrustLine());
        entryChange.lastModifiedLedgerSequence = xdr.getLastModifiedLedgerSeq().getUint32().longValue();
        break;
      case OFFER:
        break;
      case DATA:
        break;
    }
    return entryChange;
  }
}
