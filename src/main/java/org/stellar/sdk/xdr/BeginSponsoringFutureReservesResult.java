// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  union BeginSponsoringFutureReservesResult switch (
//      BeginSponsoringFutureReservesResultCode code)
//  {
//  case BEGIN_SPONSORING_FUTURE_RESERVES_SUCCESS:
//      void;
//  case BEGIN_SPONSORING_FUTURE_RESERVES_MALFORMED:
//  case BEGIN_SPONSORING_FUTURE_RESERVES_ALREADY_SPONSORED:
//  case BEGIN_SPONSORING_FUTURE_RESERVES_RECURSIVE:
//      void;
//  };

//  ===========================================================================
public class BeginSponsoringFutureReservesResult implements XdrElement {
  public BeginSponsoringFutureReservesResult() {}

  BeginSponsoringFutureReservesResultCode code;

  public BeginSponsoringFutureReservesResultCode getDiscriminant() {
    return this.code;
  }

  public void setDiscriminant(BeginSponsoringFutureReservesResultCode value) {
    this.code = value;
  }

  public static final class Builder {
    private BeginSponsoringFutureReservesResultCode discriminant;

    public Builder discriminant(BeginSponsoringFutureReservesResultCode discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public BeginSponsoringFutureReservesResult build() {
      BeginSponsoringFutureReservesResult val = new BeginSponsoringFutureReservesResult();
      val.setDiscriminant(discriminant);
      return val;
    }
  }

  public static void encode(
      XdrDataOutputStream stream,
      BeginSponsoringFutureReservesResult encodedBeginSponsoringFutureReservesResult)
      throws IOException {
    // Xdrgen::AST::Identifier
    // BeginSponsoringFutureReservesResultCode
    stream.writeInt(encodedBeginSponsoringFutureReservesResult.getDiscriminant().getValue());
    switch (encodedBeginSponsoringFutureReservesResult.getDiscriminant()) {
      case BEGIN_SPONSORING_FUTURE_RESERVES_SUCCESS:
        break;
      case BEGIN_SPONSORING_FUTURE_RESERVES_MALFORMED:
      case BEGIN_SPONSORING_FUTURE_RESERVES_ALREADY_SPONSORED:
      case BEGIN_SPONSORING_FUTURE_RESERVES_RECURSIVE:
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static BeginSponsoringFutureReservesResult decode(XdrDataInputStream stream)
      throws IOException {
    BeginSponsoringFutureReservesResult decodedBeginSponsoringFutureReservesResult =
        new BeginSponsoringFutureReservesResult();
    BeginSponsoringFutureReservesResultCode discriminant =
        BeginSponsoringFutureReservesResultCode.decode(stream);
    decodedBeginSponsoringFutureReservesResult.setDiscriminant(discriminant);
    switch (decodedBeginSponsoringFutureReservesResult.getDiscriminant()) {
      case BEGIN_SPONSORING_FUTURE_RESERVES_SUCCESS:
        break;
      case BEGIN_SPONSORING_FUTURE_RESERVES_MALFORMED:
      case BEGIN_SPONSORING_FUTURE_RESERVES_ALREADY_SPONSORED:
      case BEGIN_SPONSORING_FUTURE_RESERVES_RECURSIVE:
        break;
    }
    return decodedBeginSponsoringFutureReservesResult;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.code);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof BeginSponsoringFutureReservesResult)) {
      return false;
    }

    BeginSponsoringFutureReservesResult other = (BeginSponsoringFutureReservesResult) object;
    return Objects.equals(this.code, other.code);
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

  public static BeginSponsoringFutureReservesResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static BeginSponsoringFutureReservesResult fromXdrByteArray(byte[] xdr)
      throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
