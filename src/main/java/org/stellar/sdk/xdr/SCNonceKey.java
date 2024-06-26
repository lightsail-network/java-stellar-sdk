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
 * SCNonceKey's original definition in the XDR file is:
 *
 * <pre>
 * struct SCNonceKey {
 *     int64 nonce;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SCNonceKey implements XdrElement {
  private Int64 nonce;

  public void encode(XdrDataOutputStream stream) throws IOException {
    nonce.encode(stream);
  }

  public static SCNonceKey decode(XdrDataInputStream stream) throws IOException {
    SCNonceKey decodedSCNonceKey = new SCNonceKey();
    decodedSCNonceKey.nonce = Int64.decode(stream);
    return decodedSCNonceKey;
  }

  public static SCNonceKey fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCNonceKey fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
