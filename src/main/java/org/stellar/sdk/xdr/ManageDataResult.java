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
 * ManageDataResult's original definition in the XDR file is:
 *
 * <pre>
 * union ManageDataResult switch (ManageDataResultCode code)
 * {
 * case MANAGE_DATA_SUCCESS:
 *     void;
 * case MANAGE_DATA_NOT_SUPPORTED_YET:
 * case MANAGE_DATA_NAME_NOT_FOUND:
 * case MANAGE_DATA_LOW_RESERVE:
 * case MANAGE_DATA_INVALID_NAME:
 *     void;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ManageDataResult implements XdrElement {
  private ManageDataResultCode discriminant;

  public static void encode(XdrDataOutputStream stream, ManageDataResult encodedManageDataResult)
      throws IOException {
    // Xdrgen::AST::Identifier
    // ManageDataResultCode
    stream.writeInt(encodedManageDataResult.getDiscriminant().getValue());
    switch (encodedManageDataResult.getDiscriminant()) {
      case MANAGE_DATA_SUCCESS:
        break;
      case MANAGE_DATA_NOT_SUPPORTED_YET:
      case MANAGE_DATA_NAME_NOT_FOUND:
      case MANAGE_DATA_LOW_RESERVE:
      case MANAGE_DATA_INVALID_NAME:
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ManageDataResult decode(XdrDataInputStream stream) throws IOException {
    ManageDataResult decodedManageDataResult = new ManageDataResult();
    ManageDataResultCode discriminant = ManageDataResultCode.decode(stream);
    decodedManageDataResult.setDiscriminant(discriminant);
    switch (decodedManageDataResult.getDiscriminant()) {
      case MANAGE_DATA_SUCCESS:
        break;
      case MANAGE_DATA_NOT_SUPPORTED_YET:
      case MANAGE_DATA_NAME_NOT_FOUND:
      case MANAGE_DATA_LOW_RESERVE:
      case MANAGE_DATA_INVALID_NAME:
        break;
    }
    return decodedManageDataResult;
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

  public static ManageDataResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ManageDataResult fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
