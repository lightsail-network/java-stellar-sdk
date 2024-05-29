// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * Hash's original definition in the XDR file is:
 *
 * <pre>
 * typedef opaque Hash[32];
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hash implements XdrElement {
  private byte[] Hash;

  public void encode(XdrDataOutputStream stream) throws IOException {
    int HashSize = Hash.length;
    stream.write(getHash(), 0, HashSize);
  }

  public static Hash decode(XdrDataInputStream stream) throws IOException {
    Hash decodedHash = new Hash();
    int HashSize = 32;
    decodedHash.Hash = new byte[HashSize];
    stream.read(decodedHash.Hash, 0, HashSize);
    return decodedHash;
  }

  public static Hash fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static Hash fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
