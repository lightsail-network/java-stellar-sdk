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
 * Auth's original definition in the XDR file is:
 *
 * <pre>
 * struct Auth
 * {
 *     int flags;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Auth implements XdrElement {
  private Integer flags;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(flags);
  }

  public static Auth decode(XdrDataInputStream stream) throws IOException {
    Auth decodedAuth = new Auth();
    decodedAuth.flags = stream.readInt();
    return decodedAuth;
  }

  public static Auth fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static Auth fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
