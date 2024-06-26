// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * SCEnvMetaKind's original definition in the XDR file is:
 *
 * <pre>
 * enum SCEnvMetaKind
 * {
 *     SC_ENV_META_KIND_INTERFACE_VERSION = 0
 * };
 * </pre>
 */
public enum SCEnvMetaKind implements XdrElement {
  SC_ENV_META_KIND_INTERFACE_VERSION(0);

  private final int value;

  SCEnvMetaKind(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static SCEnvMetaKind decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return SC_ENV_META_KIND_INTERFACE_VERSION;
      default:
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(value);
  }

  public static SCEnvMetaKind fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCEnvMetaKind fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
