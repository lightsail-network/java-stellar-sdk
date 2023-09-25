// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

// === xdr source ============================================================

//  struct SignedSurveyRequestMessage
//  {
//      Signature requestSignature;
//      SurveyRequestMessage request;
//  };

//  ===========================================================================
public class SignedSurveyRequestMessage implements XdrElement {
  public SignedSurveyRequestMessage() {}

  private Signature requestSignature;

  public Signature getRequestSignature() {
    return this.requestSignature;
  }

  public void setRequestSignature(Signature value) {
    this.requestSignature = value;
  }

  private SurveyRequestMessage request;

  public SurveyRequestMessage getRequest() {
    return this.request;
  }

  public void setRequest(SurveyRequestMessage value) {
    this.request = value;
  }

  public static void encode(
      XdrDataOutputStream stream, SignedSurveyRequestMessage encodedSignedSurveyRequestMessage)
      throws IOException {
    Signature.encode(stream, encodedSignedSurveyRequestMessage.requestSignature);
    SurveyRequestMessage.encode(stream, encodedSignedSurveyRequestMessage.request);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SignedSurveyRequestMessage decode(XdrDataInputStream stream) throws IOException {
    SignedSurveyRequestMessage decodedSignedSurveyRequestMessage = new SignedSurveyRequestMessage();
    decodedSignedSurveyRequestMessage.requestSignature = Signature.decode(stream);
    decodedSignedSurveyRequestMessage.request = SurveyRequestMessage.decode(stream);
    return decodedSignedSurveyRequestMessage;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.requestSignature, this.request);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SignedSurveyRequestMessage)) {
      return false;
    }

    SignedSurveyRequestMessage other = (SignedSurveyRequestMessage) object;
    return Objects.equals(this.requestSignature, other.requestSignature)
        && Objects.equals(this.request, other.request);
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

  public static SignedSurveyRequestMessage fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SignedSurveyRequestMessage fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private Signature requestSignature;
    private SurveyRequestMessage request;

    public Builder requestSignature(Signature requestSignature) {
      this.requestSignature = requestSignature;
      return this;
    }

    public Builder request(SurveyRequestMessage request) {
      this.request = request;
      return this;
    }

    public SignedSurveyRequestMessage build() {
      SignedSurveyRequestMessage val = new SignedSurveyRequestMessage();
      val.setRequestSignature(this.requestSignature);
      val.setRequest(this.request);
      return val;
    }
  }
}
