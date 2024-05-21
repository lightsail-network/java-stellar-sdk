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
 * TransactionResultSet's original definition in the XDR file is:
 *
 * <pre>
 * struct TransactionResultSet
 * {
 *     TransactionResultPair results&lt;&gt;;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionResultSet implements XdrElement {
  private TransactionResultPair[] results;

  public static void encode(
      XdrDataOutputStream stream, TransactionResultSet encodedTransactionResultSet)
      throws IOException {
    int resultsSize = encodedTransactionResultSet.getResults().length;
    stream.writeInt(resultsSize);
    for (int i = 0; i < resultsSize; i++) {
      TransactionResultPair.encode(stream, encodedTransactionResultSet.results[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static TransactionResultSet decode(XdrDataInputStream stream) throws IOException {
    TransactionResultSet decodedTransactionResultSet = new TransactionResultSet();
    int resultsSize = stream.readInt();
    decodedTransactionResultSet.results = new TransactionResultPair[resultsSize];
    for (int i = 0; i < resultsSize; i++) {
      decodedTransactionResultSet.results[i] = TransactionResultPair.decode(stream);
    }
    return decodedTransactionResultSet;
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

  public static TransactionResultSet fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TransactionResultSet fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
