package org.stellar.sdk.scval;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.SCString;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.XdrString;

public class ScvStringTest {
  @Test
  public void testScvStringFromString() {
    String value = "hello";
    ScvString scvString = new ScvString(value);
    SCVal scVal = scvString.toSCVal();

    assertEquals(scvString.getSCValType(), SCValType.SCV_STRING);
    assertArrayEquals(scvString.getValue(), value.getBytes());

    assertEquals(ScvString.fromSCVal(scVal), scvString);
    assertEquals(Scv.fromSCVal(scVal), scvString);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_STRING)
            .str(new SCString(new XdrString(value)))
            .build();
    assertEquals(expectedScVal, scVal);
  }

  @Test
  public void testScvStringFromBytes() {
    byte[] value = new byte[] {0, 1, 2, 3};
    ScvString scvString = new ScvString(value);
    SCVal scVal = scvString.toSCVal();

    assertEquals(scvString.getSCValType(), SCValType.SCV_STRING);
    assertArrayEquals(scvString.getValue(), value);

    assertEquals(ScvString.fromSCVal(scVal), scvString);
    assertEquals(Scv.fromSCVal(scVal), scvString);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_STRING)
            .str(new SCString(new XdrString(value)))
            .build();
    assertEquals(expectedScVal, scVal);
  }
}
