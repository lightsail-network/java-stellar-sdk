package org.stellar.sdk.scval;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvUint128Test {
  private static final BigInteger MIN_VALUE = BigInteger.ZERO;
  private static final BigInteger MAX_VALUE =
      BigInteger.valueOf(2).pow(128).subtract(BigInteger.ONE);

  @Test
  public void testScvUint128() throws IOException {
    List<TestCase> values =
        Arrays.asList(
            new TestCase(MIN_VALUE, new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),
            new TestCase(
                BigInteger.valueOf(1), new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}),
            new TestCase(
                BigInteger.valueOf(2L).pow(64),
                new byte[] {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}),
            new TestCase(
                MAX_VALUE,
                new byte[] {
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff
                }));

    for (TestCase value : values) {
      checkScvUint128(value);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint128GreaterThanMaxValueThrows() {
    Scv.toUint128(MAX_VALUE.add(BigInteger.ONE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint128LessThanMinValueThrows() {
    Scv.toUint128(MIN_VALUE.subtract(BigInteger.ONE));
  }

  private void checkScvUint128(TestCase value) throws IOException {
    SCVal scVal = Scv.toUint128(value.v);
    assertEquals(scVal.getDiscriminant(), SCValType.SCV_U128);
    assertEquals(Scv.fromUint128(scVal), value.v);
    assertArrayEquals(scVal.getU128().toXdrByteArray(), value.getExpectedBytes());
  }

  @Value
  @AllArgsConstructor
  private static class TestCase {
    BigInteger v;
    byte[] expectedBytes;
  }
}
