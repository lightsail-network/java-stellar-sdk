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
    BigInteger value = ScvTimePoint.MAX_VALUE;
    ScvTimePoint scvTimePoint = new ScvTimePoint(value);
    SCVal scVal = scvTimePoint.toSCVal();

    assertEquals(scvTimePoint.getSCValType(), SCValType.SCV_TIMEPOINT);
    assertEquals(scvTimePoint.getValue(), value);

    assertEquals(ScvTimePoint.fromSCVal(scVal), scvTimePoint);
    assertEquals(Scv.fromSCVal(scVal), scvTimePoint);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_TIMEPOINT)
            .timepoint(new TimePoint(new Uint64(new XdrUnsignedHyperInteger(value))))
            .build();
    assertEquals(expectedScVal, scVal);
  }

  @Test
  public void testScvTimePointMin() {
    BigInteger value = ScvTimePoint.MIN_VALUE;
    ScvTimePoint scvTimePoint = new ScvTimePoint(value);
    SCVal scVal = scvTimePoint.toSCVal();

    assertEquals(scvTimePoint.getSCValType(), SCValType.SCV_TIMEPOINT);
    assertEquals(scvTimePoint.getValue(), value);

    assertEquals(ScvTimePoint.fromSCVal(scVal), scvTimePoint);
    assertEquals(Scv.fromSCVal(scVal), scvTimePoint);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_TIMEPOINT)
            .timepoint(new TimePoint(new Uint64(new XdrUnsignedHyperInteger(value))))
            .build();
    assertEquals(expectedScVal, scVal);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvTimePointMoreThanMaxThrows() {
    BigInteger value = ScvDuration.MAX_VALUE.add(BigInteger.ONE);
    new ScvTimePoint(value);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvTimePointLessThanMinThrows() {
    BigInteger value = ScvDuration.MIN_VALUE.subtract(BigInteger.ONE);
    new ScvTimePoint(value);
  }
}
