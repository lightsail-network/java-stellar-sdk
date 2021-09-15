package org.stellar.sdk;

import org.junit.Test;

import org.stellar.sdk.xdr.LiquidityPoolType;

import static org.junit.Assert.*;

public class LiquidityPoolIDTest {
  private final Asset a = Asset.create("native");
  private final Asset b = Asset.createNonNativeAsset("ABC", "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3");

  @Test
  public void testLiquidityPoolID() {
    LiquidityPoolID id = new LiquidityPoolID(LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT, a, b, LiquidityPoolParameters.Fee);
    assertEquals("cc22414997d7e3d9a9ac3b1d65ca9cc3e5f35ce33e0bd6a885648b11aaa3b72d", id.toString());
  }

  @Test
  public void testLiquidityPoolIDMisorderedAssets() {
    try {
      new LiquidityPoolID(LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT, b, a, LiquidityPoolParameters.Fee);
      fail();
    } catch (RuntimeException e) {
      assertEquals("AssetA must be < AssetB", e.getMessage());
    }
  }
}
