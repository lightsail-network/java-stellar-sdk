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

    SCVal expectedScVal =
        SCVal.builder().discriminant(SCValType.SCV_I32).i32(new Int32(value)).build();

    SCVal actualScVal = Scv.toInt32(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromInt32(actualScVal));
  }
}
