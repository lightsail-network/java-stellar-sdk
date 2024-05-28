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
 * TransactionMetaV3's original definition in the XDR file is:
 *
 * <pre>
 * struct TransactionMetaV3
 * {
 *     ExtensionPoint ext;
 *
 *     LedgerEntryChanges txChangesBefore;  // tx level changes before operations
 *                                          // are applied if any
 *     OperationMeta operations&lt;&gt;;          // meta for each operation
 *     LedgerEntryChanges txChangesAfter;   // tx level changes after operations are
 *                                          // applied if any
 *     SorobanTransactionMeta&#42; sorobanMeta; // Soroban-specific meta (only for
 *                                          // Soroban transactions).
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionMetaV3 implements XdrElement {
  private ExtensionPoint ext;
  private LedgerEntryChanges txChangesBefore;
  private OperationMeta[] operations;
  private LedgerEntryChanges txChangesAfter;
  private SorobanTransactionMeta sorobanMeta;

  public static void encode(XdrDataOutputStream stream, TransactionMetaV3 encodedTransactionMetaV3)
      throws IOException {
    ExtensionPoint.encode(stream, encodedTransactionMetaV3.ext);
    LedgerEntryChanges.encode(stream, encodedTransactionMetaV3.txChangesBefore);
    int operationsSize = encodedTransactionMetaV3.getOperations().length;
    stream.writeInt(operationsSize);
    for (int i = 0; i < operationsSize; i++) {
      OperationMeta.encode(stream, encodedTransactionMetaV3.operations[i]);
    }
    LedgerEntryChanges.encode(stream, encodedTransactionMetaV3.txChangesAfter);
    if (encodedTransactionMetaV3.sorobanMeta != null) {
      stream.writeInt(1);
      SorobanTransactionMeta.encode(stream, encodedTransactionMetaV3.sorobanMeta);
    } else {
      stream.writeInt(0);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static TransactionMetaV3 decode(XdrDataInputStream stream) throws IOException {
    TransactionMetaV3 decodedTransactionMetaV3 = new TransactionMetaV3();
    decodedTransactionMetaV3.ext = ExtensionPoint.decode(stream);
    decodedTransactionMetaV3.txChangesBefore = LedgerEntryChanges.decode(stream);
    int operationsSize = stream.readInt();
    decodedTransactionMetaV3.operations = new OperationMeta[operationsSize];
    for (int i = 0; i < operationsSize; i++) {
      decodedTransactionMetaV3.operations[i] = OperationMeta.decode(stream);
    }
    decodedTransactionMetaV3.txChangesAfter = LedgerEntryChanges.decode(stream);
    int sorobanMetaPresent = stream.readInt();
    if (sorobanMetaPresent != 0) {
      decodedTransactionMetaV3.sorobanMeta = SorobanTransactionMeta.decode(stream);
    }
    return decodedTransactionMetaV3;
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

  public static TransactionMetaV3 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TransactionMetaV3 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
