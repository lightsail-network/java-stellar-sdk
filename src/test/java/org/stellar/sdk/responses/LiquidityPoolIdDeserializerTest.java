package org.stellar.sdk.responses;

import junit.framework.TestCase;
import org.junit.Test;
import org.stellar.sdk.LiquidityPoolId;

public class LiquidityPoolIdDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    String json = "67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9";

    LiquidityPoolId liquidityPoolID =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolId.class);
    assertEquals(
        new LiquidityPoolId("67260c4c1807b262ff851b0a3fe141194936bb0215b2f77447f1df11998eabb9"),
        liquidityPoolID);
  }
}
