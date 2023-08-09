// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// === xdr source ============================================================

//  enum ErrorCode
//  {
//      ERR_MISC = 0, // Unspecific error
//      ERR_DATA = 1, // Malformed data
//      ERR_CONF = 2, // Misconfiguration error
//      ERR_AUTH = 3, // Authentication failure
//      ERR_LOAD = 4  // System overloaded
//  };

//  ===========================================================================
public enum ErrorCode implements XdrElement {
  ERR_MISC(0),
  ERR_DATA(1),
  ERR_CONF(2),
  ERR_AUTH(3),
  ERR_LOAD(4),
  ;
  private int mValue;

  ErrorCode(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static ErrorCode decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return ERR_MISC;
      case 1:
        return ERR_DATA;
      case 2:
        return ERR_CONF;
      case 3:
        return ERR_AUTH;
      case 4:
        return ERR_LOAD;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, ErrorCode value) throws IOException {
    stream.writeInt(value.getValue());
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  @Override
  public String toXdrBase64() throws IOException {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    return base64Encoding.encode(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static ErrorCode fromXdrBase64(String xdr) throws IOException {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ErrorCode fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
