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
 * SetOptionsResult's original definition in the XDR file is:
 *
 * <pre>
 * union SetOptionsResult switch (SetOptionsResultCode code)
 * {
 * case SET_OPTIONS_SUCCESS:
 *     void;
 * case SET_OPTIONS_LOW_RESERVE:
 * case SET_OPTIONS_TOO_MANY_SIGNERS:
 * case SET_OPTIONS_BAD_FLAGS:
 * case SET_OPTIONS_INVALID_INFLATION:
 * case SET_OPTIONS_CANT_CHANGE:
 * case SET_OPTIONS_UNKNOWN_FLAG:
 * case SET_OPTIONS_THRESHOLD_OUT_OF_RANGE:
 * case SET_OPTIONS_BAD_SIGNER:
 * case SET_OPTIONS_INVALID_HOME_DOMAIN:
 * case SET_OPTIONS_AUTH_REVOCABLE_REQUIRED:
 *     void;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SetOptionsResult implements XdrElement {
  private SetOptionsResultCode discriminant;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(discriminant.getValue());
    switch (discriminant) {
      case SET_OPTIONS_SUCCESS:
        break;
      case SET_OPTIONS_LOW_RESERVE:
      case SET_OPTIONS_TOO_MANY_SIGNERS:
      case SET_OPTIONS_BAD_FLAGS:
      case SET_OPTIONS_INVALID_INFLATION:
      case SET_OPTIONS_CANT_CHANGE:
      case SET_OPTIONS_UNKNOWN_FLAG:
      case SET_OPTIONS_THRESHOLD_OUT_OF_RANGE:
      case SET_OPTIONS_BAD_SIGNER:
      case SET_OPTIONS_INVALID_HOME_DOMAIN:
      case SET_OPTIONS_AUTH_REVOCABLE_REQUIRED:
        break;
    }
  }

  public static SetOptionsResult decode(XdrDataInputStream stream) throws IOException {
    SetOptionsResult decodedSetOptionsResult = new SetOptionsResult();
    SetOptionsResultCode discriminant = SetOptionsResultCode.decode(stream);
    decodedSetOptionsResult.setDiscriminant(discriminant);
    switch (decodedSetOptionsResult.getDiscriminant()) {
      case SET_OPTIONS_SUCCESS:
        break;
      case SET_OPTIONS_LOW_RESERVE:
      case SET_OPTIONS_TOO_MANY_SIGNERS:
      case SET_OPTIONS_BAD_FLAGS:
      case SET_OPTIONS_INVALID_INFLATION:
      case SET_OPTIONS_CANT_CHANGE:
      case SET_OPTIONS_UNKNOWN_FLAG:
      case SET_OPTIONS_THRESHOLD_OUT_OF_RANGE:
      case SET_OPTIONS_BAD_SIGNER:
      case SET_OPTIONS_INVALID_HOME_DOMAIN:
      case SET_OPTIONS_AUTH_REVOCABLE_REQUIRED:
        break;
    }
    return decodedSetOptionsResult;
  }

  public static SetOptionsResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SetOptionsResult fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
