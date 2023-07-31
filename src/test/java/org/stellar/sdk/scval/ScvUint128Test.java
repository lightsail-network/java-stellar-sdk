package org.stellar.sdk.scval;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.google.common.io.BaseEncoding;
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
import org.stellar.sdk.xdr.UInt128Parts;
import org.stellar.sdk.xdr.XdrDataOutputStream;

public class ScvUint128Test {

  @Test
  public void testScvUint128() {
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

  private void checkScvUint128(TestCase value) {
    ScvUint128 scvUint128 = new ScvUint128(value.v);
    SCVal scVal = scvUint128.toSCVal();

    assertEquals(scvUint128.getSCValType(), SCValType.SCV_U128);
    assertEquals(scvUint128.getValue(), value.v);

    assertEquals(ScvUint128.fromSCVal(scVal), scvUint128);
    assertEquals(Scv.fromSCVal(scVal), scvUint128);

    assertArrayEquals(getUint128PartsBytes(scVal.getU128()), value.getExpectedBytes());
  }

  private byte[] getUint128PartsBytes(UInt128Parts uInt128Parts) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    try {
      uInt128Parts.encode(xdrDataOutputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid int128Parts.", e);
    }
    BaseEncoding base64Encoding = BaseEncoding.base64();
    return byteArrayOutputStream.toByteArray();
  }

  @Value
  @AllArgsConstructor
  private static class TestCase {
    BigInteger v;
    byte[] expectedBytes;
  }
}
