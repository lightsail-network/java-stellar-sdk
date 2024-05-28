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
 * ExtendFootprintTTLResult's original definition in the XDR file is:
 *
 * <pre>
 * union ExtendFootprintTTLResult switch (ExtendFootprintTTLResultCode code)
 * {
 * case EXTEND_FOOTPRINT_TTL_SUCCESS:
 *     void;
 * case EXTEND_FOOTPRINT_TTL_MALFORMED:
 * case EXTEND_FOOTPRINT_TTL_RESOURCE_LIMIT_EXCEEDED:
 * case EXTEND_FOOTPRINT_TTL_INSUFFICIENT_REFUNDABLE_FEE:
 *     void;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ExtendFootprintTTLResult implements XdrElement {
  private ExtendFootprintTTLResultCode discriminant;

  public static void encode(
      XdrDataOutputStream stream, ExtendFootprintTTLResult encodedExtendFootprintTTLResult)
      throws IOException {
    // Xdrgen::AST::Identifier
    // ExtendFootprintTTLResultCode
    stream.writeInt(encodedExtendFootprintTTLResult.getDiscriminant().getValue());
    switch (encodedExtendFootprintTTLResult.getDiscriminant()) {
      case EXTEND_FOOTPRINT_TTL_SUCCESS:
        break;
      case EXTEND_FOOTPRINT_TTL_MALFORMED:
      case EXTEND_FOOTPRINT_TTL_RESOURCE_LIMIT_EXCEEDED:
      case EXTEND_FOOTPRINT_TTL_INSUFFICIENT_REFUNDABLE_FEE:
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ExtendFootprintTTLResult decode(XdrDataInputStream stream) throws IOException {
    ExtendFootprintTTLResult decodedExtendFootprintTTLResult = new ExtendFootprintTTLResult();
    ExtendFootprintTTLResultCode discriminant = ExtendFootprintTTLResultCode.decode(stream);
    decodedExtendFootprintTTLResult.setDiscriminant(discriminant);
    switch (decodedExtendFootprintTTLResult.getDiscriminant()) {
      case EXTEND_FOOTPRINT_TTL_SUCCESS:
        break;
      case EXTEND_FOOTPRINT_TTL_MALFORMED:
      case EXTEND_FOOTPRINT_TTL_RESOURCE_LIMIT_EXCEEDED:
      case EXTEND_FOOTPRINT_TTL_INSUFFICIENT_REFUNDABLE_FEE:
        break;
    }
    return decodedExtendFootprintTTLResult;
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

  public static ExtendFootprintTTLResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ExtendFootprintTTLResult fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
