// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * PaymentResult's original definition in the XDR file is:
 *
 * <pre>
 * union PaymentResult switch (PaymentResultCode code)
 * {
 * case PAYMENT_SUCCESS:
 *     void;
 * case PAYMENT_MALFORMED:
 * case PAYMENT_UNDERFUNDED:
 * case PAYMENT_SRC_NO_TRUST:
 * case PAYMENT_SRC_NOT_AUTHORIZED:
 * case PAYMENT_NO_DESTINATION:
 * case PAYMENT_NO_TRUST:
 * case PAYMENT_NOT_AUTHORIZED:
 * case PAYMENT_LINE_FULL:
 * case PAYMENT_NO_ISSUER:
 *     void;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PaymentResult implements XdrElement {
  private PaymentResultCode discriminant;

  public static void encode(XdrDataOutputStream stream, PaymentResult encodedPaymentResult)
      throws IOException {
    // Xdrgen::AST::Identifier
    // PaymentResultCode
    stream.writeInt(encodedPaymentResult.getDiscriminant().getValue());
    switch (encodedPaymentResult.getDiscriminant()) {
      case PAYMENT_SUCCESS:
        break;
      case PAYMENT_MALFORMED:
      case PAYMENT_UNDERFUNDED:
      case PAYMENT_SRC_NO_TRUST:
      case PAYMENT_SRC_NOT_AUTHORIZED:
      case PAYMENT_NO_DESTINATION:
      case PAYMENT_NO_TRUST:
      case PAYMENT_NOT_AUTHORIZED:
      case PAYMENT_LINE_FULL:
      case PAYMENT_NO_ISSUER:
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static PaymentResult decode(XdrDataInputStream stream) throws IOException {
    PaymentResult decodedPaymentResult = new PaymentResult();
    PaymentResultCode discriminant = PaymentResultCode.decode(stream);
    decodedPaymentResult.setDiscriminant(discriminant);
    switch (decodedPaymentResult.getDiscriminant()) {
      case PAYMENT_SUCCESS:
        break;
      case PAYMENT_MALFORMED:
      case PAYMENT_UNDERFUNDED:
      case PAYMENT_SRC_NO_TRUST:
      case PAYMENT_SRC_NOT_AUTHORIZED:
      case PAYMENT_NO_DESTINATION:
      case PAYMENT_NO_TRUST:
      case PAYMENT_NOT_AUTHORIZED:
      case PAYMENT_LINE_FULL:
      case PAYMENT_NO_ISSUER:
        break;
    }
    return decodedPaymentResult;
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

  public static PaymentResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static PaymentResult fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
