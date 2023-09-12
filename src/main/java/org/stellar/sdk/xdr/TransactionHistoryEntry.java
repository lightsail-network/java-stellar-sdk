// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

// === xdr source ============================================================

//  struct TransactionHistoryEntry
//  {
//      uint32 ledgerSeq;
//      TransactionSet txSet;
//
//      // when v != 0, txSet must be empty
//      union switch (int v)
//      {
//      case 0:
//          void;
//      case 1:
//          GeneralizedTransactionSet generalizedTxSet;
//      }
//      ext;
//  };

//  ===========================================================================
public class TransactionHistoryEntry implements XdrElement {
  public TransactionHistoryEntry() {}

  private Uint32 ledgerSeq;

  public Uint32 getLedgerSeq() {
    return this.ledgerSeq;
  }

  public void setLedgerSeq(Uint32 value) {
    this.ledgerSeq = value;
  }

  private TransactionSet txSet;

  public TransactionSet getTxSet() {
    return this.txSet;
  }

  public void setTxSet(TransactionSet value) {
    this.txSet = value;
  }

  private TransactionHistoryEntryExt ext;

  public TransactionHistoryEntryExt getExt() {
    return this.ext;
  }

  public void setExt(TransactionHistoryEntryExt value) {
    this.ext = value;
  }

  public static void encode(
      XdrDataOutputStream stream, TransactionHistoryEntry encodedTransactionHistoryEntry)
      throws IOException {
    Uint32.encode(stream, encodedTransactionHistoryEntry.ledgerSeq);
    TransactionSet.encode(stream, encodedTransactionHistoryEntry.txSet);
    TransactionHistoryEntryExt.encode(stream, encodedTransactionHistoryEntry.ext);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static TransactionHistoryEntry decode(XdrDataInputStream stream) throws IOException {
    TransactionHistoryEntry decodedTransactionHistoryEntry = new TransactionHistoryEntry();
    decodedTransactionHistoryEntry.ledgerSeq = Uint32.decode(stream);
    decodedTransactionHistoryEntry.txSet = TransactionSet.decode(stream);
    decodedTransactionHistoryEntry.ext = TransactionHistoryEntryExt.decode(stream);
    return decodedTransactionHistoryEntry;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ledgerSeq, this.txSet, this.ext);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof TransactionHistoryEntry)) {
      return false;
    }

    TransactionHistoryEntry other = (TransactionHistoryEntry) object;
    return Objects.equals(this.ledgerSeq, other.ledgerSeq)
        && Objects.equals(this.txSet, other.txSet)
        && Objects.equals(this.ext, other.ext);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.getEncoder().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static TransactionHistoryEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TransactionHistoryEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private Uint32 ledgerSeq;
    private TransactionSet txSet;
    private TransactionHistoryEntryExt ext;

    public Builder ledgerSeq(Uint32 ledgerSeq) {
      this.ledgerSeq = ledgerSeq;
      return this;
    }

    public Builder txSet(TransactionSet txSet) {
      this.txSet = txSet;
      return this;
    }

    public Builder ext(TransactionHistoryEntryExt ext) {
      this.ext = ext;
      return this;
    }

    public TransactionHistoryEntry build() {
      TransactionHistoryEntry val = new TransactionHistoryEntry();
      val.setLedgerSeq(this.ledgerSeq);
      val.setTxSet(this.txSet);
      val.setExt(this.ext);
      return val;
    }
  }

  public static class TransactionHistoryEntryExt implements XdrElement {
    public TransactionHistoryEntryExt() {}

    Integer v;

    public Integer getDiscriminant() {
      return this.v;
    }

    public void setDiscriminant(Integer value) {
      this.v = value;
    }

    private GeneralizedTransactionSet generalizedTxSet;

    public GeneralizedTransactionSet getGeneralizedTxSet() {
      return this.generalizedTxSet;
    }

    public void setGeneralizedTxSet(GeneralizedTransactionSet value) {
      this.generalizedTxSet = value;
    }

    public static final class Builder {
      private Integer discriminant;
      private GeneralizedTransactionSet generalizedTxSet;

      public Builder discriminant(Integer discriminant) {
        this.discriminant = discriminant;
        return this;
      }

      public Builder generalizedTxSet(GeneralizedTransactionSet generalizedTxSet) {
        this.generalizedTxSet = generalizedTxSet;
        return this;
      }

      public TransactionHistoryEntryExt build() {
        TransactionHistoryEntryExt val = new TransactionHistoryEntryExt();
        val.setDiscriminant(discriminant);
        val.setGeneralizedTxSet(this.generalizedTxSet);
        return val;
      }
    }

    public static void encode(
        XdrDataOutputStream stream, TransactionHistoryEntryExt encodedTransactionHistoryEntryExt)
        throws IOException {
      // Xdrgen::AST::Typespecs::Int
      // Integer
      stream.writeInt(encodedTransactionHistoryEntryExt.getDiscriminant().intValue());
      switch (encodedTransactionHistoryEntryExt.getDiscriminant()) {
        case 0:
          break;
        case 1:
          GeneralizedTransactionSet.encode(
              stream, encodedTransactionHistoryEntryExt.generalizedTxSet);
          break;
      }
    }

    public void encode(XdrDataOutputStream stream) throws IOException {
      encode(stream, this);
    }

    public static TransactionHistoryEntryExt decode(XdrDataInputStream stream) throws IOException {
      TransactionHistoryEntryExt decodedTransactionHistoryEntryExt =
          new TransactionHistoryEntryExt();
      Integer discriminant = stream.readInt();
      decodedTransactionHistoryEntryExt.setDiscriminant(discriminant);
      switch (decodedTransactionHistoryEntryExt.getDiscriminant()) {
        case 0:
          break;
        case 1:
          decodedTransactionHistoryEntryExt.generalizedTxSet =
              GeneralizedTransactionSet.decode(stream);
          break;
      }
      return decodedTransactionHistoryEntryExt;
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.generalizedTxSet, this.v);
    }

    @Override
    public boolean equals(Object object) {
      if (!(object instanceof TransactionHistoryEntryExt)) {
        return false;
      }

      TransactionHistoryEntryExt other = (TransactionHistoryEntryExt) object;
      return Objects.equals(this.generalizedTxSet, other.generalizedTxSet)
          && Objects.equals(this.v, other.v);
    }

    @Override
    public String toXdrBase64() throws IOException {
      return Base64.getEncoder().encodeToString(toXdrByteArray());
    }

    @Override
    public byte[] toXdrByteArray() throws IOException {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
      encode(xdrDataOutputStream);
      return byteArrayOutputStream.toByteArray();
    }

    public static TransactionHistoryEntryExt fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64.getDecoder().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static TransactionHistoryEntryExt fromXdrByteArray(byte[] xdr) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
