// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * BumpSequenceResultCode's original definition in the XDR file is:
 *
 * <pre>
 * enum BumpSequenceResultCode
 * {
 *     // codes considered as &quot;success&quot; for the operation
 *     BUMP_SEQUENCE_SUCCESS = 0,
 *     // codes considered as &quot;failure&quot; for the operation
 *     BUMP_SEQUENCE_BAD_SEQ = -1 // `bumpTo` is not within bounds
 * };
 * </pre>
 */
public enum BumpSequenceResultCode implements XdrElement {
  BUMP_SEQUENCE_SUCCESS(0),
  BUMP_SEQUENCE_BAD_SEQ(-1),
  ;
  private int mValue;

  BumpSequenceResultCode(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static BumpSequenceResultCode decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return BUMP_SEQUENCE_SUCCESS;
      case -1:
        return BUMP_SEQUENCE_BAD_SEQ;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, BumpSequenceResultCode value)
      throws IOException {
    stream.writeInt(value.getValue());
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64Factory.getInstance().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
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
