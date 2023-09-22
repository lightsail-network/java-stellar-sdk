// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  enum RevokeSponsorshipResultCode
//  {
//      // codes considered as "success" for the operation
//      REVOKE_SPONSORSHIP_SUCCESS = 0,
//
//      // codes considered as "failure" for the operation
//      REVOKE_SPONSORSHIP_DOES_NOT_EXIST = -1,
//      REVOKE_SPONSORSHIP_NOT_SPONSOR = -2,
//      REVOKE_SPONSORSHIP_LOW_RESERVE = -3,
//      REVOKE_SPONSORSHIP_ONLY_TRANSFERABLE = -4,
//      REVOKE_SPONSORSHIP_MALFORMED = -5
//  };

//  ===========================================================================
public enum RevokeSponsorshipResultCode implements XdrElement {
  REVOKE_SPONSORSHIP_SUCCESS(0),
  REVOKE_SPONSORSHIP_DOES_NOT_EXIST(-1),
  REVOKE_SPONSORSHIP_NOT_SPONSOR(-2),
  REVOKE_SPONSORSHIP_LOW_RESERVE(-3),
  REVOKE_SPONSORSHIP_ONLY_TRANSFERABLE(-4),
  REVOKE_SPONSORSHIP_MALFORMED(-5),
  ;
  private int mValue;

  RevokeSponsorshipResultCode(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static RevokeSponsorshipResultCode decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return REVOKE_SPONSORSHIP_SUCCESS;
      case -1:
        return REVOKE_SPONSORSHIP_DOES_NOT_EXIST;
      case -2:
        return REVOKE_SPONSORSHIP_NOT_SPONSOR;
      case -3:
        return REVOKE_SPONSORSHIP_LOW_RESERVE;
      case -4:
        return REVOKE_SPONSORSHIP_ONLY_TRANSFERABLE;
      case -5:
        return REVOKE_SPONSORSHIP_MALFORMED;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, RevokeSponsorshipResultCode value)
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

  public static RevokeSponsorshipResultCode fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static RevokeSponsorshipResultCode fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
