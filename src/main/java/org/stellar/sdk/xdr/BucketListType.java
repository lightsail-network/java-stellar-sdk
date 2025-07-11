// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * BucketListType's original definition in the XDR file is:
 *
 * <pre>
 * enum BucketListType
 * {
 *     LIVE = 0,
 *     HOT_ARCHIVE = 1
 * };
 * </pre>
 */
public enum BucketListType implements XdrElement {
  LIVE(0),
  HOT_ARCHIVE(1);

  private final int value;

  BucketListType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static BucketListType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return LIVE;
      case 1:
        return HOT_ARCHIVE;
      default:
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(value);
  }

  public static BucketListType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static BucketListType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
