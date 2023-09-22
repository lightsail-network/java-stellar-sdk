// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  struct TransactionHistoryResultEntry
//  {
//      uint32 ledgerSeq;
//      TransactionResultSet txResultSet;
//
//      // reserved for future use
//      union switch (int v)
//      {
//      case 0:
//          void;
//      }
//      ext;
//  };

//  ===========================================================================
public class TransactionHistoryResultEntry implements XdrElement {
  public TransactionHistoryResultEntry() {}

  private Uint32 ledgerSeq;

  public Uint32 getLedgerSeq() {
    return this.ledgerSeq;
  }

  public void setLedgerSeq(Uint32 value) {
    this.ledgerSeq = value;
  }

  private TransactionResultSet txResultSet;

  public TransactionResultSet getTxResultSet() {
    return this.txResultSet;
  }

  public void setTxResultSet(TransactionResultSet value) {
    this.txResultSet = value;
  }

  private TransactionHistoryResultEntryExt ext;

  public TransactionHistoryResultEntryExt getExt() {
    return this.ext;
  }

  public void setExt(TransactionHistoryResultEntryExt value) {
    this.ext = value;
  }

  public static void encode(
      XdrDataOutputStream stream,
      TransactionHistoryResultEntry encodedTransactionHistoryResultEntry)
      throws IOException {
    Uint32.encode(stream, encodedTransactionHistoryResultEntry.ledgerSeq);
    TransactionResultSet.encode(stream, encodedTransactionHistoryResultEntry.txResultSet);
    TransactionHistoryResultEntryExt.encode(stream, encodedTransactionHistoryResultEntry.ext);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static TransactionHistoryResultEntry decode(XdrDataInputStream stream) throws IOException {
    TransactionHistoryResultEntry decodedTransactionHistoryResultEntry =
        new TransactionHistoryResultEntry();
    decodedTransactionHistoryResultEntry.ledgerSeq = Uint32.decode(stream);
    decodedTransactionHistoryResultEntry.txResultSet = TransactionResultSet.decode(stream);
    decodedTransactionHistoryResultEntry.ext = TransactionHistoryResultEntryExt.decode(stream);
    return decodedTransactionHistoryResultEntry;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ledgerSeq, this.txResultSet, this.ext);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof TransactionHistoryResultEntry)) {
      return false;
    }

    TransactionHistoryResultEntry other = (TransactionHistoryResultEntry) object;
    return Objects.equals(this.ledgerSeq, other.ledgerSeq)
        && Objects.equals(this.txResultSet, other.txResultSet)
        && Objects.equals(this.ext, other.ext);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static TransactionHistoryResultEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TransactionHistoryResultEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private Uint32 ledgerSeq;
    private TransactionResultSet txResultSet;
    private TransactionHistoryResultEntryExt ext;

    public Builder ledgerSeq(Uint32 ledgerSeq) {
      this.ledgerSeq = ledgerSeq;
      return this;
    }

    public Builder txResultSet(TransactionResultSet txResultSet) {
      this.txResultSet = txResultSet;
      return this;
    }

    public Builder ext(TransactionHistoryResultEntryExt ext) {
      this.ext = ext;
      return this;
    }

    public TransactionHistoryResultEntry build() {
      TransactionHistoryResultEntry val = new TransactionHistoryResultEntry();
      val.setLedgerSeq(this.ledgerSeq);
      val.setTxResultSet(this.txResultSet);
      val.setExt(this.ext);
      return val;
    }
  }

  public static class TransactionHistoryResultEntryExt implements XdrElement {
    public TransactionHistoryResultEntryExt() {}

    Integer v;

    public Integer getDiscriminant() {
      return this.v;
    }

    public void setDiscriminant(Integer value) {
      this.v = value;
    }

    public static final class Builder {
      private Integer discriminant;

      public Builder discriminant(Integer discriminant) {
        this.discriminant = discriminant;
        return this;
      }

      public TransactionHistoryResultEntryExt build() {
        TransactionHistoryResultEntryExt val = new TransactionHistoryResultEntryExt();
        val.setDiscriminant(discriminant);
        return val;
      }
    }

    public static void encode(
        XdrDataOutputStream stream,
        TransactionHistoryResultEntryExt encodedTransactionHistoryResultEntryExt)
        throws IOException {
      // Xdrgen::AST::Typespecs::Int
      // Integer
      stream.writeInt(encodedTransactionHistoryResultEntryExt.getDiscriminant().intValue());
      switch (encodedTransactionHistoryResultEntryExt.getDiscriminant()) {
        case 0:
          break;
      }
    }

    public void encode(XdrDataOutputStream stream) throws IOException {
      encode(stream, this);
    }

    public static TransactionHistoryResultEntryExt decode(XdrDataInputStream stream)
        throws IOException {
      TransactionHistoryResultEntryExt decodedTransactionHistoryResultEntryExt =
          new TransactionHistoryResultEntryExt();
      Integer discriminant = stream.readInt();
      decodedTransactionHistoryResultEntryExt.setDiscriminant(discriminant);
      switch (decodedTransactionHistoryResultEntryExt.getDiscriminant()) {
        case 0:
          break;
      }
      return decodedTransactionHistoryResultEntryExt;
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.v);
    }

    @Override
    public boolean equals(Object object) {
      if (!(object instanceof TransactionHistoryResultEntryExt)) {
        return false;
      }

      TransactionHistoryResultEntryExt other = (TransactionHistoryResultEntryExt) object;
      return Objects.equals(this.v, other.v);
    }

    @Override
    public String toXdrBase64() throws IOException {
      return Base64.encodeToString(toXdrByteArray());
    }

    @Override
    public byte[] toXdrByteArray() throws IOException {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
      encode(xdrDataOutputStream);
      return byteArrayOutputStream.toByteArray();
    }

    public static TransactionHistoryResultEntryExt fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64.decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static TransactionHistoryResultEntryExt fromXdrByteArray(byte[] xdr) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
