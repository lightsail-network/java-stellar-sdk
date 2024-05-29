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
 * SCMetaV0's original definition in the XDR file is:
 *
 * <pre>
 * struct SCMetaV0
 * {
 *     string key&lt;&gt;;
 *     string val&lt;&gt;;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SCMetaV0 implements XdrElement {
  private XdrString key;
  private XdrString val;

  public void encode(XdrDataOutputStream stream) throws IOException {
    key.encode(stream);
    val.encode(stream);
  }

  public static SCMetaV0 decode(XdrDataInputStream stream) throws IOException {
    SCMetaV0 decodedSCMetaV0 = new SCMetaV0();
    decodedSCMetaV0.key = XdrString.decode(stream, Integer.MAX_VALUE);
    decodedSCMetaV0.val = XdrString.decode(stream, Integer.MAX_VALUE);
    return decodedSCMetaV0;
  }

  public static SCMetaV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCMetaV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
