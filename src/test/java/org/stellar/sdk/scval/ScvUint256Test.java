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

public class ScvUint256Test {
  private static final BigInteger MIN_VALUE = BigInteger.ZERO;
  private static final BigInteger MAX_VALUE =
      BigInteger.valueOf(2).pow(256).subtract(BigInteger.ONE);

  @Test
  public void testScvUint256() throws IOException {
    List<TestCase> values =
        Arrays.asList(
            new TestCase(MIN_VALUE, new byte[32]),
            new TestCase(
                BigInteger.valueOf(1),
                new byte[] {
                  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                  0, 0, 0, 0, 1
                }),
            new TestCase(
                BigInteger.valueOf(2).pow(64),
                new byte[] {
                  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
                  0, 0, 0, 0, 0
                }),
            new TestCase(
                BigInteger.valueOf(2).pow(128),
                new byte[] {
                  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                  0, 0, 0, 0, 0
                }),
            new TestCase(
                BigInteger.valueOf(2).pow(192),
                new byte[] {
                  0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                  0, 0, 0, 0, 0
                }),
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
                  (byte) 0xff,
                  (byte) 0xff
                }));

    for (TestCase value : values) {
      checkScvUint256(value);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint256GreaterThanMaxValueThrows() {
    Scv.toUint256(MAX_VALUE.add(BigInteger.ONE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint256LessThanMinValueThrows() {
    Scv.toUint256(MIN_VALUE.subtract(BigInteger.ONE));
  }

  private void checkScvUint256(TestCase value) throws IOException {
    SCVal scVal = Scv.toUint256(value.v);
    assertEquals(scVal.getDiscriminant(), SCValType.SCV_U256);
    assertEquals(Scv.fromUint256(scVal), value.v);
    assertArrayEquals(scVal.getU256().toXdrByteArray(), value.getExpectedBytes());
  }

  @Value
  @AllArgsConstructor
  private static class TestCase {
    BigInteger v;
    byte[] expectedBytes;
  }
}
