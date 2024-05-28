package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

public class ScvUint32Test {
  @Test
  public void testScvUint32Max() {
    long value = XdrUnsignedInteger.MAX_VALUE;

    SCVal expectedScVal =
        SCVal.builder()
            .discriminant(SCValType.SCV_U32)
            .u32(new Uint32(new XdrUnsignedInteger(value)))
            .build();

    SCVal actualScVal = Scv.toUint32(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromUint32(actualScVal));
  }

  @Test
  public void testScvUint32Min() {
    long value = XdrUnsignedInteger.MAX_VALUE;

    SCVal expectedScVal =
        SCVal.builder()
            .discriminant(SCValType.SCV_U32)
            .u32(new Uint32(new XdrUnsignedInteger(value)))
            .build();

    SCVal actualScVal = Scv.toUint32(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromUint32(actualScVal));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint32Overflow() {
    long value = XdrUnsignedInteger.MAX_VALUE + 1;
    Scv.toUint32(value);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint32Underflow() {
    long value = XdrUnsignedInteger.MIN_VALUE - 1;
    Scv.toUint32(value);
  }
}
