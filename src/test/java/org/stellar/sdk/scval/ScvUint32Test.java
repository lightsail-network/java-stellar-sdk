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
    long value = ScvUint32.MAX_VALUE;
    ScvUint32 scvUint32 = new ScvUint32(value);
    SCVal scVal = scvUint32.toSCVal();

    assertEquals(scvUint32.getSCValType(), SCValType.SCV_U32);
    assertEquals(scvUint32.getValue(), value);

    assertEquals(ScvUint32.fromSCVal(scVal), scvUint32);
    assertEquals(Scv.fromSCVal(scVal), scvUint32);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_U32)
            .u32(new Uint32(new XdrUnsignedInteger((1L << 32) - 1)))
            .build();
    assertEquals(expectedScVal, scVal);
  }

  @Test
  public void testScvUint32Min() {
    long value = ScvUint32.MIN_VALUE;
    ScvUint32 scvUint32 = new ScvUint32(value);
    SCVal scVal = scvUint32.toSCVal();

    assertEquals(scvUint32.getSCValType(), SCValType.SCV_U32);
    assertEquals(scvUint32.getValue(), value);

    assertEquals(ScvUint32.fromSCVal(scVal), scvUint32);
    assertEquals(Scv.fromSCVal(scVal), scvUint32);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_U32)
            .u32(new Uint32(new XdrUnsignedInteger(0)))
            .build();
    assertEquals(expectedScVal, scVal);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint32Overflow() {
    long value = ScvUint32.MAX_VALUE + 1;
    new ScvUint32(value);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint32Underflow() {
    long value = ScvUint32.MIN_VALUE - 1;
    new ScvUint32(value);
  }
}
