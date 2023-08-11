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

public class ScvInt256Test {

  @Test
  public void testScvInt256() throws IOException {
    List<TestCase> values =
        Arrays.asList(
            new TestCase(BigInteger.ZERO, new byte[32]),
            new TestCase(
                BigInteger.valueOf(1),
                new byte[] {
                  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                  0, 0, 0, 0, 1
                }),
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
                }),
            new TestCase(
                BigInteger.valueOf(2L).pow(64),
                new byte[] {
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x1,
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x0
                }),
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
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0
                }),
            new TestCase(
                BigInteger.valueOf(2L).pow(128),
                new byte[] {
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x1, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x0
                }),
            new TestCase(
                BigInteger.valueOf(2L).pow(128).negate(),
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
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0
                }),
            new TestCase(
                BigInteger.valueOf(2L).pow(192),
                new byte[] {
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                  (byte) 0x0, (byte) 0x0
                }),
            new TestCase(
                BigInteger.valueOf(2L).pow(192).negate(),
                new byte[] {
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0xff,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0,
                  (byte) 0x0
                }),
            new TestCase(
                ScvInt256.MAX_VALUE,
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
                }),
            new TestCase(
                ScvInt256.MIN_VALUE,
                new byte[] {
                  (byte) 0x80,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0,
                  0
                }));

    for (TestCase value : values) {
      checkScvInt256(value);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvInt256GreaterThanMaxValueThrows() {
    Scv.toInt256(ScvInt256.MAX_VALUE.add(BigInteger.ONE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvInt256LessThanMinValueThrows() {
    Scv.toInt256(ScvInt256.MIN_VALUE.subtract(BigInteger.ONE));
  }

  private void checkScvInt256(TestCase value) throws IOException {
    ScvInt256 scvInt256 = new ScvInt256(value.v);
    SCVal scVal = scvInt256.toSCVal();

    assertEquals(scvInt256.getValue(), value.v);
    assertEquals(ScvInt256.fromSCVal(scVal), scvInt256);
    assertArrayEquals(scVal.getI256().toXdrByteArray(), value.getExpectedBytes());

    assertEquals(Scv.toInt256(value.v), scVal);
    assertEquals(Scv.fromInt256(scVal), value.v);
  }

  @Value
  @AllArgsConstructor
  private static class TestCase {
    BigInteger v;
    byte[] expectedBytes;
  }
}
