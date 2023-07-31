package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.Int32;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvInt32Test {
  @Test
  public void testScvInt32() {
    int value = -234;
    ScvInt32 scvInt32 = new ScvInt32(value);
    SCVal scVal = scvInt32.toSCVal();

    assertEquals(scvInt32.getSCValType(), SCValType.SCV_I32);
    assertEquals(scvInt32.getValue(), value);

    assertEquals(ScvInt32.fromSCVal(scVal), scvInt32);
    assertEquals(Scv.fromSCVal(scVal), scvInt32);

    SCVal expectedScVal =
        new SCVal.Builder().discriminant(SCValType.SCV_I32).i32(new Int32(value)).build();
    assertEquals(expectedScVal, scVal);
  }
}
