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
 * AlphaNum4's original definition in the XDR file is:
 *
 * <pre>
 * struct AlphaNum4
 * {
 *     AssetCode4 assetCode;
 *     AccountID issuer;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AlphaNum4 implements XdrElement {
  private AssetCode4 assetCode;
  private AccountID issuer;

  public void encode(XdrDataOutputStream stream) throws IOException {
    assetCode.encode(stream);
    issuer.encode(stream);
  }

  public static AlphaNum4 decode(XdrDataInputStream stream) throws IOException {
    AlphaNum4 decodedAlphaNum4 = new AlphaNum4();
    decodedAlphaNum4.assetCode = AssetCode4.decode(stream);
    decodedAlphaNum4.issuer = AccountID.decode(stream);
    return decodedAlphaNum4;
  }

  public static AlphaNum4 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static AlphaNum4 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
