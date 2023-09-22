// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  union TxSetComponent switch (TxSetComponentType type)
//  {
//  case TXSET_COMP_TXS_MAYBE_DISCOUNTED_FEE:
//    struct
//    {
//      int64* baseFee;
//      TransactionEnvelope txs<>;
//    } txsMaybeDiscountedFee;
//  };

//  ===========================================================================
public class TxSetComponent implements XdrElement {
  public TxSetComponent() {}

  TxSetComponentType type;

  public TxSetComponentType getDiscriminant() {
    return this.type;
  }

  public void setDiscriminant(TxSetComponentType value) {
    this.type = value;
  }

  private TxSetComponentTxsMaybeDiscountedFee txsMaybeDiscountedFee;

  public TxSetComponentTxsMaybeDiscountedFee getTxsMaybeDiscountedFee() {
    return this.txsMaybeDiscountedFee;
  }

  public void setTxsMaybeDiscountedFee(TxSetComponentTxsMaybeDiscountedFee value) {
    this.txsMaybeDiscountedFee = value;
  }

  public static final class Builder {
    private TxSetComponentType discriminant;
    private TxSetComponentTxsMaybeDiscountedFee txsMaybeDiscountedFee;

    public Builder discriminant(TxSetComponentType discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public Builder txsMaybeDiscountedFee(
        TxSetComponentTxsMaybeDiscountedFee txsMaybeDiscountedFee) {
      this.txsMaybeDiscountedFee = txsMaybeDiscountedFee;
      return this;
    }

    public TxSetComponent build() {
      TxSetComponent val = new TxSetComponent();
      val.setDiscriminant(discriminant);
      val.setTxsMaybeDiscountedFee(this.txsMaybeDiscountedFee);
      return val;
    }
  }

  public static void encode(XdrDataOutputStream stream, TxSetComponent encodedTxSetComponent)
      throws IOException {
    // Xdrgen::AST::Identifier
    // TxSetComponentType
    stream.writeInt(encodedTxSetComponent.getDiscriminant().getValue());
    switch (encodedTxSetComponent.getDiscriminant()) {
      case TXSET_COMP_TXS_MAYBE_DISCOUNTED_FEE:
        TxSetComponentTxsMaybeDiscountedFee.encode(
            stream, encodedTxSetComponent.txsMaybeDiscountedFee);
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static TxSetComponent decode(XdrDataInputStream stream) throws IOException {
    TxSetComponent decodedTxSetComponent = new TxSetComponent();
    TxSetComponentType discriminant = TxSetComponentType.decode(stream);
    decodedTxSetComponent.setDiscriminant(discriminant);
    switch (decodedTxSetComponent.getDiscriminant()) {
      case TXSET_COMP_TXS_MAYBE_DISCOUNTED_FEE:
        decodedTxSetComponent.txsMaybeDiscountedFee =
            TxSetComponentTxsMaybeDiscountedFee.decode(stream);
        break;
    }
    return decodedTxSetComponent;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.txsMaybeDiscountedFee, this.type);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof TxSetComponent)) {
      return false;
    }

    TxSetComponent other = (TxSetComponent) object;
    return Objects.equals(this.txsMaybeDiscountedFee, other.txsMaybeDiscountedFee)
        && Objects.equals(this.type, other.type);
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

  public static TxSetComponent fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TxSetComponent fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static class TxSetComponentTxsMaybeDiscountedFee implements XdrElement {
    public TxSetComponentTxsMaybeDiscountedFee() {}

    private Int64 baseFee;

    public Int64 getBaseFee() {
      return this.baseFee;
    }

    public void setBaseFee(Int64 value) {
      this.baseFee = value;
    }

    private TransactionEnvelope[] txs;

    public TransactionEnvelope[] getTxs() {
      return this.txs;
    }

    public void setTxs(TransactionEnvelope[] value) {
      this.txs = value;
    }

    public static void encode(
        XdrDataOutputStream stream,
        TxSetComponentTxsMaybeDiscountedFee encodedTxSetComponentTxsMaybeDiscountedFee)
        throws IOException {
      if (encodedTxSetComponentTxsMaybeDiscountedFee.baseFee != null) {
        stream.writeInt(1);
        Int64.encode(stream, encodedTxSetComponentTxsMaybeDiscountedFee.baseFee);
      } else {
        stream.writeInt(0);
      }
      int txssize = encodedTxSetComponentTxsMaybeDiscountedFee.getTxs().length;
      stream.writeInt(txssize);
      for (int i = 0; i < txssize; i++) {
        TransactionEnvelope.encode(stream, encodedTxSetComponentTxsMaybeDiscountedFee.txs[i]);
      }
    }

    public void encode(XdrDataOutputStream stream) throws IOException {
      encode(stream, this);
    }

    public static TxSetComponentTxsMaybeDiscountedFee decode(XdrDataInputStream stream)
        throws IOException {
      TxSetComponentTxsMaybeDiscountedFee decodedTxSetComponentTxsMaybeDiscountedFee =
          new TxSetComponentTxsMaybeDiscountedFee();
      int baseFeePresent = stream.readInt();
      if (baseFeePresent != 0) {
        decodedTxSetComponentTxsMaybeDiscountedFee.baseFee = Int64.decode(stream);
      }
      int txssize = stream.readInt();
      decodedTxSetComponentTxsMaybeDiscountedFee.txs = new TransactionEnvelope[txssize];
      for (int i = 0; i < txssize; i++) {
        decodedTxSetComponentTxsMaybeDiscountedFee.txs[i] = TransactionEnvelope.decode(stream);
      }
      return decodedTxSetComponentTxsMaybeDiscountedFee;
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.baseFee, Arrays.hashCode(this.txs));
    }

    @Override
    public boolean equals(Object object) {
      if (!(object instanceof TxSetComponentTxsMaybeDiscountedFee)) {
        return false;
      }

      TxSetComponentTxsMaybeDiscountedFee other = (TxSetComponentTxsMaybeDiscountedFee) object;
      return Objects.equals(this.baseFee, other.baseFee) && Arrays.equals(this.txs, other.txs);
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

    public static TxSetComponentTxsMaybeDiscountedFee fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64.decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static TxSetComponentTxsMaybeDiscountedFee fromXdrByteArray(byte[] xdr)
        throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }

    public static final class Builder {
      private Int64 baseFee;
      private TransactionEnvelope[] txs;

      public Builder baseFee(Int64 baseFee) {
        this.baseFee = baseFee;
        return this;
      }

      public Builder txs(TransactionEnvelope[] txs) {
        this.txs = txs;
        return this;
      }

      public TxSetComponentTxsMaybeDiscountedFee build() {
        TxSetComponentTxsMaybeDiscountedFee val = new TxSetComponentTxsMaybeDiscountedFee();
        val.setBaseFee(this.baseFee);
        val.setTxs(this.txs);
        return val;
      }
    }
  }
}
