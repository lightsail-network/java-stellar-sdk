// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  enum ClawbackResultCode
//  {
//      // codes considered as "success" for the operation
//      CLAWBACK_SUCCESS = 0,
//
//      // codes considered as "failure" for the operation
//      CLAWBACK_MALFORMED = -1,
//      CLAWBACK_NOT_CLAWBACK_ENABLED = -2,
//      CLAWBACK_NO_TRUST = -3,
//      CLAWBACK_UNDERFUNDED = -4
//  };

//  ===========================================================================
public enum ClawbackResultCode implements XdrElement {
  CLAWBACK_SUCCESS(0),
  CLAWBACK_MALFORMED(-1),
  CLAWBACK_NOT_CLAWBACK_ENABLED(-2),
  CLAWBACK_NO_TRUST(-3),
  CLAWBACK_UNDERFUNDED(-4),
  ;
  private int mValue;

  ClawbackResultCode(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static ClawbackResultCode decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return CLAWBACK_SUCCESS;
      case -1:
        return CLAWBACK_MALFORMED;
      case -2:
        return CLAWBACK_NOT_CLAWBACK_ENABLED;
      case -3:
        return CLAWBACK_NO_TRUST;
      case -4:
        return CLAWBACK_UNDERFUNDED;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, ClawbackResultCode value)
      throws IOException {
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

  public static ClawbackResultCode fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ClawbackResultCode fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
