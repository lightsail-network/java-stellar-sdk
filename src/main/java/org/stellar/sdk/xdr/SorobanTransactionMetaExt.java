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
 * SorobanTransactionMetaExt's original definition in the XDR file is:
 *
 * <pre>
 * union SorobanTransactionMetaExt switch (int v)
 * {
 * case 0:
 *     void;
 * case 1:
 *     SorobanTransactionMetaExtV1 v1;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SorobanTransactionMetaExt implements XdrElement {
  private Integer discriminant;
  private SorobanTransactionMetaExtV1 v1;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(discriminant);
    switch (discriminant) {
      case 0:
        break;
      case 1:
        v1.encode(stream);
        break;
    }
  }

  public static SorobanTransactionMetaExt decode(XdrDataInputStream stream) throws IOException {
    SorobanTransactionMetaExt decodedSorobanTransactionMetaExt = new SorobanTransactionMetaExt();
    Integer discriminant = stream.readInt();
    decodedSorobanTransactionMetaExt.setDiscriminant(discriminant);
    switch (decodedSorobanTransactionMetaExt.getDiscriminant()) {
      case 0:
        break;
      case 1:
        decodedSorobanTransactionMetaExt.v1 = SorobanTransactionMetaExtV1.decode(stream);
        break;
    }
    return decodedSorobanTransactionMetaExt;
  }

  public static SorobanTransactionMetaExt fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SorobanTransactionMetaExt fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
