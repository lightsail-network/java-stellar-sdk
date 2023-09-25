// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

// === xdr source ============================================================

//  enum ManageDataResultCode
//  {
//      // codes considered as "success" for the operation
//      MANAGE_DATA_SUCCESS = 0,
//      // codes considered as "failure" for the operation
//      MANAGE_DATA_NOT_SUPPORTED_YET =
//          -1, // The network hasn't moved to this protocol change yet
//      MANAGE_DATA_NAME_NOT_FOUND =
//          -2, // Trying to remove a Data Entry that isn't there
//      MANAGE_DATA_LOW_RESERVE = -3, // not enough funds to create a new Data Entry
//      MANAGE_DATA_INVALID_NAME = -4 // Name not a valid string
//  };

//  ===========================================================================
public enum ManageDataResultCode implements XdrElement {
  MANAGE_DATA_SUCCESS(0),
  MANAGE_DATA_NOT_SUPPORTED_YET(-1),
  MANAGE_DATA_NAME_NOT_FOUND(-2),
  MANAGE_DATA_LOW_RESERVE(-3),
  MANAGE_DATA_INVALID_NAME(-4),
  ;
  private int mValue;

  ManageDataResultCode(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static ManageDataResultCode decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return MANAGE_DATA_SUCCESS;
      case -1:
        return MANAGE_DATA_NOT_SUPPORTED_YET;
      case -2:
        return MANAGE_DATA_NAME_NOT_FOUND;
      case -3:
        return MANAGE_DATA_LOW_RESERVE;
      case -4:
        return MANAGE_DATA_INVALID_NAME;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, ManageDataResultCode value)
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

  public static ManageDataResultCode fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ManageDataResultCode fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
