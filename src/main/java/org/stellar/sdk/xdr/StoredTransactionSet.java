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

//  union StoredTransactionSet switch (int v)
//  {
//  case 0:
//  	TransactionSet txSet;
//  case 1:
//  	GeneralizedTransactionSet generalizedTxSet;
//  };

//  ===========================================================================
public class StoredTransactionSet implements XdrElement {
  public StoredTransactionSet() {}

  Integer v;

  public Integer getDiscriminant() {
    return this.v;
  }

  public void setDiscriminant(Integer value) {
    this.v = value;
  }

  private TransactionSet txSet;

  public TransactionSet getTxSet() {
    return this.txSet;
  }

  public void setTxSet(TransactionSet value) {
    this.txSet = value;
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
    private TransactionSet txSet;
    private GeneralizedTransactionSet generalizedTxSet;

    public Builder discriminant(Integer discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public Builder txSet(TransactionSet txSet) {
      this.txSet = txSet;
      return this;
    }

    public Builder generalizedTxSet(GeneralizedTransactionSet generalizedTxSet) {
      this.generalizedTxSet = generalizedTxSet;
      return this;
    }

    public StoredTransactionSet build() {
      StoredTransactionSet val = new StoredTransactionSet();
      val.setDiscriminant(discriminant);
      val.setTxSet(this.txSet);
      val.setGeneralizedTxSet(this.generalizedTxSet);
      return val;
    }
  }

  public static void encode(
      XdrDataOutputStream stream, StoredTransactionSet encodedStoredTransactionSet)
      throws IOException {
    // Xdrgen::AST::Typespecs::Int
    // Integer
    stream.writeInt(encodedStoredTransactionSet.getDiscriminant().intValue());
    switch (encodedStoredTransactionSet.getDiscriminant()) {
      case 0:
        TransactionSet.encode(stream, encodedStoredTransactionSet.txSet);
        break;
      case 1:
        GeneralizedTransactionSet.encode(stream, encodedStoredTransactionSet.generalizedTxSet);
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static StoredTransactionSet decode(XdrDataInputStream stream) throws IOException {
    StoredTransactionSet decodedStoredTransactionSet = new StoredTransactionSet();
    Integer discriminant = stream.readInt();
    decodedStoredTransactionSet.setDiscriminant(discriminant);
    switch (decodedStoredTransactionSet.getDiscriminant()) {
      case 0:
        decodedStoredTransactionSet.txSet = TransactionSet.decode(stream);
        break;
      case 1:
        decodedStoredTransactionSet.generalizedTxSet = GeneralizedTransactionSet.decode(stream);
        break;
    }
    return decodedStoredTransactionSet;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.txSet, this.generalizedTxSet, this.v);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof StoredTransactionSet)) {
      return false;
    }

    StoredTransactionSet other = (StoredTransactionSet) object;
    return Objects.equals(this.txSet, other.txSet)
        && Objects.equals(this.generalizedTxSet, other.generalizedTxSet)
        && Objects.equals(this.v, other.v);
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

  public static StoredTransactionSet fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static StoredTransactionSet fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
