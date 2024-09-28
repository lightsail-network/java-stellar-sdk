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
 * SignedTimeSlicedSurveyStartCollectingMessage's original definition in the XDR file is:
 *
 * <pre>
 * struct SignedTimeSlicedSurveyStartCollectingMessage
 * {
 *     Signature signature;
 *     TimeSlicedSurveyStartCollectingMessage startCollecting;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SignedTimeSlicedSurveyStartCollectingMessage implements XdrElement {
  private Signature signature;
  private TimeSlicedSurveyStartCollectingMessage startCollecting;

  public void encode(XdrDataOutputStream stream) throws IOException {
    signature.encode(stream);
    startCollecting.encode(stream);
  }

  public static SignedTimeSlicedSurveyStartCollectingMessage decode(XdrDataInputStream stream)
      throws IOException {
    SignedTimeSlicedSurveyStartCollectingMessage
        decodedSignedTimeSlicedSurveyStartCollectingMessage =
            new SignedTimeSlicedSurveyStartCollectingMessage();
    decodedSignedTimeSlicedSurveyStartCollectingMessage.signature = Signature.decode(stream);
    decodedSignedTimeSlicedSurveyStartCollectingMessage.startCollecting =
        TimeSlicedSurveyStartCollectingMessage.decode(stream);
    return decodedSignedTimeSlicedSurveyStartCollectingMessage;
  }

  public static SignedTimeSlicedSurveyStartCollectingMessage fromXdrBase64(String xdr)
      throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SignedTimeSlicedSurveyStartCollectingMessage fromXdrByteArray(byte[] xdr)
      throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}