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
 * Price's original definition in the XDR file is:
 *
 * <pre>
 * struct Price
 * {
 *     int32 n; // numerator
 *     int32 d; // denominator
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Price implements XdrElement {
  private Int32 n;
  private Int32 d;

  public void encode(XdrDataOutputStream stream) throws IOException {
    n.encode(stream);
    d.encode(stream);
  }

  public static Price decode(XdrDataInputStream stream) throws IOException {
    Price decodedPrice = new Price();
    decodedPrice.n = Int32.decode(stream);
    decodedPrice.d = Int32.decode(stream);
    return decodedPrice;
  }

  public static Price fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static Price fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
