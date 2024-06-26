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
 * TransactionMetaV1's original definition in the XDR file is:
 *
 * <pre>
 * struct TransactionMetaV1
 * {
 *     LedgerEntryChanges txChanges; // tx level changes if any
 *     OperationMeta operations&lt;&gt;;   // meta for each operation
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionMetaV1 implements XdrElement {
  private LedgerEntryChanges txChanges;
  private OperationMeta[] operations;

  public void encode(XdrDataOutputStream stream) throws IOException {
    txChanges.encode(stream);
    int operationsSize = getOperations().length;
    stream.writeInt(operationsSize);
    for (int i = 0; i < operationsSize; i++) {
      operations[i].encode(stream);
    }
  }

  public static TransactionMetaV1 decode(XdrDataInputStream stream) throws IOException {
    TransactionMetaV1 decodedTransactionMetaV1 = new TransactionMetaV1();
    decodedTransactionMetaV1.txChanges = LedgerEntryChanges.decode(stream);
    int operationsSize = stream.readInt();
    decodedTransactionMetaV1.operations = new OperationMeta[operationsSize];
    for (int i = 0; i < operationsSize; i++) {
      decodedTransactionMetaV1.operations[i] = OperationMeta.decode(stream);
    }
    return decodedTransactionMetaV1;
  }

  public static TransactionMetaV1 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TransactionMetaV1 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
