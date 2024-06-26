// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * SignedSurveyRequestMessage's original definition in the XDR file is:
 *
 * <pre>
 * struct SignedSurveyRequestMessage
 * {
 *     Signature requestSignature;
 *     SurveyRequestMessage request;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SignedSurveyRequestMessage implements XdrElement {
  private Signature requestSignature;
  private SurveyRequestMessage request;

  public void encode(XdrDataOutputStream stream) throws IOException {
    requestSignature.encode(stream);
    request.encode(stream);
  }

  public static SignedSurveyRequestMessage decode(XdrDataInputStream stream) throws IOException {
    SignedSurveyRequestMessage decodedSignedSurveyRequestMessage = new SignedSurveyRequestMessage();
    decodedSignedSurveyRequestMessage.requestSignature = Signature.decode(stream);
    decodedSignedSurveyRequestMessage.request = SurveyRequestMessage.decode(stream);
    return decodedSignedSurveyRequestMessage;
  }

  public static SignedSurveyRequestMessage fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SignedSurveyRequestMessage fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
