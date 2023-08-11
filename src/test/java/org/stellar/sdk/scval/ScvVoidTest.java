package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvVoidTest {
  @Test
  public void testScvVoid() {
    ScvVoid scvVoid = new ScvVoid();
    SCVal scVal = scvVoid.toSCVal();

    assertEquals(ScvVoid.fromSCVal(scVal), scvVoid);

    SCVal expectedScVal = new SCVal.Builder().discriminant(SCValType.SCV_VOID).build();
    assertEquals(expectedScVal, scVal);

    assertEquals(Scv.toVoid(), scVal);
    Scv.fromVoid(scVal);
  }
}
