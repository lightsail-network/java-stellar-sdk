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
 * TransactionV0's original definition in the XDR file is:
 *
 * <pre>
 * struct TransactionV0
 * {
 *     uint256 sourceAccountEd25519;
 *     uint32 fee;
 *     SequenceNumber seqNum;
 *     TimeBounds&#42; timeBounds;
 *     Memo memo;
 *     Operation operations&lt;MAX_OPS_PER_TX&gt;;
 *     union switch (int v)
 *     {
 *     case 0:
 *         void;
 *     }
 *     ext;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionV0 implements XdrElement {
  private Uint256 sourceAccountEd25519;
  private Uint32 fee;
  private SequenceNumber seqNum;
  private TimeBounds timeBounds;
  private Memo memo;
  private Operation[] operations;
  private TransactionV0Ext ext;

  public static void encode(XdrDataOutputStream stream, TransactionV0 encodedTransactionV0)
      throws IOException {
    Uint256.encode(stream, encodedTransactionV0.sourceAccountEd25519);
    Uint32.encode(stream, encodedTransactionV0.fee);
    SequenceNumber.encode(stream, encodedTransactionV0.seqNum);
    if (encodedTransactionV0.timeBounds != null) {
      stream.writeInt(1);
      TimeBounds.encode(stream, encodedTransactionV0.timeBounds);
    } else {
      stream.writeInt(0);
    }
    Memo.encode(stream, encodedTransactionV0.memo);
    int operationssize = encodedTransactionV0.getOperations().length;
    stream.writeInt(operationssize);
    for (int i = 0; i < operationssize; i++) {
      Operation.encode(stream, encodedTransactionV0.operations[i]);
    }
    TransactionV0Ext.encode(stream, encodedTransactionV0.ext);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static TransactionV0 decode(XdrDataInputStream stream) throws IOException {
    TransactionV0 decodedTransactionV0 = new TransactionV0();
    decodedTransactionV0.sourceAccountEd25519 = Uint256.decode(stream);
    decodedTransactionV0.fee = Uint32.decode(stream);
    decodedTransactionV0.seqNum = SequenceNumber.decode(stream);
    int timeBoundsPresent = stream.readInt();
    if (timeBoundsPresent != 0) {
      decodedTransactionV0.timeBounds = TimeBounds.decode(stream);
    }
    decodedTransactionV0.memo = Memo.decode(stream);
    int operationssize = stream.readInt();
    decodedTransactionV0.operations = new Operation[operationssize];
    for (int i = 0; i < operationssize; i++) {
      decodedTransactionV0.operations[i] = Operation.decode(stream);
    }
    decodedTransactionV0.ext = TransactionV0Ext.decode(stream);
    return decodedTransactionV0;
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

  public static TransactionV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TransactionV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  /**
   * TransactionV0Ext's original definition in the XDR file is:
   *
   * <pre>
   * union switch (int v)
   *     {
   *     case 0:
   *         void;
   *     }
   * </pre>
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  public static class TransactionV0Ext implements XdrElement {
    private Integer discriminant;

    public static void encode(XdrDataOutputStream stream, TransactionV0Ext encodedTransactionV0Ext)
        throws IOException {
      // Xdrgen::AST::Typespecs::Int
      // Integer
      stream.writeInt(encodedTransactionV0Ext.getDiscriminant().intValue());
      switch (encodedTransactionV0Ext.getDiscriminant()) {
        case 0:
          break;
      }
    }

    public void encode(XdrDataOutputStream stream) throws IOException {
      encode(stream, this);
    }

    public static TransactionV0Ext decode(XdrDataInputStream stream) throws IOException {
      TransactionV0Ext decodedTransactionV0Ext = new TransactionV0Ext();
      Integer discriminant = stream.readInt();
      decodedTransactionV0Ext.setDiscriminant(discriminant);
      switch (decodedTransactionV0Ext.getDiscriminant()) {
        case 0:
          break;
      }
      return decodedTransactionV0Ext;
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

    public static TransactionV0Ext fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64Factory.getInstance().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static TransactionV0Ext fromXdrByteArray(byte[] xdr) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
