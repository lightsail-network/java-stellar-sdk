package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class AssetResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/asset.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AssetResponse assetResponse = GsonSingleton.getInstance().fromJson(json, AssetResponse.class);
    assertEquals(assetResponse.getAssetType(), "credit_alphanum4");
    assertEquals(assetResponse.getAssetCode(), "USDC");
    assertEquals(
        assetResponse.getAssetIssuer(), "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN");
    assertEquals(
        assetResponse.getPagingToken(),
        "USDC_GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN_credit_alphanum4");
    assertEquals(
        assetResponse.getContractId(), "CCW67TSZV3SSS2HXMBQ5JFGCKJNXKZM7UQUWUZPUTHXSTZLEO7SJMI75");
    assertEquals(assetResponse.getNumClaimableBalances().intValue(), 415);
    assertEquals(assetResponse.getNumLiquidityPools().intValue(), 384);
    assertEquals(assetResponse.getNumContracts().intValue(), 24);
    assertEquals(assetResponse.getAccounts().getAuthorized().intValue(), 918786);
    assertEquals(assetResponse.getAccounts().getAuthorizedToMaintainLiabilities().intValue(), 0);
    assertEquals(assetResponse.getAccounts().getUnauthorized().intValue(), 0);
    assertEquals(assetResponse.getClaimableBalancesAmount(), "8073.0306754");
    assertEquals(assetResponse.getLiquidityPoolsAmount(), "3001425.9918924");
    assertEquals(assetResponse.getContractsAmount(), "453241.3236804");
    assertEquals(assetResponse.getBalances().getAuthorized(), "196141192.6091517");
    assertEquals(assetResponse.getBalances().getAuthorizedToMaintainLiabilities(), "0.0000000");
    assertEquals(assetResponse.getBalances().getUnauthorized(), "0.0000000");
    assertEquals(assetResponse.getFlags().getAuthRequired(), false);
    assertEquals(assetResponse.getFlags().getAuthRevocable(), true);
    assertEquals(assetResponse.getFlags().getAuthImmutable(), false);
    assertEquals(assetResponse.getFlags().getAuthClawbackEnabled(), false);
  }
}
