package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import org.junit.Test;
import org.stellar.sdk.xdr.Duration;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

public class ScvDurationTest {
  @Test
  public void testScvDurationMax() {
    BigInteger value = XdrUnsignedHyperInteger.MAX_VALUE;

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_DURATION)
            .duration(new Duration(new Uint64(new XdrUnsignedHyperInteger(value))))
            .build();

    SCVal actualScVal = Scv.toDuration(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromDuration(actualScVal));
  }

  @Test
  public void testScvDurationMin() {
    BigInteger value = XdrUnsignedHyperInteger.MIN_VALUE;

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_DURATION)
            .duration(new Duration(new Uint64(new XdrUnsignedHyperInteger(value))))
            .build();

    SCVal actualScVal = Scv.toDuration(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromDuration(actualScVal));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvDurationMoreThanMaxThrows() {
    BigInteger value = XdrUnsignedHyperInteger.MAX_VALUE.add(BigInteger.ONE);
    ScvDuration.toSCVal(value);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvDurationLessThanMinThrows() {
    BigInteger value = XdrUnsignedHyperInteger.MIN_VALUE.subtract(BigInteger.ONE);
    ScvDuration.toSCVal(value);
  }
}
