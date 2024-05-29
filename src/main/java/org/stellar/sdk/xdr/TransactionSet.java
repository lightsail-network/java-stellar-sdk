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
 * TransactionSet's original definition in the XDR file is:
 *
 * <pre>
 * struct TransactionSet
 * {
 *     Hash previousLedgerHash;
 *     TransactionEnvelope txs&lt;&gt;;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionSet implements XdrElement {
  private Hash previousLedgerHash;
  private TransactionEnvelope[] txs;

  public void encode(XdrDataOutputStream stream) throws IOException {
    previousLedgerHash.encode(stream);
    int txsSize = getTxs().length;
    stream.writeInt(txsSize);
    for (int i = 0; i < txsSize; i++) {
      txs[i].encode(stream);
    }
  }

  public static TransactionSet decode(XdrDataInputStream stream) throws IOException {
    TransactionSet decodedTransactionSet = new TransactionSet();
    decodedTransactionSet.previousLedgerHash = Hash.decode(stream);
    int txsSize = stream.readInt();
    decodedTransactionSet.txs = new TransactionEnvelope[txsSize];
    for (int i = 0; i < txsSize; i++) {
      decodedTransactionSet.txs[i] = TransactionEnvelope.decode(stream);
    }
    return decodedTransactionSet;
  }

  public static TransactionSet fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TransactionSet fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
