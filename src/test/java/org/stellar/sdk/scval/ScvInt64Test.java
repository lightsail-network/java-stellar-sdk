package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvInt64Test {
  @Test
  public void testScvInt64() {
    long value = 23453454L;
    ScvInt64 scvInt64 = new ScvInt64(value);
    SCVal scVal = scvInt64.toSCVal();

    assertEquals(scvInt64.getValue(), value);
    assertEquals(ScvInt64.fromSCVal(scVal), scvInt64);

    SCVal expectedScVal =
        new SCVal.Builder().discriminant(SCValType.SCV_I64).i64(new Int64(value)).build();
    assertEquals(expectedScVal, scVal);

    assertEquals(Scv.toInt64(value), scVal);
    assertEquals(Scv.fromInt64(scVal), value);
  }
}
