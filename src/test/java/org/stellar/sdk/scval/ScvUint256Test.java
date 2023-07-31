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
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.UInt256Parts;
import org.stellar.sdk.xdr.XdrDataOutputStream;

public class ScvUint256Test {
  @Test
  public void testScvUint256() {
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

  private void checkScvUint256(TestCase value) {
    ScvUint256 scvUint256 = new ScvUint256(value.v);
    SCVal scVal = scvUint256.toSCVal();

    assertEquals(scvUint256.getSCValType(), SCValType.SCV_U256);
    assertEquals(scvUint256.getValue(), value.v);

    assertEquals(ScvUint256.fromSCVal(scVal), scvUint256);
    assertEquals(Scv.fromSCVal(scVal), scvUint256);

    assertArrayEquals(getInt256PartsBytes(scVal.getU256()), value.getExpectedBytes());
  }

  private byte[] getInt256PartsBytes(UInt256Parts uint256Parts) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    try {
      uint256Parts.encode(xdrDataOutputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid uint256Parts.", e);
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
