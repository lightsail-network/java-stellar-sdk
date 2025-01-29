package network.lightsail;

import static org.stellar.sdk.responses.sorobanrpc.GetLedgerEntriesResponse.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.SorobanServer;
import org.stellar.sdk.responses.sorobanrpc.GetLedgerEntriesResponse;
import org.stellar.sdk.xdr.LedgerEntry;
import org.stellar.sdk.xdr.LedgerEntryType;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.LedgerKey.LedgerKeyAccount;

public class GetLedgerEntries {
  public static void main(String[] args) throws IOException {
    String rpcServerUrl = "https://soroban-testnet.stellar.org:443";
    GetLedgerEntriesResponse response;
    try (SorobanServer sorobanServer = new SorobanServer(rpcServerUrl)) {
      KeyPair keyPair =
          KeyPair.fromAccountId("GDJLBYYKMCXNVVNABOE66NYXQGIA5AC5D223Z2KF6ZEYK4UBCA7FKLTG");

      // Create ledger keys
      List<LedgerKey> ledgerKeys =
          Collections.singletonList(
              LedgerKey.builder()
                  .account(LedgerKeyAccount.builder().accountID(keyPair.getXdrAccountId()).build())
                  .discriminant(LedgerEntryType.ACCOUNT)
                  .build());

      // Get ledger entries
      response = sorobanServer.getLedgerEntries(ledgerKeys);
    }

    // Print ledger entries
    for (LedgerEntryResult result : response.getEntries()) {
      LedgerEntry.LedgerEntryData ledgerEntryData =
          LedgerEntry.LedgerEntryData.fromXdrBase64(result.getXdr());
      System.out.println(ledgerEntryData);
    }
  }
}
