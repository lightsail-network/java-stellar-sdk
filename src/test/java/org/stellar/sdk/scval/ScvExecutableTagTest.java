package org.stellar.sdk.scval;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.nio.charset.StandardCharsets;
import org.junit.Test;
import org.stellar.sdk.xdr.SCString;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.XdrString;

public class ScvExecutableTagTest {
  @Test
  public void testScvExecutableTagFromString() {
    String value = "v1";

    SCVal expectedScVal =
        SCVal.builder()
            .discriminant(SCValType.SCV_EXECUTABLE_TAG)
            .executable_tag(new SCString(new XdrString(value)))
            .build();

    SCVal actualScVal = Scv.toExecutableTag(value);
    assertEquals(expectedScVal, actualScVal);
    assertArrayEquals(value.getBytes(StandardCharsets.UTF_8), Scv.fromExecutableTag(actualScVal));
  }

  @Test
  public void testScvExecutableTagFromStringEncodesUtf8() {
    // A non-ASCII tag must go over the wire as UTF-8 regardless of the platform default charset.
    String value = "标签";

    assertArrayEquals(
        value.getBytes(StandardCharsets.UTF_8), Scv.fromExecutableTag(Scv.toExecutableTag(value)));
  }

  @Test
  public void testScvExecutableTagFromBytes() {
    // A tag is an unbounded SCString: it need not be valid UTF-8, and it round-trips undecoded.
    byte[] value = new byte[] {(byte) 0xff, (byte) 0xfe, 0x00, 0x41};

    SCVal expectedScVal =
        SCVal.builder()
            .discriminant(SCValType.SCV_EXECUTABLE_TAG)
            .executable_tag(new SCString(new XdrString(value)))
            .build();

    SCVal actualScVal = Scv.toExecutableTag(value);
    assertEquals(expectedScVal, actualScVal);
    assertArrayEquals(value, Scv.fromExecutableTag(actualScVal));
  }

  @Test
  public void testScvExecutableTagFromEmptyBytes() {
    byte[] value = new byte[] {};
    assertArrayEquals(value, Scv.fromExecutableTag(Scv.toExecutableTag(value)));
  }

  @Test
  public void testFromExecutableTagInvalidTypeThrows() {
    IllegalArgumentException e =
        assertThrows(
            IllegalArgumentException.class, () -> Scv.fromExecutableTag(Scv.toString("v1")));
    assertEquals(
        "invalid scVal type, expected SCV_EXECUTABLE_TAG, but got SCV_STRING", e.getMessage());
  }
}
