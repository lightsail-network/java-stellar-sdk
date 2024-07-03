package org.stellar.sdk;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;
import org.stellar.sdk.xdr.PoolID;

public class LiquidityPoolIDTest {
  @Test
  public void testLiquidityPoolIdFromHex() throws IOException {
    String id = "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380fac7";
    LiquidityPoolID poolId = new LiquidityPoolID(id);
    assertEquals(poolId.getPoolId(), id);
    assertEquals(poolId.toXdr().toXdrBase64(), "3XsauDHCczEN2+xvl4cKqDwvvXjOIq3tN+y/TzOA+sc=");
    PoolID xdr = poolId.toXdr();
    LiquidityPoolID parsedPoolId = LiquidityPoolID.fromXdr(xdr);
    assertEquals(poolId, parsedPoolId);
  }

  @Test
  public void testLiquidityPoolIdFromBytes() throws IOException {
    String idHex = "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380fac7";
    byte[] id = Util.hexToBytes(idHex);
    LiquidityPoolID poolId = new LiquidityPoolID(id);
    assertEquals(poolId.getPoolId(), idHex);
    assertEquals(poolId.toXdr().toXdrBase64(), "3XsauDHCczEN2+xvl4cKqDwvvXjOIq3tN+y/TzOA+sc=");
    PoolID xdr = poolId.toXdr();
    LiquidityPoolID parsedPoolId = LiquidityPoolID.fromXdr(xdr);
    assertEquals(poolId, parsedPoolId);
  }
}
