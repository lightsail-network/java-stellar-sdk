// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * BucketEntryType's original definition in the XDR file is:
 *
 * <pre>
 * enum BucketEntryType
 * {
 *     METAENTRY =
 *         -1, // At-and-after protocol 11: bucket metadata, should come first.
 *     LIVEENTRY = 0, // Before protocol 11: created-or-updated;
 *                    // At-and-after protocol 11: only updated.
 *     DEADENTRY = 1,
 *     INITENTRY = 2 // At-and-after protocol 11: only created.
 * };
 * </pre>
 */
public enum BucketEntryType implements XdrElement {
  METAENTRY(-1),
  LIVEENTRY(0),
  DEADENTRY(1),
  INITENTRY(2);

  private final int value;

  BucketEntryType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static BucketEntryType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case -1:
        return METAENTRY;
      case 0:
        return LIVEENTRY;
      case 1:
        return DEADENTRY;
      case 2:
        return INITENTRY;
      default:
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(value);
  }

  public static BucketEntryType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static BucketEntryType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
