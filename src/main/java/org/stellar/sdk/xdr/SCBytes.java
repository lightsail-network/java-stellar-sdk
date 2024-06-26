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
 * SCBytes's original definition in the XDR file is:
 *
 * <pre>
 * typedef opaque SCBytes&lt;&gt;;
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SCBytes implements XdrElement {
  private byte[] SCBytes;

  public void encode(XdrDataOutputStream stream) throws IOException {
    int SCBytesSize = SCBytes.length;
    stream.writeInt(SCBytesSize);
    stream.write(getSCBytes(), 0, SCBytesSize);
  }

  public static SCBytes decode(XdrDataInputStream stream) throws IOException {
    SCBytes decodedSCBytes = new SCBytes();
    int SCBytesSize = stream.readInt();
    decodedSCBytes.SCBytes = new byte[SCBytesSize];
    stream.read(decodedSCBytes.SCBytes, 0, SCBytesSize);
    return decodedSCBytes;
  }

  public static SCBytes fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCBytes fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
