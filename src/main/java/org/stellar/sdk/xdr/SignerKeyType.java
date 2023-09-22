// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  enum SignerKeyType
//  {
//      SIGNER_KEY_TYPE_ED25519 = KEY_TYPE_ED25519,
//      SIGNER_KEY_TYPE_PRE_AUTH_TX = KEY_TYPE_PRE_AUTH_TX,
//      SIGNER_KEY_TYPE_HASH_X = KEY_TYPE_HASH_X,
//      SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD = KEY_TYPE_ED25519_SIGNED_PAYLOAD
//  };

//  ===========================================================================
public enum SignerKeyType implements XdrElement {
  SIGNER_KEY_TYPE_ED25519(0),
  SIGNER_KEY_TYPE_PRE_AUTH_TX(1),
  SIGNER_KEY_TYPE_HASH_X(2),
  SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD(3),
  ;
  private int mValue;

  SignerKeyType(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static SignerKeyType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return SIGNER_KEY_TYPE_ED25519;
      case 1:
        return SIGNER_KEY_TYPE_PRE_AUTH_TX;
      case 2:
        return SIGNER_KEY_TYPE_HASH_X;
      case 3:
        return SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, SignerKeyType value) throws IOException {
    stream.writeInt(value.getValue());
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static SignerKeyType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SignerKeyType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
