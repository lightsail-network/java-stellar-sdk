package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

public class ScvUint64Test {
  @Test
  public void testScvUint64Max() {
    BigInteger value = ScvUint64.MAX_VALUE;
    ScvUint64 scvUint64 = new ScvUint64(value);
    SCVal scVal = scvUint64.toSCVal();

    assertEquals(scvUint64.getSCValType(), SCValType.SCV_U64);
    assertEquals(scvUint64.getValue(), value);

    assertEquals(ScvUint64.fromSCVal(scVal), scvUint64);
    assertEquals(Scv.fromSCVal(scVal), scvUint64);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_U64)
            .u64(new Uint64(new XdrUnsignedHyperInteger(XdrUnsignedHyperInteger.MAX_VALUE)))
            .build();
    assertEquals(expectedScVal, scVal);
  }

  @Test
  public void testScvUint64Min() {
    BigInteger value = ScvUint64.MIN_VALUE;
    ScvUint64 scvUint64 = new ScvUint64(value);
    SCVal scVal = scvUint64.toSCVal();

    assertEquals(scvUint64.getSCValType(), SCValType.SCV_U64);
    assertEquals(scvUint64.getValue(), value);

    assertEquals(ScvUint64.fromSCVal(scVal), scvUint64);
    assertEquals(Scv.fromSCVal(scVal), scvUint64);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_U64)
            .u64(new Uint64(new XdrUnsignedHyperInteger(XdrUnsignedInteger.MIN_VALUE)))
            .build();
    assertEquals(expectedScVal, scVal);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint64GreaterThanMaxValueThrows() {
    new ScvUint64(ScvUint64.MAX_VALUE.add(BigInteger.ONE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint64LessThanMinValueThrows() {
    new ScvUint64(ScvUint64.MIN_VALUE.subtract(BigInteger.ONE));
  }
}
