// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.stellar.sdk.Base64Factory;

// === xdr source ============================================================

//  union ClawbackResult switch (ClawbackResultCode code)
//  {
//  case CLAWBACK_SUCCESS:
//      void;
//  case CLAWBACK_MALFORMED:
//  case CLAWBACK_NOT_CLAWBACK_ENABLED:
//  case CLAWBACK_NO_TRUST:
//  case CLAWBACK_UNDERFUNDED:
//      void;
//  };

//  ===========================================================================
public class ClawbackResult implements XdrElement {
  public ClawbackResult() {}

  ClawbackResultCode code;

  public ClawbackResultCode getDiscriminant() {
    return this.code;
  }

  public void setDiscriminant(ClawbackResultCode value) {
    this.code = value;
  }

  public static final class Builder {
    private ClawbackResultCode discriminant;

    public Builder discriminant(ClawbackResultCode discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public ClawbackResult build() {
      ClawbackResult val = new ClawbackResult();
      val.setDiscriminant(discriminant);
      return val;
    }
  }

  public static void encode(XdrDataOutputStream stream, ClawbackResult encodedClawbackResult)
      throws IOException {
    // Xdrgen::AST::Identifier
    // ClawbackResultCode
    stream.writeInt(encodedClawbackResult.getDiscriminant().getValue());
    switch (encodedClawbackResult.getDiscriminant()) {
      case CLAWBACK_SUCCESS:
        break;
      case CLAWBACK_MALFORMED:
      case CLAWBACK_NOT_CLAWBACK_ENABLED:
      case CLAWBACK_NO_TRUST:
      case CLAWBACK_UNDERFUNDED:
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ClawbackResult decode(XdrDataInputStream stream) throws IOException {
    ClawbackResult decodedClawbackResult = new ClawbackResult();
    ClawbackResultCode discriminant = ClawbackResultCode.decode(stream);
    decodedClawbackResult.setDiscriminant(discriminant);
    switch (decodedClawbackResult.getDiscriminant()) {
      case CLAWBACK_SUCCESS:
        break;
      case CLAWBACK_MALFORMED:
      case CLAWBACK_NOT_CLAWBACK_ENABLED:
      case CLAWBACK_NO_TRUST:
      case CLAWBACK_UNDERFUNDED:
        break;
    }
    return decodedClawbackResult;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.code);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ClawbackResult)) {
      return false;
    }

    ClawbackResult other = (ClawbackResult) object;
    return Objects.equals(this.code, other.code);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64Factory.getInstance().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static ClawbackResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ClawbackResult fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
