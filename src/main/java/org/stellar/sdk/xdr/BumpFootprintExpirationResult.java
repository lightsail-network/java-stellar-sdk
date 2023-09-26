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

//  union BumpFootprintExpirationResult switch (BumpFootprintExpirationResultCode code)
//  {
//  case BUMP_FOOTPRINT_EXPIRATION_SUCCESS:
//      void;
//  case BUMP_FOOTPRINT_EXPIRATION_MALFORMED:
//  case BUMP_FOOTPRINT_EXPIRATION_RESOURCE_LIMIT_EXCEEDED:
//  case BUMP_FOOTPRINT_EXPIRATION_INSUFFICIENT_REFUNDABLE_FEE:
//      void;
//  };

//  ===========================================================================
public class BumpFootprintExpirationResult implements XdrElement {
  public BumpFootprintExpirationResult() {}

  BumpFootprintExpirationResultCode code;

  public BumpFootprintExpirationResultCode getDiscriminant() {
    return this.code;
  }

  public void setDiscriminant(BumpFootprintExpirationResultCode value) {
    this.code = value;
  }

  public static final class Builder {
    private BumpFootprintExpirationResultCode discriminant;

    public Builder discriminant(BumpFootprintExpirationResultCode discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public BumpFootprintExpirationResult build() {
      BumpFootprintExpirationResult val = new BumpFootprintExpirationResult();
      val.setDiscriminant(discriminant);
      return val;
    }
  }

  public static void encode(
      XdrDataOutputStream stream,
      BumpFootprintExpirationResult encodedBumpFootprintExpirationResult)
      throws IOException {
    // Xdrgen::AST::Identifier
    // BumpFootprintExpirationResultCode
    stream.writeInt(encodedBumpFootprintExpirationResult.getDiscriminant().getValue());
    switch (encodedBumpFootprintExpirationResult.getDiscriminant()) {
      case BUMP_FOOTPRINT_EXPIRATION_SUCCESS:
        break;
      case BUMP_FOOTPRINT_EXPIRATION_MALFORMED:
      case BUMP_FOOTPRINT_EXPIRATION_RESOURCE_LIMIT_EXCEEDED:
      case BUMP_FOOTPRINT_EXPIRATION_INSUFFICIENT_REFUNDABLE_FEE:
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static BumpFootprintExpirationResult decode(XdrDataInputStream stream) throws IOException {
    BumpFootprintExpirationResult decodedBumpFootprintExpirationResult =
        new BumpFootprintExpirationResult();
    BumpFootprintExpirationResultCode discriminant =
        BumpFootprintExpirationResultCode.decode(stream);
    decodedBumpFootprintExpirationResult.setDiscriminant(discriminant);
    switch (decodedBumpFootprintExpirationResult.getDiscriminant()) {
      case BUMP_FOOTPRINT_EXPIRATION_SUCCESS:
        break;
      case BUMP_FOOTPRINT_EXPIRATION_MALFORMED:
      case BUMP_FOOTPRINT_EXPIRATION_RESOURCE_LIMIT_EXCEEDED:
      case BUMP_FOOTPRINT_EXPIRATION_INSUFFICIENT_REFUNDABLE_FEE:
        break;
    }
    return decodedBumpFootprintExpirationResult;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.code);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof BumpFootprintExpirationResult)) {
      return false;
    }

    BumpFootprintExpirationResult other = (BumpFootprintExpirationResult) object;
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

  public static BumpFootprintExpirationResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static BumpFootprintExpirationResult fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
