package org.stellar.sdk;

import static org.junit.Assert.*;
import static org.stellar.sdk.Asset.create;

import org.junit.Test;
import org.stellar.sdk.xdr.LiquidityPoolType;

public class LiquidityPoolIDTest {
  private final Asset a = create("native");
  private final Asset b =
      create(null, "ABC", "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3");
  private final Asset c =
      create(null, "ABCD", "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3");

  @Test
  public void testLiquidityPoolID() {
    LiquidityPoolID id =
        new LiquidityPoolID(
            LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT, a, b, LiquidityPoolParameters.Fee);
    assertEquals("cc22414997d7e3d9a9ac3b1d65ca9cc3e5f35ce33e0bd6a885648b11aaa3b72d", id.toString());
  }

  @Test
  public void testLiquidityPoolIDMisorderedAssets() {
    try {
      new LiquidityPoolID(
          LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT, b, a, LiquidityPoolParameters.Fee);
      fail();
    } catch (RuntimeException e) {
      assertEquals("AssetA must be < AssetB", e.getMessage());
    }
  }

  @Test
  public void testEquality() {
    LiquidityPoolID pool1 =
        new LiquidityPoolID(
            LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT, a, b, LiquidityPoolParameters.Fee);
    assertEquals(pool1, pool1);
  }

  @Test
  public void testInequality() {
    LiquidityPoolID pool1 =
        new LiquidityPoolID(
            LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT, a, b, LiquidityPoolParameters.Fee);
    LiquidityPoolID pool2 =
        new LiquidityPoolID(
            LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT, b, c, LiquidityPoolParameters.Fee);
    assertNotEquals(pool1, pool2);
  }
}
