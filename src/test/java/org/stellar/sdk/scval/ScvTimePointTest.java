package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.TimePoint;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

public class ScvTimePointTest {
  @Test
  public void testScvTimePointMax() {
    BigInteger value = XdrUnsignedHyperInteger.MAX_VALUE;

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_TIMEPOINT)
            .timepoint(new TimePoint(new Uint64(new XdrUnsignedHyperInteger(value))))
            .build();

    SCVal actualScVal = Scv.toTimePoint(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromTimePoint(actualScVal));
  }

  @Test
  public void testScvTimePointMin() {
    BigInteger value = XdrUnsignedHyperInteger.MIN_VALUE;

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_TIMEPOINT)
            .timepoint(new TimePoint(new Uint64(new XdrUnsignedHyperInteger(value))))
            .build();

    SCVal actualScVal = Scv.toTimePoint(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromTimePoint(actualScVal));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvTimePointMoreThanMaxThrows() {
    BigInteger value = XdrUnsignedHyperInteger.MAX_VALUE.add(BigInteger.ONE);
    Scv.toTimePoint(value);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvTimePointLessThanMinThrows() {
    BigInteger value = XdrUnsignedHyperInteger.MIN_VALUE.subtract(BigInteger.ONE);
    Scv.toTimePoint(value);
  }
}
