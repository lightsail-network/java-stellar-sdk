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
 * PathPaymentStrictSendResult's original definition in the XDR file is:
 *
 * <pre>
 * union PathPaymentStrictSendResult switch (PathPaymentStrictSendResultCode code)
 * {
 * case PATH_PAYMENT_STRICT_SEND_SUCCESS:
 *     struct
 *     {
 *         ClaimAtom offers&lt;&gt;;
 *         SimplePaymentResult last;
 *     } success;
 * case PATH_PAYMENT_STRICT_SEND_MALFORMED:
 * case PATH_PAYMENT_STRICT_SEND_UNDERFUNDED:
 * case PATH_PAYMENT_STRICT_SEND_SRC_NO_TRUST:
 * case PATH_PAYMENT_STRICT_SEND_SRC_NOT_AUTHORIZED:
 * case PATH_PAYMENT_STRICT_SEND_NO_DESTINATION:
 * case PATH_PAYMENT_STRICT_SEND_NO_TRUST:
 * case PATH_PAYMENT_STRICT_SEND_NOT_AUTHORIZED:
 * case PATH_PAYMENT_STRICT_SEND_LINE_FULL:
 *     void;
 * case PATH_PAYMENT_STRICT_SEND_NO_ISSUER:
 *     Asset noIssuer; // the asset that caused the error
 * case PATH_PAYMENT_STRICT_SEND_TOO_FEW_OFFERS:
 * case PATH_PAYMENT_STRICT_SEND_OFFER_CROSS_SELF:
 * case PATH_PAYMENT_STRICT_SEND_UNDER_DESTMIN:
 *     void;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PathPaymentStrictSendResult implements XdrElement {
  private PathPaymentStrictSendResultCode discriminant;
  private PathPaymentStrictSendResultSuccess success;
  private Asset noIssuer;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(discriminant.getValue());
    switch (discriminant) {
      case PATH_PAYMENT_STRICT_SEND_SUCCESS:
        success.encode(stream);
        break;
      case PATH_PAYMENT_STRICT_SEND_MALFORMED:
      case PATH_PAYMENT_STRICT_SEND_UNDERFUNDED:
      case PATH_PAYMENT_STRICT_SEND_SRC_NO_TRUST:
      case PATH_PAYMENT_STRICT_SEND_SRC_NOT_AUTHORIZED:
      case PATH_PAYMENT_STRICT_SEND_NO_DESTINATION:
      case PATH_PAYMENT_STRICT_SEND_NO_TRUST:
      case PATH_PAYMENT_STRICT_SEND_NOT_AUTHORIZED:
      case PATH_PAYMENT_STRICT_SEND_LINE_FULL:
        break;
      case PATH_PAYMENT_STRICT_SEND_NO_ISSUER:
        noIssuer.encode(stream);
        break;
      case PATH_PAYMENT_STRICT_SEND_TOO_FEW_OFFERS:
      case PATH_PAYMENT_STRICT_SEND_OFFER_CROSS_SELF:
      case PATH_PAYMENT_STRICT_SEND_UNDER_DESTMIN:
        break;
    }
  }

  public static PathPaymentStrictSendResult decode(XdrDataInputStream stream) throws IOException {
    PathPaymentStrictSendResult decodedPathPaymentStrictSendResult =
        new PathPaymentStrictSendResult();
    PathPaymentStrictSendResultCode discriminant = PathPaymentStrictSendResultCode.decode(stream);
    decodedPathPaymentStrictSendResult.setDiscriminant(discriminant);
    switch (decodedPathPaymentStrictSendResult.getDiscriminant()) {
      case PATH_PAYMENT_STRICT_SEND_SUCCESS:
        decodedPathPaymentStrictSendResult.success =
            PathPaymentStrictSendResultSuccess.decode(stream);
        break;
      case PATH_PAYMENT_STRICT_SEND_MALFORMED:
      case PATH_PAYMENT_STRICT_SEND_UNDERFUNDED:
      case PATH_PAYMENT_STRICT_SEND_SRC_NO_TRUST:
      case PATH_PAYMENT_STRICT_SEND_SRC_NOT_AUTHORIZED:
      case PATH_PAYMENT_STRICT_SEND_NO_DESTINATION:
      case PATH_PAYMENT_STRICT_SEND_NO_TRUST:
      case PATH_PAYMENT_STRICT_SEND_NOT_AUTHORIZED:
      case PATH_PAYMENT_STRICT_SEND_LINE_FULL:
        break;
      case PATH_PAYMENT_STRICT_SEND_NO_ISSUER:
        decodedPathPaymentStrictSendResult.noIssuer = Asset.decode(stream);
        break;
      case PATH_PAYMENT_STRICT_SEND_TOO_FEW_OFFERS:
      case PATH_PAYMENT_STRICT_SEND_OFFER_CROSS_SELF:
      case PATH_PAYMENT_STRICT_SEND_UNDER_DESTMIN:
        break;
    }
    return decodedPathPaymentStrictSendResult;
  }

  public static PathPaymentStrictSendResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static PathPaymentStrictSendResult fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  /**
   * PathPaymentStrictSendResultSuccess's original definition in the XDR file is:
   *
   * <pre>
   * struct
   *     {
   *         ClaimAtom offers&lt;&gt;;
   *         SimplePaymentResult last;
   *     }
   * </pre>
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  public static class PathPaymentStrictSendResultSuccess implements XdrElement {
    private ClaimAtom[] offers;
    private SimplePaymentResult last;

    public void encode(XdrDataOutputStream stream) throws IOException {
      int offersSize = getOffers().length;
      stream.writeInt(offersSize);
      for (int i = 0; i < offersSize; i++) {
        offers[i].encode(stream);
      }
      last.encode(stream);
    }

    public static PathPaymentStrictSendResultSuccess decode(XdrDataInputStream stream)
        throws IOException {
      PathPaymentStrictSendResultSuccess decodedPathPaymentStrictSendResultSuccess =
          new PathPaymentStrictSendResultSuccess();
      int offersSize = stream.readInt();
      decodedPathPaymentStrictSendResultSuccess.offers = new ClaimAtom[offersSize];
      for (int i = 0; i < offersSize; i++) {
        decodedPathPaymentStrictSendResultSuccess.offers[i] = ClaimAtom.decode(stream);
      }
      decodedPathPaymentStrictSendResultSuccess.last = SimplePaymentResult.decode(stream);
      return decodedPathPaymentStrictSendResultSuccess;
    }

    public static PathPaymentStrictSendResultSuccess fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64Factory.getInstance().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static PathPaymentStrictSendResultSuccess fromXdrByteArray(byte[] xdr)
        throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
