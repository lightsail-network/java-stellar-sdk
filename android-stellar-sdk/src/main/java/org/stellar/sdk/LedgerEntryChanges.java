package org.stellar.sdk;


import java.util.ArrayList;
import java.util.List;

public class LedgerEntryChanges {

  private LedgerEntryChange[] ledgerEntryUpdates;
  private LedgerEntryChange[] ledgerEntryStates;

  public LedgerEntryChange[] getLedgerEntryStates() {
    return ledgerEntryStates;
  }

  public LedgerEntryChange[] getLedgerEntryUpdates() {
    return this.ledgerEntryUpdates;
  }

  public static LedgerEntryChanges fromXdr(org.stellar.sdk.xdr.LedgerEntryChanges xdr) {
    LedgerEntryChanges ledgerEntryChanges = new LedgerEntryChanges();
    List<LedgerEntryChange> updates = new ArrayList<>();
    List<LedgerEntryChange> states = new ArrayList<>();
    for (org.stellar.sdk.xdr.LedgerEntryChange ledgerEntryChange : xdr.getLedgerEntryChanges()) {
      switch (ledgerEntryChange.getDiscriminant()) {
        case LEDGER_ENTRY_CREATED:
          break;
        case LEDGER_ENTRY_UPDATED:
          LedgerEntryChange entryChange = LedgerEntryChange.fromXdr(ledgerEntryChange.getUpdated());
          if (entryChange != null) {
            updates.add(entryChange);
          }
          break;
        case LEDGER_ENTRY_REMOVED:
          break;
        case LEDGER_ENTRY_STATE:
          LedgerEntryChange stateChange = LedgerEntryChange.fromXdr(ledgerEntryChange.getState());
          if (stateChange != null) {
            states.add(stateChange);
          }
          break;
      }
    }
    ledgerEntryChanges.ledgerEntryUpdates = new LedgerEntryChange[updates.size()];
    updates.toArray(ledgerEntryChanges.ledgerEntryUpdates);
    ledgerEntryChanges.ledgerEntryStates = new LedgerEntryChange[updates.size()];
    states.toArray(ledgerEntryChanges.ledgerEntryStates);
    return ledgerEntryChanges;
  }

}
