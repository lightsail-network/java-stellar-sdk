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
 * OperationMeta's original definition in the XDR file is:
 *
 * <pre>
 * struct OperationMeta
 * {
 *     LedgerEntryChanges changes;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OperationMeta implements XdrElement {
  private LedgerEntryChanges changes;

  public void encode(XdrDataOutputStream stream) throws IOException {
    changes.encode(stream);
  }

  public static OperationMeta decode(XdrDataInputStream stream) throws IOException {
    OperationMeta decodedOperationMeta = new OperationMeta();
    decodedOperationMeta.changes = LedgerEntryChanges.decode(stream);
    return decodedOperationMeta;
  }

  public static OperationMeta fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static OperationMeta fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
