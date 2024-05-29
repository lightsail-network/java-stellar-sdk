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
 * LedgerCloseMeta's original definition in the XDR file is:
 *
 * <pre>
 * union LedgerCloseMeta switch (int v)
 * {
 * case 0:
 *     LedgerCloseMetaV0 v0;
 * case 1:
 *     LedgerCloseMetaV1 v1;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LedgerCloseMeta implements XdrElement {
  private Integer discriminant;
  private LedgerCloseMetaV0 v0;
  private LedgerCloseMetaV1 v1;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(discriminant);
    switch (discriminant) {
      case 0:
        v0.encode(stream);
        break;
      case 1:
        v1.encode(stream);
        break;
    }
  }

  public static LedgerCloseMeta decode(XdrDataInputStream stream) throws IOException {
    LedgerCloseMeta decodedLedgerCloseMeta = new LedgerCloseMeta();
    Integer discriminant = stream.readInt();
    decodedLedgerCloseMeta.setDiscriminant(discriminant);
    switch (decodedLedgerCloseMeta.getDiscriminant()) {
      case 0:
        decodedLedgerCloseMeta.v0 = LedgerCloseMetaV0.decode(stream);
        break;
      case 1:
        decodedLedgerCloseMeta.v1 = LedgerCloseMetaV1.decode(stream);
        break;
    }
    return decodedLedgerCloseMeta;
  }

  public static LedgerCloseMeta fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static LedgerCloseMeta fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
