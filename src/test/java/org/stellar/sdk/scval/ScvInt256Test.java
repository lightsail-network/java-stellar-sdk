package org.stellar.sdk.scval;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.junit.Test;
import org.stellar.sdk.xdr.Int256Parts;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.XdrDataOutputStream;

public class ScvInt256Test {

  @Test
  public void testScvInt256() {
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
    new ScvInt256(ScvInt256.MAX_VALUE.add(BigInteger.ONE));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScvInt256LessThanMinValueThrows() {
    new ScvInt256(ScvInt256.MIN_VALUE.subtract(BigInteger.ONE));
  }

  private void checkScvInt256(TestCase value) {
    ScvInt256 scvInt256 = new ScvInt256(value.v);
    SCVal scVal = scvInt256.toSCVal();

    assertEquals(scvInt256.getSCValType(), SCValType.SCV_I256);
    assertEquals(scvInt256.getValue(), value.v);

    assertEquals(ScvInt256.fromSCVal(scVal), scvInt256);
    assertEquals(Scv.fromSCVal(scVal), scvInt256);

    assertArrayEquals(getInt256PartsBytes(scVal.getI256()), value.getExpectedBytes());
  }

  private byte[] getInt256PartsBytes(Int256Parts int256Parts) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    try {
      int256Parts.encode(xdrDataOutputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid int256Parts.", e);
    }
    return byteArrayOutputStream.toByteArray();
  }

  @Value
  @AllArgsConstructor
  private static class TestCase {
    BigInteger v;
    byte[] expectedBytes;
  }
}
