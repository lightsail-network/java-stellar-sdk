// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.stellar.sdk.Base64Factory;

/**
 * AllowTrustResult's original definition in the XDR file is:
 *
 * <pre>
 * union AllowTrustResult switch (AllowTrustResultCode code)
 * {
 * case ALLOW_TRUST_SUCCESS:
 *     void;
 * case ALLOW_TRUST_MALFORMED:
 * case ALLOW_TRUST_NO_TRUST_LINE:
 * case ALLOW_TRUST_TRUST_NOT_REQUIRED:
 * case ALLOW_TRUST_CANT_REVOKE:
 * case ALLOW_TRUST_SELF_NOT_ALLOWED:
 * case ALLOW_TRUST_LOW_RESERVE:
 *     void;
 * };
 * </pre>
 */
public class AllowTrustResult implements XdrElement {
  public AllowTrustResult() {}

  AllowTrustResultCode code;

  public AllowTrustResultCode getDiscriminant() {
    return this.code;
  }

  public void setDiscriminant(AllowTrustResultCode value) {
    this.code = value;
  }

  public static final class Builder {
    private AllowTrustResultCode discriminant;

    public Builder discriminant(AllowTrustResultCode discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public AllowTrustResult build() {
      AllowTrustResult val = new AllowTrustResult();
      val.setDiscriminant(discriminant);
      return val;
    }
  }

  public static void encode(XdrDataOutputStream stream, AllowTrustResult encodedAllowTrustResult)
      throws IOException {
    // Xdrgen::AST::Identifier
    // AllowTrustResultCode
    stream.writeInt(encodedAllowTrustResult.getDiscriminant().getValue());
    switch (encodedAllowTrustResult.getDiscriminant()) {
      case ALLOW_TRUST_SUCCESS:
        break;
      case ALLOW_TRUST_MALFORMED:
      case ALLOW_TRUST_NO_TRUST_LINE:
      case ALLOW_TRUST_TRUST_NOT_REQUIRED:
      case ALLOW_TRUST_CANT_REVOKE:
      case ALLOW_TRUST_SELF_NOT_ALLOWED:
      case ALLOW_TRUST_LOW_RESERVE:
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static AllowTrustResult decode(XdrDataInputStream stream) throws IOException {
    AllowTrustResult decodedAllowTrustResult = new AllowTrustResult();
    AllowTrustResultCode discriminant = AllowTrustResultCode.decode(stream);
    decodedAllowTrustResult.setDiscriminant(discriminant);
    switch (decodedAllowTrustResult.getDiscriminant()) {
      case ALLOW_TRUST_SUCCESS:
        break;
      case ALLOW_TRUST_MALFORMED:
      case ALLOW_TRUST_NO_TRUST_LINE:
      case ALLOW_TRUST_TRUST_NOT_REQUIRED:
      case ALLOW_TRUST_CANT_REVOKE:
      case ALLOW_TRUST_SELF_NOT_ALLOWED:
      case ALLOW_TRUST_LOW_RESERVE:
        break;
    }
    return decodedAllowTrustResult;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.code);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof AllowTrustResult)) {
      return false;
    }

    AllowTrustResult other = (AllowTrustResult) object;
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

  public static AllowTrustResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static AllowTrustResult fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
