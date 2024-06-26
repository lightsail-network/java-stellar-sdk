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
 * SorobanAuthorizationEntry's original definition in the XDR file is:
 *
 * <pre>
 * struct SorobanAuthorizationEntry
 * {
 *     SorobanCredentials credentials;
 *     SorobanAuthorizedInvocation rootInvocation;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SorobanAuthorizationEntry implements XdrElement {
  private SorobanCredentials credentials;
  private SorobanAuthorizedInvocation rootInvocation;

  public void encode(XdrDataOutputStream stream) throws IOException {
    credentials.encode(stream);
    rootInvocation.encode(stream);
  }

  public static SorobanAuthorizationEntry decode(XdrDataInputStream stream) throws IOException {
    SorobanAuthorizationEntry decodedSorobanAuthorizationEntry = new SorobanAuthorizationEntry();
    decodedSorobanAuthorizationEntry.credentials = SorobanCredentials.decode(stream);
    decodedSorobanAuthorizationEntry.rootInvocation = SorobanAuthorizedInvocation.decode(stream);
    return decodedSorobanAuthorizationEntry;
  }

  public static SorobanAuthorizationEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SorobanAuthorizationEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
