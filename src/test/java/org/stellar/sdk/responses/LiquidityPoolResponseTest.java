package org.stellar.sdk.responses;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;

import junit.framework.TestCase;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.xdr.LiquidityPoolType;

public class LiquidityPoolResponseTest extends TestCase {
  @Test
  public void testDeserialize() {
    String json =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"effects\": {\n"
            + "      \"href\": \"/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9/effects{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"operations\": {\n"
            + "      \"href\": \"/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9/operations{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9\"\n"
            + "    },\n"
            + "    \"transactions\": {\n"
            + "      \"href\": \"/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9/transactions{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    }\n"
            + "  },\n"
            + "  \"fee_bp\": 30,\n"
            + "  \"id\": \"67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9\",\n"
            + "  \"paging_token\": \"113725249324879873\",\n"
            + "  \"reserves\": [\n"
            + "    {\n"
            + "      \"amount\": \"1000.0000005\",\n"
            + "      \"asset\": \"EURT:GAP5LETOV6YIE62YAM56STDANPRDO7ZFDBGSNHJQIYGGKSMOZAHOOS2S\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"amount\": \"2000.0000000\",\n"
            + "      \"asset\": \"PHP:GAP5LETOV6YIE62YAM56STDANPRDO7ZFDBGSNHJQIYGGKSMOZAHOOS2S\"\n"
            + "    }\n"
            + "  ],\n"
            + "  \"total_shares\": \"5000.0000000\",\n"
            + "  \"total_trustlines\": \"300\",\n"
            + "  \"type\": \"constant_product\"\n"
            + "}";

    LiquidityPoolResponse liquidityPool =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolResponse.class);
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
        "/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9/effects{?cursor,limit,order}",
        liquidityPool.getLinks().getEffects().getHref());
    assertEquals(
        "/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9/operations{?cursor,limit,order}",
        liquidityPool.getLinks().getOperations().getHref());
    assertEquals(
        "/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9",
        liquidityPool.getLinks().getSelf().getHref());
    assertEquals(
        "/liquidity_pools/67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9/transactions{?cursor,limit,order}",
        liquidityPool.getLinks().getTransactions().getHref());
  }

  @Test
  public void testReserveEquality() {
    LiquidityPoolResponse.Reserve a =
        new LiquidityPoolResponse.Reserve(
            "2000.0000000",
            Asset.create("PHP:GAP5LETOV6YIE62YAM56STDANPRDO7ZFDBGSNHJQIYGGKSMOZAHOOS2S"));
    LiquidityPoolResponse.Reserve b =
        new LiquidityPoolResponse.Reserve(
            "2000.0000000",
            Asset.create("PHP:GAP5LETOV6YIE62YAM56STDANPRDO7ZFDBGSNHJQIYGGKSMOZAHOOS2S"));
    LiquidityPoolResponse.Reserve c =
        new LiquidityPoolResponse.Reserve(
            "1000.0000005", "PHP:GAP5LETOV6YIE62YAM56STDANPRDO7ZFDBGSNHJQIYGGKSMOZAHOOS2S");
    LiquidityPoolResponse.Reserve d =
        new LiquidityPoolResponse.Reserve(
            "2000.0000000",
            Asset.create("EURT:GAP5LETOV6YIE62YAM56STDANPRDO7ZFDBGSNHJQIYGGKSMOZAHOOS2S"));

    assertEquals(a, b);
    assertNotEquals(a, c);
    assertNotEquals(a, d);
    assertNotEquals(c, d);
  }
}
