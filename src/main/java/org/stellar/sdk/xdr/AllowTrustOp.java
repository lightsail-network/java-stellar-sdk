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
 * AllowTrustOp's original definition in the XDR file is:
 *
 * <pre>
 * struct AllowTrustOp
 * {
 *     AccountID trustor;
 *     AssetCode asset;
 *
 *     // One of 0, AUTHORIZED_FLAG, or AUTHORIZED_TO_MAINTAIN_LIABILITIES_FLAG
 *     uint32 authorize;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AllowTrustOp implements XdrElement {
  private AccountID trustor;
  private AssetCode asset;
  private Uint32 authorize;

  public void encode(XdrDataOutputStream stream) throws IOException {
    trustor.encode(stream);
    asset.encode(stream);
    authorize.encode(stream);
  }

  public static AllowTrustOp decode(XdrDataInputStream stream) throws IOException {
    AllowTrustOp decodedAllowTrustOp = new AllowTrustOp();
    decodedAllowTrustOp.trustor = AccountID.decode(stream);
    decodedAllowTrustOp.asset = AssetCode.decode(stream);
    decodedAllowTrustOp.authorize = Uint32.decode(stream);
    return decodedAllowTrustOp;
  }

  public static AllowTrustOp fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static AllowTrustOp fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
