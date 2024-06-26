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
 * Uint32's original definition in the XDR file is:
 *
 * <pre>
 * typedef unsigned int uint32;
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Uint32 implements XdrElement {
  private XdrUnsignedInteger uint32;

  public void encode(XdrDataOutputStream stream) throws IOException {
    uint32.encode(stream);
  }

  public static Uint32 decode(XdrDataInputStream stream) throws IOException {
    Uint32 decodedUint32 = new Uint32();
    decodedUint32.uint32 = XdrUnsignedInteger.decode(stream);
    return decodedUint32;
  }

  public static Uint32 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static Uint32 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
