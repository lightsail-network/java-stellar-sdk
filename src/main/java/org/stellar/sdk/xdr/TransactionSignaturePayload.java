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
 * TransactionSignaturePayload's original definition in the XDR file is:
 *
 * <pre>
 * struct TransactionSignaturePayload
 * {
 *     Hash networkId;
 *     union switch (EnvelopeType type)
 *     {
 *     // Backwards Compatibility: Use ENVELOPE_TYPE_TX to sign ENVELOPE_TYPE_TX_V0
 *     case ENVELOPE_TYPE_TX:
 *         Transaction tx;
 *     case ENVELOPE_TYPE_TX_FEE_BUMP:
 *         FeeBumpTransaction feeBump;
 *     }
 *     taggedTransaction;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionSignaturePayload implements XdrElement {
  private Hash networkId;
  private TransactionSignaturePayloadTaggedTransaction taggedTransaction;

  public static void encode(
      XdrDataOutputStream stream, TransactionSignaturePayload encodedTransactionSignaturePayload)
      throws IOException {
    Hash.encode(stream, encodedTransactionSignaturePayload.networkId);
    TransactionSignaturePayloadTaggedTransaction.encode(
        stream, encodedTransactionSignaturePayload.taggedTransaction);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static TransactionSignaturePayload decode(XdrDataInputStream stream) throws IOException {
    TransactionSignaturePayload decodedTransactionSignaturePayload =
        new TransactionSignaturePayload();
    decodedTransactionSignaturePayload.networkId = Hash.decode(stream);
    decodedTransactionSignaturePayload.taggedTransaction =
        TransactionSignaturePayloadTaggedTransaction.decode(stream);
    return decodedTransactionSignaturePayload;
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

  public static TransactionSignaturePayload fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TransactionSignaturePayload fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  /**
   * TransactionSignaturePayloadTaggedTransaction's original definition in the XDR file is:
   *
   * <pre>
   * union switch (EnvelopeType type)
   *     {
   *     // Backwards Compatibility: Use ENVELOPE_TYPE_TX to sign ENVELOPE_TYPE_TX_V0
   *     case ENVELOPE_TYPE_TX:
   *         Transaction tx;
   *     case ENVELOPE_TYPE_TX_FEE_BUMP:
   *         FeeBumpTransaction feeBump;
   *     }
   * </pre>
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  public static class TransactionSignaturePayloadTaggedTransaction implements XdrElement {
    private EnvelopeType discriminant;
    private Transaction tx;
    private FeeBumpTransaction feeBump;

    public static void encode(
        XdrDataOutputStream stream,
        TransactionSignaturePayloadTaggedTransaction
            encodedTransactionSignaturePayloadTaggedTransaction)
        throws IOException {
      // Xdrgen::AST::Identifier
      // EnvelopeType
      stream.writeInt(
          encodedTransactionSignaturePayloadTaggedTransaction.getDiscriminant().getValue());
      switch (encodedTransactionSignaturePayloadTaggedTransaction.getDiscriminant()) {
        case ENVELOPE_TYPE_TX:
          Transaction.encode(stream, encodedTransactionSignaturePayloadTaggedTransaction.tx);
          break;
        case ENVELOPE_TYPE_TX_FEE_BUMP:
          FeeBumpTransaction.encode(
              stream, encodedTransactionSignaturePayloadTaggedTransaction.feeBump);
          break;
      }
    }

    public void encode(XdrDataOutputStream stream) throws IOException {
      encode(stream, this);
    }

    public static TransactionSignaturePayloadTaggedTransaction decode(XdrDataInputStream stream)
        throws IOException {
      TransactionSignaturePayloadTaggedTransaction
          decodedTransactionSignaturePayloadTaggedTransaction =
              new TransactionSignaturePayloadTaggedTransaction();
      EnvelopeType discriminant = EnvelopeType.decode(stream);
      decodedTransactionSignaturePayloadTaggedTransaction.setDiscriminant(discriminant);
      switch (decodedTransactionSignaturePayloadTaggedTransaction.getDiscriminant()) {
        case ENVELOPE_TYPE_TX:
          decodedTransactionSignaturePayloadTaggedTransaction.tx = Transaction.decode(stream);
          break;
        case ENVELOPE_TYPE_TX_FEE_BUMP:
          decodedTransactionSignaturePayloadTaggedTransaction.feeBump =
              FeeBumpTransaction.decode(stream);
          break;
      }
      return decodedTransactionSignaturePayloadTaggedTransaction;
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

    public static TransactionSignaturePayloadTaggedTransaction fromXdrBase64(String xdr)
        throws IOException {
      byte[] bytes = Base64Factory.getInstance().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static TransactionSignaturePayloadTaggedTransaction fromXdrByteArray(byte[] xdr)
        throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
