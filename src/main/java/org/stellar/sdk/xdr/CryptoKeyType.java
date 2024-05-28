// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * CryptoKeyType's original definition in the XDR file is:
 *
 * <pre>
 * enum CryptoKeyType
 * {
 *     KEY_TYPE_ED25519 = 0,
 *     KEY_TYPE_PRE_AUTH_TX = 1,
 *     KEY_TYPE_HASH_X = 2,
 *     KEY_TYPE_ED25519_SIGNED_PAYLOAD = 3,
 *     // MUXED enum values for supported type are derived from the enum values
 *     // above by ORing them with 0x100
 *     KEY_TYPE_MUXED_ED25519 = 0x100
 * };
 * </pre>
 */
public enum CryptoKeyType implements XdrElement {
  KEY_TYPE_ED25519(0),
  KEY_TYPE_PRE_AUTH_TX(1),
  KEY_TYPE_HASH_X(2),
  KEY_TYPE_ED25519_SIGNED_PAYLOAD(3),
  KEY_TYPE_MUXED_ED25519(256);

  private final int value;

  CryptoKeyType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static CryptoKeyType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return KEY_TYPE_ED25519;
      case 1:
        return KEY_TYPE_PRE_AUTH_TX;
      case 2:
        return KEY_TYPE_HASH_X;
      case 3:
        return KEY_TYPE_ED25519_SIGNED_PAYLOAD;
      case 256:
        return KEY_TYPE_MUXED_ED25519;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, CryptoKeyType value) throws IOException {
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

  public static CryptoKeyType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static CryptoKeyType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
