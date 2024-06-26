// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * BumpSequenceResultCode's original definition in the XDR file is:
 *
 * <pre>
 * enum BumpSequenceResultCode
 * {
 *     // codes considered as "success" for the operation
 *     BUMP_SEQUENCE_SUCCESS = 0,
 *     // codes considered as "failure" for the operation
 *     BUMP_SEQUENCE_BAD_SEQ = -1 // `bumpTo` is not within bounds
 * };
 * </pre>
 */
public enum BumpSequenceResultCode implements XdrElement {
  BUMP_SEQUENCE_SUCCESS(0),
  BUMP_SEQUENCE_BAD_SEQ(-1);

  private final int value;

  BumpSequenceResultCode(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static BumpSequenceResultCode decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return BUMP_SEQUENCE_SUCCESS;
      case -1:
        return BUMP_SEQUENCE_BAD_SEQ;
      default:
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(value);
  }

  public static BumpSequenceResultCode fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static BumpSequenceResultCode fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
