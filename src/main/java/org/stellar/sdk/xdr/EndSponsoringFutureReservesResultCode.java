// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  enum EndSponsoringFutureReservesResultCode
//  {
//      // codes considered as "success" for the operation
//      END_SPONSORING_FUTURE_RESERVES_SUCCESS = 0,
//
//      // codes considered as "failure" for the operation
//      END_SPONSORING_FUTURE_RESERVES_NOT_SPONSORED = -1
//  };

//  ===========================================================================
public enum EndSponsoringFutureReservesResultCode implements XdrElement {
  END_SPONSORING_FUTURE_RESERVES_SUCCESS(0),
  END_SPONSORING_FUTURE_RESERVES_NOT_SPONSORED(-1),
  ;
  private int mValue;

  EndSponsoringFutureReservesResultCode(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static EndSponsoringFutureReservesResultCode decode(XdrDataInputStream stream)
      throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return END_SPONSORING_FUTURE_RESERVES_SUCCESS;
      case -1:
        return END_SPONSORING_FUTURE_RESERVES_NOT_SPONSORED;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, EndSponsoringFutureReservesResultCode value)
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

  public static EndSponsoringFutureReservesResultCode fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static EndSponsoringFutureReservesResultCode fromXdrByteArray(byte[] xdr)
      throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
