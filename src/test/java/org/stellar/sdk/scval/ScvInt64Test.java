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

    SCVal expectedScVal =
        SCVal.builder().discriminant(SCValType.SCV_I64).i64(new Int64(value)).build();

    SCVal actualScVal = Scv.toInt64(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromInt64(actualScVal));
  }
}
