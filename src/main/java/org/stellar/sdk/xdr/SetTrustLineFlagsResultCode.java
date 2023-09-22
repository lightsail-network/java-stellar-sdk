// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  enum SetTrustLineFlagsResultCode
//  {
//      // codes considered as "success" for the operation
//      SET_TRUST_LINE_FLAGS_SUCCESS = 0,
//
//      // codes considered as "failure" for the operation
//      SET_TRUST_LINE_FLAGS_MALFORMED = -1,
//      SET_TRUST_LINE_FLAGS_NO_TRUST_LINE = -2,
//      SET_TRUST_LINE_FLAGS_CANT_REVOKE = -3,
//      SET_TRUST_LINE_FLAGS_INVALID_STATE = -4,
//      SET_TRUST_LINE_FLAGS_LOW_RESERVE = -5 // claimable balances can't be created
//                                            // on revoke due to low reserves
//  };

//  ===========================================================================
public enum SetTrustLineFlagsResultCode implements XdrElement {
  SET_TRUST_LINE_FLAGS_SUCCESS(0),
  SET_TRUST_LINE_FLAGS_MALFORMED(-1),
  SET_TRUST_LINE_FLAGS_NO_TRUST_LINE(-2),
  SET_TRUST_LINE_FLAGS_CANT_REVOKE(-3),
  SET_TRUST_LINE_FLAGS_INVALID_STATE(-4),
  SET_TRUST_LINE_FLAGS_LOW_RESERVE(-5),
  ;
  private int mValue;

  SetTrustLineFlagsResultCode(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static SetTrustLineFlagsResultCode decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return SET_TRUST_LINE_FLAGS_SUCCESS;
      case -1:
        return SET_TRUST_LINE_FLAGS_MALFORMED;
      case -2:
        return SET_TRUST_LINE_FLAGS_NO_TRUST_LINE;
      case -3:
        return SET_TRUST_LINE_FLAGS_CANT_REVOKE;
      case -4:
        return SET_TRUST_LINE_FLAGS_INVALID_STATE;
      case -5:
        return SET_TRUST_LINE_FLAGS_LOW_RESERVE;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, SetTrustLineFlagsResultCode value)
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

  public static SetTrustLineFlagsResultCode fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SetTrustLineFlagsResultCode fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
