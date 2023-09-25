// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

// === xdr source ============================================================

//  struct TransactionV0
//  {
//      uint256 sourceAccountEd25519;
//      uint32 fee;
//      SequenceNumber seqNum;
//      TimeBounds* timeBounds;
//      Memo memo;
//      Operation operations<MAX_OPS_PER_TX>;
//      union switch (int v)
//      {
//      case 0:
//          void;
//      }
//      ext;
//  };

//  ===========================================================================
public class TransactionV0 implements XdrElement {
  public TransactionV0() {}

  private Uint256 sourceAccountEd25519;

  public Uint256 getSourceAccountEd25519() {
    return this.sourceAccountEd25519;
  }

  public void setSourceAccountEd25519(Uint256 value) {
    this.sourceAccountEd25519 = value;
  }

  private Uint32 fee;

  public Uint32 getFee() {
    return this.fee;
  }

  public void setFee(Uint32 value) {
    this.fee = value;
  }

  private SequenceNumber seqNum;

  public SequenceNumber getSeqNum() {
    return this.seqNum;
  }

  public void setSeqNum(SequenceNumber value) {
    this.seqNum = value;
  }

  private TimeBounds timeBounds;

  public TimeBounds getTimeBounds() {
    return this.timeBounds;
  }

  public void setTimeBounds(TimeBounds value) {
    this.timeBounds = value;
  }

  private Memo memo;

  public Memo getMemo() {
    return this.memo;
  }

  public void setMemo(Memo value) {
    this.memo = value;
  }

  private Operation[] operations;

  public Operation[] getOperations() {
    return this.operations;
  }

  public void setOperations(Operation[] value) {
    this.operations = value;
  }

  private TransactionV0Ext ext;

  public TransactionV0Ext getExt() {
    return this.ext;
  }

  public void setExt(TransactionV0Ext value) {
    this.ext = value;
  }

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
  public int hashCode() {
    return Objects.hash(
        this.sourceAccountEd25519,
        this.fee,
        this.seqNum,
        this.timeBounds,
        this.memo,
        Arrays.hashCode(this.operations),
        this.ext);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof TransactionV0)) {
      return false;
    }

    TransactionV0 other = (TransactionV0) object;
    return Objects.equals(this.sourceAccountEd25519, other.sourceAccountEd25519)
        && Objects.equals(this.fee, other.fee)
        && Objects.equals(this.seqNum, other.seqNum)
        && Objects.equals(this.timeBounds, other.timeBounds)
        && Objects.equals(this.memo, other.memo)
        && Arrays.equals(this.operations, other.operations)
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

  public static TransactionV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TransactionV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private Uint256 sourceAccountEd25519;
    private Uint32 fee;
    private SequenceNumber seqNum;
    private TimeBounds timeBounds;
    private Memo memo;
    private Operation[] operations;
    private TransactionV0Ext ext;

    public Builder sourceAccountEd25519(Uint256 sourceAccountEd25519) {
      this.sourceAccountEd25519 = sourceAccountEd25519;
      return this;
    }

    public Builder fee(Uint32 fee) {
      this.fee = fee;
      return this;
    }

    public Builder seqNum(SequenceNumber seqNum) {
      this.seqNum = seqNum;
      return this;
    }

    public Builder timeBounds(TimeBounds timeBounds) {
      this.timeBounds = timeBounds;
      return this;
    }

    public Builder memo(Memo memo) {
      this.memo = memo;
      return this;
    }

    public Builder operations(Operation[] operations) {
      this.operations = operations;
      return this;
    }

    public Builder ext(TransactionV0Ext ext) {
      this.ext = ext;
      return this;
    }

    public TransactionV0 build() {
      TransactionV0 val = new TransactionV0();
      val.setSourceAccountEd25519(this.sourceAccountEd25519);
      val.setFee(this.fee);
      val.setSeqNum(this.seqNum);
      val.setTimeBounds(this.timeBounds);
      val.setMemo(this.memo);
      val.setOperations(this.operations);
      val.setExt(this.ext);
      return val;
    }
  }

  public static class TransactionV0Ext implements XdrElement {
    public TransactionV0Ext() {}

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

      public TransactionV0Ext build() {
        TransactionV0Ext val = new TransactionV0Ext();
        val.setDiscriminant(discriminant);
        return val;
      }
    }

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
    public int hashCode() {
      return Objects.hash(this.v);
    }

    @Override
    public boolean equals(Object object) {
      if (!(object instanceof TransactionV0Ext)) {
        return false;
      }

      TransactionV0Ext other = (TransactionV0Ext) object;
      return Objects.equals(this.v, other.v);
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

    public static TransactionV0Ext fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64.getDecoder().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static TransactionV0Ext fromXdrByteArray(byte[] xdr) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
