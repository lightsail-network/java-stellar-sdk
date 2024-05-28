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

    SCVal expectedScVal =
        SCVal.builder()
            .discriminant(SCValType.SCV_STRING)
            .str(new SCString(new XdrString(value)))
            .build();

    SCVal actualScVal = Scv.toString(value);
    assertEquals(expectedScVal, actualScVal);
    assertArrayEquals(value.getBytes(), Scv.fromString(actualScVal));
  }

  @Test
  public void testScvStringFromBytes() {
    byte[] value = new byte[] {0, 1, 2, 3};

    SCVal expectedScVal =
        SCVal.builder()
            .discriminant(SCValType.SCV_STRING)
            .str(new SCString(new XdrString(value)))
            .build();

    SCVal actualScVal = Scv.toString(value);
    assertEquals(expectedScVal, actualScVal);
    assertArrayEquals(value, Scv.fromString(actualScVal));
  }
}
