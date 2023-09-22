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

//  union CreateAccountResult switch (CreateAccountResultCode code)
//  {
//  case CREATE_ACCOUNT_SUCCESS:
//      void;
//  case CREATE_ACCOUNT_MALFORMED:
//  case CREATE_ACCOUNT_UNDERFUNDED:
//  case CREATE_ACCOUNT_LOW_RESERVE:
//  case CREATE_ACCOUNT_ALREADY_EXIST:
//      void;
//  };

//  ===========================================================================
public class CreateAccountResult implements XdrElement {
  public CreateAccountResult() {}

  CreateAccountResultCode code;

  public CreateAccountResultCode getDiscriminant() {
    return this.code;
  }

  public void setDiscriminant(CreateAccountResultCode value) {
    this.code = value;
  }

  public static final class Builder {
    private CreateAccountResultCode discriminant;

    public Builder discriminant(CreateAccountResultCode discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public CreateAccountResult build() {
      CreateAccountResult val = new CreateAccountResult();
      val.setDiscriminant(discriminant);
      return val;
    }
  }

  public static void encode(
      XdrDataOutputStream stream, CreateAccountResult encodedCreateAccountResult)
      throws IOException {
    // Xdrgen::AST::Identifier
    // CreateAccountResultCode
    stream.writeInt(encodedCreateAccountResult.getDiscriminant().getValue());
    switch (encodedCreateAccountResult.getDiscriminant()) {
      case CREATE_ACCOUNT_SUCCESS:
        break;
      case CREATE_ACCOUNT_MALFORMED:
      case CREATE_ACCOUNT_UNDERFUNDED:
      case CREATE_ACCOUNT_LOW_RESERVE:
      case CREATE_ACCOUNT_ALREADY_EXIST:
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static CreateAccountResult decode(XdrDataInputStream stream) throws IOException {
    CreateAccountResult decodedCreateAccountResult = new CreateAccountResult();
    CreateAccountResultCode discriminant = CreateAccountResultCode.decode(stream);
    decodedCreateAccountResult.setDiscriminant(discriminant);
    switch (decodedCreateAccountResult.getDiscriminant()) {
      case CREATE_ACCOUNT_SUCCESS:
        break;
      case CREATE_ACCOUNT_MALFORMED:
      case CREATE_ACCOUNT_UNDERFUNDED:
      case CREATE_ACCOUNT_LOW_RESERVE:
      case CREATE_ACCOUNT_ALREADY_EXIST:
        break;
    }
    return decodedCreateAccountResult;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.code);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof CreateAccountResult)) {
      return false;
    }

    CreateAccountResult other = (CreateAccountResult) object;
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

  public static CreateAccountResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static CreateAccountResult fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
