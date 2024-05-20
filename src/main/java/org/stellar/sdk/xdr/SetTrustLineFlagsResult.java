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
 * SetTrustLineFlagsResult's original definition in the XDR file is:
 *
 * <pre>
 * union SetTrustLineFlagsResult switch (SetTrustLineFlagsResultCode code)
 * {
 * case SET_TRUST_LINE_FLAGS_SUCCESS:
 *     void;
 * case SET_TRUST_LINE_FLAGS_MALFORMED:
 * case SET_TRUST_LINE_FLAGS_NO_TRUST_LINE:
 * case SET_TRUST_LINE_FLAGS_CANT_REVOKE:
 * case SET_TRUST_LINE_FLAGS_INVALID_STATE:
 * case SET_TRUST_LINE_FLAGS_LOW_RESERVE:
 *     void;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SetTrustLineFlagsResult implements XdrElement {
  private SetTrustLineFlagsResultCode discriminant;

  public static void encode(
      XdrDataOutputStream stream, SetTrustLineFlagsResult encodedSetTrustLineFlagsResult)
      throws IOException {
    // Xdrgen::AST::Identifier
    // SetTrustLineFlagsResultCode
    stream.writeInt(encodedSetTrustLineFlagsResult.getDiscriminant().getValue());
    switch (encodedSetTrustLineFlagsResult.getDiscriminant()) {
      case SET_TRUST_LINE_FLAGS_SUCCESS:
        break;
      case SET_TRUST_LINE_FLAGS_MALFORMED:
      case SET_TRUST_LINE_FLAGS_NO_TRUST_LINE:
      case SET_TRUST_LINE_FLAGS_CANT_REVOKE:
      case SET_TRUST_LINE_FLAGS_INVALID_STATE:
      case SET_TRUST_LINE_FLAGS_LOW_RESERVE:
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SetTrustLineFlagsResult decode(XdrDataInputStream stream) throws IOException {
    SetTrustLineFlagsResult decodedSetTrustLineFlagsResult = new SetTrustLineFlagsResult();
    SetTrustLineFlagsResultCode discriminant = SetTrustLineFlagsResultCode.decode(stream);
    decodedSetTrustLineFlagsResult.setDiscriminant(discriminant);
    switch (decodedSetTrustLineFlagsResult.getDiscriminant()) {
      case SET_TRUST_LINE_FLAGS_SUCCESS:
        break;
      case SET_TRUST_LINE_FLAGS_MALFORMED:
      case SET_TRUST_LINE_FLAGS_NO_TRUST_LINE:
      case SET_TRUST_LINE_FLAGS_CANT_REVOKE:
      case SET_TRUST_LINE_FLAGS_INVALID_STATE:
      case SET_TRUST_LINE_FLAGS_LOW_RESERVE:
        break;
    }
    return decodedSetTrustLineFlagsResult;
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

  public static SetTrustLineFlagsResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SetTrustLineFlagsResult fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
