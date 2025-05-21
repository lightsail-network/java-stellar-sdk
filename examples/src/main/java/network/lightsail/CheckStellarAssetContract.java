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
 * This example shows how to check the existence of the SAC (Stellar Asset Contract) of a class
 * asset.
 */
public class CheckStellarAssetContract {
  public static void main(String[] args) throws IOException {
    String rpcServerUrl = "https://soroban-testnet.stellar.org:443";
    Network network = Network.TESTNET;
    Asset assetNative = Asset.createNativeAsset();
    Asset assetUSDC =
        Asset.createNonNativeAsset(
            "USDC", "GDQOE23CFSUMSVQK4Y5JHPPYK73VYCNHZHA7ENKCV37P6SUEO6XQBKPP");
    Asset assetUnknown =
        Asset.createNonNativeAsset(
            "UNKN", "GDQOE23CFSUMSVQK4Y5JHPPYK73VYCNHZHA7ENKCV37P6SUEO6XQBKPP");

    try (SorobanServer sorobanServer = new SorobanServer(rpcServerUrl)) {
      for (Asset asset : List.of(assetNative, assetUSDC, assetUnknown)) {
        String contractId = asset.getContractId(network);
        System.out.printf(
            "The contract id for %s is %s, it is %s\n",
            asset,
            contractId,
            isContractDeployed(sorobanServer, contractId) ? "deployed" : "not deployed");
      }
    }
  }

  public static boolean isContractDeployed(SorobanServer sorobanServer, String contractId) {
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
    return response.getEntries() != null && !response.getEntries().isEmpty();
  }
}
