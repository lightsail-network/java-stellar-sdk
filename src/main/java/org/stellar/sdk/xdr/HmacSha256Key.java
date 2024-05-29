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
 * HmacSha256Key's original definition in the XDR file is:
 *
 * <pre>
 * struct HmacSha256Key
 * {
 *     opaque key[32];
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class HmacSha256Key implements XdrElement {
  private byte[] key;

  public void encode(XdrDataOutputStream stream) throws IOException {
    int keySize = key.length;
    stream.write(getKey(), 0, keySize);
  }

  public static HmacSha256Key decode(XdrDataInputStream stream) throws IOException {
    HmacSha256Key decodedHmacSha256Key = new HmacSha256Key();
    int keySize = 32;
    decodedHmacSha256Key.key = new byte[keySize];
    stream.read(decodedHmacSha256Key.key, 0, keySize);
    return decodedHmacSha256Key;
  }

  public static HmacSha256Key fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static HmacSha256Key fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
