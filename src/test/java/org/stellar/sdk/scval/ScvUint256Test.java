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

public class ScvUint256Test {
  @Test
  public void testScvUint256() throws IOException {
    List<TestCase> values =
        Arrays.asList(
            new TestCase(ScvUint256.MIN_VALUE, new byte[32]),
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
                ScvUint256.MAX_VALUE,
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
    new ScvUint256(ScvUint256.MAX_VALUE.add(BigInteger.ONE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint256LessThanMinValueThrows() {
    new ScvUint256(ScvUint256.MIN_VALUE.subtract(BigInteger.ONE));
  }

  private void checkScvUint256(TestCase value) throws IOException {
    ScvUint256 scvUint256 = new ScvUint256(value.v);
    SCVal scVal = scvUint256.toSCVal();

    assertEquals(scvUint256.getValue(), value.v);
    assertEquals(ScvUint256.fromSCVal(scVal), scvUint256);

    assertArrayEquals(scVal.getU256().toXdrByteArray(), value.getExpectedBytes());

    assertEquals(Scv.toUint256(value.v), scVal);
    assertEquals(Scv.fromUint256(scVal), value.v);
  }

  @Value
  @AllArgsConstructor
  private static class TestCase {
    BigInteger v;
    byte[] expectedBytes;
  }
}
