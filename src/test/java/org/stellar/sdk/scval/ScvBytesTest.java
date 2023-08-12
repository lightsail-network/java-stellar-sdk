package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.SCBytes;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvBytesTest {
  @Test
  public void testScvBytes() {
    byte[] data = new byte[] {0x01, 0x02, 0x03};

    SCVal expectedScVal =
        new SCVal.Builder().discriminant(SCValType.SCV_BYTES).bytes(new SCBytes(data)).build();

    SCVal actualScVal = Scv.toBytes(data);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(data, Scv.fromBytes(actualScVal));
  }
}
