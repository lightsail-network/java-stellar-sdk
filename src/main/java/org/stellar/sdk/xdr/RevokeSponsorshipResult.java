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
 * RevokeSponsorshipResult's original definition in the XDR file is:
 *
 * <pre>
 * union RevokeSponsorshipResult switch (RevokeSponsorshipResultCode code)
 * {
 * case REVOKE_SPONSORSHIP_SUCCESS:
 *     void;
 * case REVOKE_SPONSORSHIP_DOES_NOT_EXIST:
 * case REVOKE_SPONSORSHIP_NOT_SPONSOR:
 * case REVOKE_SPONSORSHIP_LOW_RESERVE:
 * case REVOKE_SPONSORSHIP_ONLY_TRANSFERABLE:
 * case REVOKE_SPONSORSHIP_MALFORMED:
 *     void;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RevokeSponsorshipResult implements XdrElement {
  private RevokeSponsorshipResultCode discriminant;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(discriminant.getValue());
    switch (discriminant) {
      case REVOKE_SPONSORSHIP_SUCCESS:
        break;
      case REVOKE_SPONSORSHIP_DOES_NOT_EXIST:
      case REVOKE_SPONSORSHIP_NOT_SPONSOR:
      case REVOKE_SPONSORSHIP_LOW_RESERVE:
      case REVOKE_SPONSORSHIP_ONLY_TRANSFERABLE:
      case REVOKE_SPONSORSHIP_MALFORMED:
        break;
    }
  }

  public static RevokeSponsorshipResult decode(XdrDataInputStream stream) throws IOException {
    RevokeSponsorshipResult decodedRevokeSponsorshipResult = new RevokeSponsorshipResult();
    RevokeSponsorshipResultCode discriminant = RevokeSponsorshipResultCode.decode(stream);
    decodedRevokeSponsorshipResult.setDiscriminant(discriminant);
    switch (decodedRevokeSponsorshipResult.getDiscriminant()) {
      case REVOKE_SPONSORSHIP_SUCCESS:
        break;
      case REVOKE_SPONSORSHIP_DOES_NOT_EXIST:
      case REVOKE_SPONSORSHIP_NOT_SPONSOR:
      case REVOKE_SPONSORSHIP_LOW_RESERVE:
      case REVOKE_SPONSORSHIP_ONLY_TRANSFERABLE:
      case REVOKE_SPONSORSHIP_MALFORMED:
        break;
    }
    return decodedRevokeSponsorshipResult;
  }

  public static RevokeSponsorshipResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static RevokeSponsorshipResult fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
