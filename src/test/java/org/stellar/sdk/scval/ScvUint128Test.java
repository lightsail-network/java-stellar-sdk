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

public class ScvUint128Test {

  @Test
  public void testScvUint128() throws IOException {
    List<TestCase> values =
        Arrays.asList(
            new TestCase(
                ScvUint128.MIN_VALUE, new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),
            new TestCase(
                BigInteger.valueOf(1), new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}),
            new TestCase(
                BigInteger.valueOf(2L).pow(64),
                new byte[] {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}),
            new TestCase(
                ScvUint128.MAX_VALUE,
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
    new ScvUint128(ScvUint128.MAX_VALUE.add(BigInteger.ONE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvUint128LessThanMinValueThrows() {
    new ScvUint128(ScvUint128.MIN_VALUE.subtract(BigInteger.ONE));
  }

  private void checkScvUint128(TestCase value) throws IOException {
    ScvUint128 scvUint128 = new ScvUint128(value.v);
    SCVal scVal = scvUint128.toSCVal();

    assertEquals(scvUint128.getValue(), value.v);
    assertEquals(ScvUint128.fromSCVal(scVal), scvUint128);

    assertArrayEquals(scVal.getU128().toXdrByteArray(), value.getExpectedBytes());

    assertEquals(Scv.toUint128(value.v), scVal);
    assertEquals(Scv.fromUint128(scVal), value.v);
  }

  @Value
  @AllArgsConstructor
  private static class TestCase {
    BigInteger v;
    byte[] expectedBytes;
  }
}
