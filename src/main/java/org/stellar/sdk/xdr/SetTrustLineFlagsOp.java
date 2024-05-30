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
 * SetTrustLineFlagsOp's original definition in the XDR file is:
 *
 * <pre>
 * struct SetTrustLineFlagsOp
 * {
 *     AccountID trustor;
 *     Asset asset;
 *
 *     uint32 clearFlags; // which flags to clear
 *     uint32 setFlags;   // which flags to set
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SetTrustLineFlagsOp implements XdrElement {
  private AccountID trustor;
  private Asset asset;
  private Uint32 clearFlags;
  private Uint32 setFlags;

  public void encode(XdrDataOutputStream stream) throws IOException {
    trustor.encode(stream);
    asset.encode(stream);
    clearFlags.encode(stream);
    setFlags.encode(stream);
  }

  public static SetTrustLineFlagsOp decode(XdrDataInputStream stream) throws IOException {
    SetTrustLineFlagsOp decodedSetTrustLineFlagsOp = new SetTrustLineFlagsOp();
    decodedSetTrustLineFlagsOp.trustor = AccountID.decode(stream);
    decodedSetTrustLineFlagsOp.asset = Asset.decode(stream);
    decodedSetTrustLineFlagsOp.clearFlags = Uint32.decode(stream);
    decodedSetTrustLineFlagsOp.setFlags = Uint32.decode(stream);
    return decodedSetTrustLineFlagsOp;
  }

  public static SetTrustLineFlagsOp fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SetTrustLineFlagsOp fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
