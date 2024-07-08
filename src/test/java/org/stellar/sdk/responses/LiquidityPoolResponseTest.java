package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.responses.gson.GsonSingleton;
import org.stellar.sdk.xdr.LiquidityPoolType;

public class LiquidityPoolResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/liquidity_pool.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolResponse liquidityPoolResponse =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolResponse.class);

    assertEquals(
        "a468d41d8e9b8f3c7209651608b74b7db7ac9952dcae0cdf24871d1d9c7b0088",
        liquidityPoolResponse.getId().toString());

    assertEquals(
        "a468d41d8e9b8f3c7209651608b74b7db7ac9952dcae0cdf24871d1d9c7b0088",
        liquidityPoolResponse.getPagingToken());
    assertEquals(30, liquidityPoolResponse.getFeeBP().intValue());
    assertEquals(
        LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT, liquidityPoolResponse.getType());
    assertEquals(885, liquidityPoolResponse.getTotalTrustlines().longValue());
    assertEquals("5787922.2796105", liquidityPoolResponse.getTotalShares());

    assertEquals(2, liquidityPoolResponse.getReserves().size());
    assertEquals(new AssetTypeNative(), liquidityPoolResponse.getReserves().get(0).getAsset());
    assertEquals("22548198.4301913", liquidityPoolResponse.getReserves().get(0).getAmount());
    assertEquals(
        Asset.create("USDC:GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN"),
        liquidityPoolResponse.getReserves().get(1).getAsset());
    assertEquals("1897783.4547179", liquidityPoolResponse.getReserves().get(1).getAmount());

    assertEquals(52426005L, liquidityPoolResponse.getLastModifiedLedger().longValue());
    assertEquals("2024-07-05T00:57:29Z", liquidityPoolResponse.getLastModifiedTime());
  }
}
