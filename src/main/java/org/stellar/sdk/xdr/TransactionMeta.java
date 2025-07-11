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
 * TransactionMeta's original definition in the XDR file is:
 *
 * <pre>
 * union TransactionMeta switch (int v)
 * {
 * case 0:
 *     OperationMeta operations&lt;&gt;;
 * case 1:
 *     TransactionMetaV1 v1;
 * case 2:
 *     TransactionMetaV2 v2;
 * case 3:
 *     TransactionMetaV3 v3;
 * case 4:
 *     TransactionMetaV4 v4;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionMeta implements XdrElement {
  private Integer discriminant;
  private OperationMeta[] operations;
  private TransactionMetaV1 v1;
  private TransactionMetaV2 v2;
  private TransactionMetaV3 v3;
  private TransactionMetaV4 v4;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(discriminant);
    switch (discriminant) {
      case 0:
        int operationsSize = getOperations().length;
        stream.writeInt(operationsSize);
        for (int i = 0; i < operationsSize; i++) {
          operations[i].encode(stream);
        }
        break;
      case 1:
        v1.encode(stream);
        break;
      case 2:
        v2.encode(stream);
        break;
      case 3:
        v3.encode(stream);
        break;
      case 4:
        v4.encode(stream);
        break;
    }
  }

  public static TransactionMeta decode(XdrDataInputStream stream) throws IOException {
    TransactionMeta decodedTransactionMeta = new TransactionMeta();
    Integer discriminant = stream.readInt();
    decodedTransactionMeta.setDiscriminant(discriminant);
    switch (decodedTransactionMeta.getDiscriminant()) {
      case 0:
        int operationsSize = stream.readInt();
        decodedTransactionMeta.operations = new OperationMeta[operationsSize];
        for (int i = 0; i < operationsSize; i++) {
          decodedTransactionMeta.operations[i] = OperationMeta.decode(stream);
        }
        break;
      case 1:
        decodedTransactionMeta.v1 = TransactionMetaV1.decode(stream);
        break;
      case 2:
        decodedTransactionMeta.v2 = TransactionMetaV2.decode(stream);
        break;
      case 3:
        decodedTransactionMeta.v3 = TransactionMetaV3.decode(stream);
        break;
      case 4:
        decodedTransactionMeta.v4 = TransactionMetaV4.decode(stream);
        break;
    }
    return decodedTransactionMeta;
  }

  public static TransactionMeta fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TransactionMeta fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
