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

public class ScvInt128Test {

  @Test
  public void testScvInt128() throws IOException {
    List<TestCase> values =
        Arrays.asList(
            new TestCase(
                BigInteger.ZERO, new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),
            new TestCase(
                BigInteger.valueOf(1), new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}),
            new TestCase(
                BigInteger.valueOf(-1),
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
                }),
            new TestCase(
                BigInteger.valueOf(2L).pow(64),
                new byte[] {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}),
            new TestCase(
                BigInteger.valueOf(2L).pow(64).negate(),
                new byte[] {
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0
                }),
            new TestCase(
                ScvInt128.MAX_VALUE,
                new byte[] {
                  (byte) 0x7f,
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
                }),
            new TestCase(
                ScvInt128.MIN_VALUE,
                new byte[] {(byte) 0x80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));

    for (TestCase value : values) {
      checkScvInt128(value);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvInt128GreaterThanMaxValueThrows() {
    Scv.toInt128(ScvInt128.MAX_VALUE.add(BigInteger.ONE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvInt128LessThanMinValueThrows() {
    Scv.toInt128(ScvInt128.MIN_VALUE.subtract(BigInteger.ONE));
  }

  private void checkScvInt128(TestCase value) throws IOException {
    ScvInt128 scvInt128 = new ScvInt128(value.v);
    SCVal scVal = scvInt128.toSCVal();

    assertEquals(scvInt128.getValue(), value.v);
    assertEquals(ScvInt128.fromSCVal(scVal), scvInt128);
    assertArrayEquals(scVal.getI128().toXdrByteArray(), value.getExpectedBytes());

    assertEquals(Scv.toInt128(value.v), scVal);
    assertEquals(Scv.fromInt128(scVal), value.v);
  }

  @Value
  @AllArgsConstructor
  private static class TestCase {
    BigInteger v;
    byte[] expectedBytes;
  }
}
