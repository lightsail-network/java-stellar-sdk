// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

// === xdr source ============================================================

//  enum RevokeSponsorshipType
//  {
//      REVOKE_SPONSORSHIP_LEDGER_ENTRY = 0,
//      REVOKE_SPONSORSHIP_SIGNER = 1
//  };

//  ===========================================================================
public enum RevokeSponsorshipType implements XdrElement {
  REVOKE_SPONSORSHIP_LEDGER_ENTRY(0),
  REVOKE_SPONSORSHIP_SIGNER(1),
  ;
  private int mValue;

  RevokeSponsorshipType(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static RevokeSponsorshipType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return REVOKE_SPONSORSHIP_LEDGER_ENTRY;
      case 1:
        return REVOKE_SPONSORSHIP_SIGNER;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, RevokeSponsorshipType value)
      throws IOException {
    stream.writeInt(value.getValue());
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.getEncoder().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static RevokeSponsorshipType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static RevokeSponsorshipType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
