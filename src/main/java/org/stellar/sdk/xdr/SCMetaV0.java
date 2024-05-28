// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

  public static void encode(XdrDataOutputStream stream, SCMetaV0 encodedSCMetaV0)
      throws IOException {
    encodedSCMetaV0.key.encode(stream);
    encodedSCMetaV0.val.encode(stream);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SCMetaV0 decode(XdrDataInputStream stream) throws IOException {
    SCMetaV0 decodedSCMetaV0 = new SCMetaV0();
    decodedSCMetaV0.key = XdrString.decode(stream, Integer.MAX_VALUE);
    decodedSCMetaV0.val = XdrString.decode(stream, Integer.MAX_VALUE);
    return decodedSCMetaV0;
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64Factory.getInstance().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
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
