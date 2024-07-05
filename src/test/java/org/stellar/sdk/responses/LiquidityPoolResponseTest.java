package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.xdr.LiquidityPoolType;

// {
//  "_links": {
//    "self": {
//      "href":
// "https://horizon.stellar.org/liquidity_pools/a468d41d8e9b8f3c7209651608b74b7db7ac9952dcae0cdf24871d1d9c7b0088"
//    },
//    "transactions": {
//      "href":
// "https://horizon.stellar.org/liquidity_pools/a468d41d8e9b8f3c7209651608b74b7db7ac9952dcae0cdf24871d1d9c7b0088/transactions{?cursor,limit,order}",
//      "templated": true
//    },
//    "operations": {
//      "href":
// "https://horizon.stellar.org/liquidity_pools/a468d41d8e9b8f3c7209651608b74b7db7ac9952dcae0cdf24871d1d9c7b0088/operations{?cursor,limit,order}",
//      "templated": true
//    }
//  },
//  "id": "a468d41d8e9b8f3c7209651608b74b7db7ac9952dcae0cdf24871d1d9c7b0088",
//  "paging_token": "a468d41d8e9b8f3c7209651608b74b7db7ac9952dcae0cdf24871d1d9c7b0088",
//  "fee_bp": 30,
//  "type": "constant_product",
//  "total_trustlines": "885",
//  "total_shares": "5787922.2796105",
//  "reserves": [
//    {
//      "asset": "native",
//      "amount": "22548198.4301913"
//    },
//    {
//      "asset": "USDC:GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN",
//      "amount": "1897783.4547179"
//    }
//  ],
//  "last_modified_ledger": 52426005,
//  "last_modified_time": "2024-07-05T00:57:29Z"
// }
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

    assertEquals(2, liquidityPoolResponse.getReserves().length);
    assertEquals(new AssetTypeNative(), liquidityPoolResponse.getReserves()[0].getAsset());
    assertEquals("22548198.4301913", liquidityPoolResponse.getReserves()[0].getAmount());
    assertEquals(
        Asset.create("USDC:GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN"),
        liquidityPoolResponse.getReserves()[1].getAsset());
    assertEquals("1897783.4547179", liquidityPoolResponse.getReserves()[1].getAmount());

    assertEquals(52426005L, liquidityPoolResponse.getLastModifiedLedger().longValue());
    assertEquals("2024-07-05T00:57:29Z", liquidityPoolResponse.getLastModifiedTime());
  }
}
