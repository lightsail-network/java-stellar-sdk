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
 * AlphaNum12's original definition in the XDR file is:
 *
 * <pre>
 * struct AlphaNum12
 * {
 *     AssetCode12 assetCode;
 *     AccountID issuer;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AlphaNum12 implements XdrElement {
  private AssetCode12 assetCode;
  private AccountID issuer;

  public void encode(XdrDataOutputStream stream) throws IOException {
    assetCode.encode(stream);
    issuer.encode(stream);
  }

  public static AlphaNum12 decode(XdrDataInputStream stream) throws IOException {
    AlphaNum12 decodedAlphaNum12 = new AlphaNum12();
    decodedAlphaNum12.assetCode = AssetCode12.decode(stream);
    decodedAlphaNum12.issuer = AccountID.decode(stream);
    return decodedAlphaNum12;
  }

  public static AlphaNum12 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static AlphaNum12 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
