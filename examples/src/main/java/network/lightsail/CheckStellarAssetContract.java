package network.lightsail;

import java.io.IOException;
import java.util.List;
import org.stellar.sdk.*;
import org.stellar.sdk.responses.sorobanrpc.GetLedgerEntriesResponse;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.ContractDataDurability;
import org.stellar.sdk.xdr.LedgerEntry.LedgerEntryData;
import org.stellar.sdk.xdr.LedgerEntryType;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.LedgerKey.LedgerKeyContractData;

/**
 * This example shows how to check the existence of the SAC (Stellar Asset Contract) of a class asset.
 */
public class CheckStellarAssetContract {
  public static void main(String[] args) throws IOException {
    String rpcServerUrl = "https://soroban-testnet.stellar.org:443";
    Asset assetNative = Asset.createNativeAsset();
    Asset assetUSDC =
        Asset.createNonNativeAsset(
            "USDC", "GDQOE23CFSUMSVQK4Y5JHPPYK73VYCNHZHA7ENKCV37P6SUEO6XQBKPP");
    Asset assetUnknown =
        Asset.createNonNativeAsset(
            "UNKN", "GDQOE23CFSUMSVQK4Y5JHPPYK73VYCNHZHA7ENKCV37P6SUEO6XQBKPP");

    try (SorobanServer sorobanServer = new SorobanServer(rpcServerUrl)) {
      System.out.println(
          "The SAC ID of the asset ["
              + assetNative
              + "] is "
              + getContractData(sorobanServer, assetNative));
      System.out.println(
          "The SAC ID of the asset ["
              + assetUSDC
              + "] is "
              + getContractData(sorobanServer, assetNative));
      System.out.println(
          "The SAC ID of the asset ["
              + assetUSDC
              + "] is unknown ("
              + getContractData(sorobanServer, assetUnknown)
              + ")");
    }
  }

  public static String getContractData(SorobanServer sorobanServer, Asset asset)
      throws IOException {
    String contractId = asset.getContractId(Network.TESTNET);
    List<LedgerKey> ledgerKeys =
        List.of(
            LedgerKey.builder()
                .discriminant(LedgerEntryType.CONTRACT_DATA)
                .contractData(
                    LedgerKeyContractData.builder()
                        .contract(new Address(contractId).toSCAddress())
                        .key(Scv.toLedgerKeyContractInstance())
                        .durability(ContractDataDurability.PERSISTENT)
                        .build())
                .build());
    GetLedgerEntriesResponse response = sorobanServer.getLedgerEntries(ledgerKeys);
    if (response.getEntries() != null && !response.getEntries().isEmpty()) {
      LedgerEntryData entryData =
          LedgerEntryData.fromXdrBase64(response.getEntries().get(0).getXdr());
      if (entryData.getContractData() == null) return null;
      return entryData.getContractData().getContract().getContractId().toXdrBase64();
    } else {
      return null;
    }
  }
}
