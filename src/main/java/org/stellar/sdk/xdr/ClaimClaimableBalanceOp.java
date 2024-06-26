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
 * ClaimClaimableBalanceOp's original definition in the XDR file is:
 *
 * <pre>
 * struct ClaimClaimableBalanceOp
 * {
 *     ClaimableBalanceID balanceID;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClaimClaimableBalanceOp implements XdrElement {
  private ClaimableBalanceID balanceID;

  public void encode(XdrDataOutputStream stream) throws IOException {
    balanceID.encode(stream);
  }

  public static ClaimClaimableBalanceOp decode(XdrDataInputStream stream) throws IOException {
    ClaimClaimableBalanceOp decodedClaimClaimableBalanceOp = new ClaimClaimableBalanceOp();
    decodedClaimClaimableBalanceOp.balanceID = ClaimableBalanceID.decode(stream);
    return decodedClaimClaimableBalanceOp;
  }

  public static ClaimClaimableBalanceOp fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ClaimClaimableBalanceOp fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
