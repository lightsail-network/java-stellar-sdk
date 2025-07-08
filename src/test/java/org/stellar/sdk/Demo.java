package org.stellar.sdk;

import org.stellar.sdk.responses.sorobanrpc.GetSACBalanceResponse;

public class Demo {
  public static void main(String[] args) {
    SorobanServer server = new SorobanServer("https://mainnet.sorobanrpc.com");
    Asset asset =
        Asset.createNonNativeAsset(
            "USDC", "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN");
    GetSACBalanceResponse resp =
        server.getSACBalance(
            "CDOAW6D7NXAPOCO7TFAWZNJHK62E3IYRGNRVX3VOXNKNVOXCLLPJXQCF", asset, Network.PUBLIC);
    System.out.println(resp);
  }
}
