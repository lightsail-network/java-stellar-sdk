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

//  union ChangeTrustResult switch (ChangeTrustResultCode code)
//  {
//  case CHANGE_TRUST_SUCCESS:
//      void;
//  case CHANGE_TRUST_MALFORMED:
//  case CHANGE_TRUST_NO_ISSUER:
//  case CHANGE_TRUST_INVALID_LIMIT:
//  case CHANGE_TRUST_LOW_RESERVE:
//  case CHANGE_TRUST_SELF_NOT_ALLOWED:
//  case CHANGE_TRUST_TRUST_LINE_MISSING:
//  case CHANGE_TRUST_CANNOT_DELETE:
//  case CHANGE_TRUST_NOT_AUTH_MAINTAIN_LIABILITIES:
//      void;
//  };

//  ===========================================================================
public class ChangeTrustResult implements XdrElement {
  public ChangeTrustResult() {}

  ChangeTrustResultCode code;

  public ChangeTrustResultCode getDiscriminant() {
    return this.code;
  }

  public void setDiscriminant(ChangeTrustResultCode value) {
    this.code = value;
  }

  public static final class Builder {
    private ChangeTrustResultCode discriminant;

    public Builder discriminant(ChangeTrustResultCode discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public ChangeTrustResult build() {
      ChangeTrustResult val = new ChangeTrustResult();
      val.setDiscriminant(discriminant);
      return val;
    }
  }

  public static void encode(XdrDataOutputStream stream, ChangeTrustResult encodedChangeTrustResult)
      throws IOException {
    // Xdrgen::AST::Identifier
    // ChangeTrustResultCode
    stream.writeInt(encodedChangeTrustResult.getDiscriminant().getValue());
    switch (encodedChangeTrustResult.getDiscriminant()) {
      case CHANGE_TRUST_SUCCESS:
        break;
      case CHANGE_TRUST_MALFORMED:
      case CHANGE_TRUST_NO_ISSUER:
      case CHANGE_TRUST_INVALID_LIMIT:
      case CHANGE_TRUST_LOW_RESERVE:
      case CHANGE_TRUST_SELF_NOT_ALLOWED:
      case CHANGE_TRUST_TRUST_LINE_MISSING:
      case CHANGE_TRUST_CANNOT_DELETE:
      case CHANGE_TRUST_NOT_AUTH_MAINTAIN_LIABILITIES:
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ChangeTrustResult decode(XdrDataInputStream stream) throws IOException {
    ChangeTrustResult decodedChangeTrustResult = new ChangeTrustResult();
    ChangeTrustResultCode discriminant = ChangeTrustResultCode.decode(stream);
    decodedChangeTrustResult.setDiscriminant(discriminant);
    switch (decodedChangeTrustResult.getDiscriminant()) {
      case CHANGE_TRUST_SUCCESS:
        break;
      case CHANGE_TRUST_MALFORMED:
      case CHANGE_TRUST_NO_ISSUER:
      case CHANGE_TRUST_INVALID_LIMIT:
      case CHANGE_TRUST_LOW_RESERVE:
      case CHANGE_TRUST_SELF_NOT_ALLOWED:
      case CHANGE_TRUST_TRUST_LINE_MISSING:
      case CHANGE_TRUST_CANNOT_DELETE:
      case CHANGE_TRUST_NOT_AUTH_MAINTAIN_LIABILITIES:
        break;
    }
    return decodedChangeTrustResult;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.code);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ChangeTrustResult)) {
      return false;
    }

    ChangeTrustResult other = (ChangeTrustResult) object;
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

  public static ChangeTrustResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ChangeTrustResult fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
