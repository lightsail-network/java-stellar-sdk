package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

public class ScvUint64Test {
  @Test
  public void testScvUint64Max() {
    BigInteger value = XdrUnsignedHyperInteger.MAX_VALUE;

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_U64)
            .u64(new Uint64(new XdrUnsignedHyperInteger(value)))
            .build();

    SCVal actualScVal = Scv.toUint64(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromUint64(actualScVal));
  }

  @Test
  public void testScvUint64Min() {
    BigInteger value = XdrUnsignedHyperInteger.MIN_VALUE;

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_U64)
            .u64(new Uint64(new XdrUnsignedHyperInteger(value)))
            .build();

    SCVal actualScVal = Scv.toUint64(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromUint64(actualScVal));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint64GreaterThanMaxValueThrows() {
    Scv.toUint64(XdrUnsignedHyperInteger.MAX_VALUE.add(BigInteger.ONE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint64LessThanMinValueThrows() {
    Scv.toUint64(XdrUnsignedHyperInteger.MIN_VALUE.subtract(BigInteger.ONE));
  }
}
