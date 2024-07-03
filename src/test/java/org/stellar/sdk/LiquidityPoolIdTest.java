package org.stellar.sdk;

import static org.junit.Assert.*;
import static org.stellar.sdk.Asset.create;

import org.junit.Test;

public class LiquidityPoolIdTest {
  private final Asset a = create("native");
  private final Asset b =
      create(null, "ABC", "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3");
  private final Asset c =
      create(null, "ABCD", "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3");

  @Test
  public void testLiquidityPoolID() {
    LiquidityPoolId id = new LiquidityPool(a, b).getLiquidityPoolId();

    assertEquals("cc22414997d7e3d9a9ac3b1d65ca9cc3e5f35ce33e0bd6a885648b11aaa3b72d", id.toString());
  }

  @Test
  public void testLiquidityPoolIDMisorderedAssets() {
    try {
      new LiquidityPool(b, a).getLiquidityPoolId();
      fail();
    } catch (RuntimeException e) {
      assertEquals("Assets are not in lexicographic order", e.getMessage());
    }
  }

  @Test
  public void testEquality() {
    LiquidityPoolId pool1 = new LiquidityPool(a, b).getLiquidityPoolId();
    LiquidityPoolId pool2 = new LiquidityPool(a, b).getLiquidityPoolId();
    assertEquals(pool1, pool2);
  }

  @Test
  public void testInequality() {

    LiquidityPoolId pool1 = new LiquidityPool(a, b).getLiquidityPoolId();
    LiquidityPoolId pool2 = new LiquidityPool(b, c).getLiquidityPoolId();
    assertNotEquals(pool1, pool2);
  }
}
