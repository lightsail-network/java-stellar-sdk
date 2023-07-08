package org.stellar.sdk.responses;

import static org.junit.Assert.assertArrayEquals;

import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Test;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.xdr.LiquidityPoolType;

public class LiquidityPoolsPageDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    Page<LiquidityPoolResponse> liquidityPoolsPage =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<Page<LiquidityPoolResponse>>() {}.getType());

    assertEquals(
        "https://horizon.stellar.org/liquidity_pools?cursor=3748308153536513-0\\u0026limit=10\\u0026order=asc",
        liquidityPoolsPage.getLinks().getNext().getHref());
    assertEquals(
        "https://horizon.stellar.org/liquidity_pools?cursor=3697472920621057-0\\u0026limit=10\\u0026order=desc",
        liquidityPoolsPage.getLinks().getPrev().getHref());
    assertEquals(
        "https://horizon.stellar.org/liquidity_pools?cursor=\\u0026limit=10\\u0026order=asc",
        liquidityPoolsPage.getLinks().getSelf().getHref());

    LiquidityPoolResponse liquidityPool = liquidityPoolsPage.getRecords().get(0);
    assertEquals(
        new LiquidityPoolID("67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9"),
        liquidityPool.getID());
    assertEquals("113725249324879873", liquidityPool.getPagingToken());
    assertEquals(Integer.valueOf(30), liquidityPool.getFeeBP());
    assertEquals(LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT, liquidityPool.getType());
    assertEquals(Long.valueOf(300), liquidityPool.getTotalTrustlines());
    assertEquals("5000.0000000", liquidityPool.getTotalShares());
    assertArrayEquals(
        new LiquidityPoolResponse.Reserve[] {
          new LiquidityPoolResponse.Reserve(
              "1000.0000005", "EURT:GAP5LETOV6YIE62YAM56STDANPRDO7ZFDBGSNHJQIYGGKSMOZAHOOS2S"),
          new LiquidityPoolResponse.Reserve(
              "2000.0000000", "PHP:GAP5LETOV6YIE62YAM56STDANPRDO7ZFDBGSNHJQIYGGKSMOZAHOOS2S")
        },
        liquidityPool.getReserves());
    assertEquals(
        "https://horizon.stellar.org/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9/effects{?cursor,limit,order}",
        liquidityPool.getLinks().getEffects().getHref());
    assertEquals(
        "https://horizon.stellar.org/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9/operations{?cursor,limit,order}",
        liquidityPool.getLinks().getOperations().getHref());
    assertEquals(
        "https://horizon.stellar.org/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9",
        liquidityPool.getLinks().getSelf().getHref());
    assertEquals(
        "https://horizon.stellar.org/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9/transactions{?cursor,limit,order}",
        liquidityPool.getLinks().getTransactions().getHref());

    assertEquals(
        new LiquidityPoolID("14be5a5b3d3f5e1e74380ab0a3bf9c172b7246fdf7753b172cbacd4d66143c08"),
        liquidityPoolsPage.getRecords().get(1).getID());
  }

  String json =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"next\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/liquidity_pools?cursor=3748308153536513-0\\\\u0026limit=10\\\\u0026order=asc\"\n"
          + "    },\n"
          + "    \"prev\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/liquidity_pools?cursor=3697472920621057-0\\\\u0026limit=10\\\\u0026order=desc\"\n"
          + "    },\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/liquidity_pools?cursor=\\\\u0026limit=10\\\\u0026order=asc\"\n"
          + "    }\n"
          + "  },\n"
          + "  \"_embedded\": {\n"
          + "    \"records\": [\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9/effects{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"operations\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9/operations{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"self\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9\"\n"
          + "          },\n"
          + "          \"transactions\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9/transactions{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          }\n"
          + "        },\n"
          + "        \"fee_bp\": 30,\n"
          + "        \"id\": \"67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9\",\n"
          + "        \"paging_token\": \"113725249324879873\",\n"
          + "        \"reserves\": [\n"
          + "          {\n"
          + "            \"amount\": \"1000.0000005\",\n"
          + "            \"asset\": \"EURT:GAP5LETOV6YIE62YAM56STDANPRDO7ZFDBGSNHJQIYGGKSMOZAHOOS2S\"\n"
          + "          },\n"
          + "          {\n"
          + "            \"amount\": \"2000.0000000\",\n"
          + "            \"asset\": \"PHP:GAP5LETOV6YIE62YAM56STDANPRDO7ZFDBGSNHJQIYGGKSMOZAHOOS2S\"\n"
          + "          }\n"
          + "        ],\n"
          + "        \"total_shares\": \"5000.0000000\",\n"
          + "        \"total_trustlines\": \"300\",\n"
          + "        \"type\": \"constant_product\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/liquidity_pools/14be5a5b3d3f5e1e74380ab0a3bf9c172b7246fdf7753b172cbacd4d66143c08/effects{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"operations\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/liquidity_pools/14be5a5b3d3f5e1e74380ab0a3bf9c172b7246fdf7753b172cbacd4d66143c08/operations{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"self\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/liquidity_pools/14be5a5b3d3f5e1e74380ab0a3bf9c172b7246fdf7753b172cbacd4d66143c08\"\n"
          + "          },\n"
          + "          \"transactions\": {\n"
          + "            \"href\": \"https://horizon.stellar.org/liquidity_pools/14be5a5b3d3f5e1e74380ab0a3bf9c172b7246fdf7753b172cbacd4d66143c08/transactions{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          }\n"
          + "        },\n"
          + "        \"fee_bp\": 30,\n"
          + "        \"id\": \"14be5a5b3d3f5e1e74380ab0a3bf9c172b7246fdf7753b172cbacd4d66143c08\",\n"
          + "        \"paging_token\": \"113725249324879874\",\n"
          + "        \"reserves\": [\n"
          + "          {\n"
          + "            \"amount\": \"1000.0000005\",\n"
          + "            \"asset\": \"EURT:GAP5LETOV6YIE62YAM56STDANPRDO7ZFDBGSNHJQIYGGKSMOZAHOOS2S\"\n"
          + "          },\n"
          + "          {\n"
          + "            \"amount\": \"1200.0000000\",\n"
          + "            \"asset\": \"USDC:GC5W3BH2MQRQK2H4A6LP3SXDSAAY2W2W64OWKKVNQIAOVWSAHFDEUSDC\"\n"
          + "          }\n"
          + "        ],\n"
          + "        \"total_shares\": \"3500\",\n"
          + "        \"total_trustlines\": \"200\",\n"
          + "        \"type\": \"constant_product\"\n"
          + "      }\n"
          + "    ]\n"
          + "  }\n"
          + "}\n";
}
