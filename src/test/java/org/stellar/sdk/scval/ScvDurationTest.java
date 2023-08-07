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
    BigInteger value = ScvDuration.MAX_VALUE;
    ScvDuration scvDuration = new ScvDuration(value);
    SCVal scVal = scvDuration.toSCVal();

    assertEquals(scvDuration.getSCValType(), SCValType.SCV_DURATION);
    assertEquals(scvDuration.getValue(), XdrUnsignedHyperInteger.MAX_VALUE);

    assertEquals(ScvDuration.fromSCVal(scVal), scvDuration);
    assertEquals(Scv.fromSCVal(scVal), scvDuration);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_DURATION)
            .duration(new Duration(new Uint64(new XdrUnsignedHyperInteger(value))))
            .build();
    assertEquals(expectedScVal, scVal);
  }

  @Test
  public void testScvDurationMin() {
    BigInteger value = ScvDuration.MIN_VALUE;
    ScvDuration scvDuration = new ScvDuration(value);
    SCVal scVal = scvDuration.toSCVal();

    assertEquals(scvDuration.getSCValType(), SCValType.SCV_DURATION);
    assertEquals(scvDuration.getValue(), XdrUnsignedHyperInteger.MIN_VALUE);

    assertEquals(ScvDuration.fromSCVal(scVal), scvDuration);
    assertEquals(Scv.fromSCVal(scVal), scvDuration);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_DURATION)
            .duration(new Duration(new Uint64(new XdrUnsignedHyperInteger(value))))
            .build();
    assertEquals(expectedScVal, scVal);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvDurationMoreThanMaxThrows() {
    BigInteger value = ScvDuration.MAX_VALUE.add(BigInteger.ONE);
    new ScvDuration(value);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvDurationLessThanMinThrows() {
    BigInteger value = ScvDuration.MIN_VALUE.subtract(BigInteger.ONE);
    new ScvDuration(value);
  }
}