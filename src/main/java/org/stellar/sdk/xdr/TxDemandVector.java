// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.IOException;
import java.util.Arrays;

// === xdr source ============================================================

//  typedef Hash TxDemandVector<TX_DEMAND_VECTOR_MAX_SIZE>;

//  ===========================================================================
public class TxDemandVector implements XdrElement {
  private Hash[] TxDemandVector;

  public TxDemandVector() {}

  public TxDemandVector(Hash[] TxDemandVector) {
    this.TxDemandVector = TxDemandVector;
  }

  public Hash[] getTxDemandVector() {
    return this.TxDemandVector;
  }

  public void setTxDemandVector(Hash[] value) {
    this.TxDemandVector = value;
  }

  public static void encode(XdrDataOutputStream stream, TxDemandVector encodedTxDemandVector)
      throws IOException {
    int TxDemandVectorsize = encodedTxDemandVector.getTxDemandVector().length;
    stream.writeInt(TxDemandVectorsize);
    for (int i = 0; i < TxDemandVectorsize; i++) {
      Hash.encode(stream, encodedTxDemandVector.TxDemandVector[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static TxDemandVector decode(XdrDataInputStream stream) throws IOException {
    TxDemandVector decodedTxDemandVector = new TxDemandVector();
    int TxDemandVectorsize = stream.readInt();
    decodedTxDemandVector.TxDemandVector = new Hash[TxDemandVectorsize];
    for (int i = 0; i < TxDemandVectorsize; i++) {
      decodedTxDemandVector.TxDemandVector[i] = Hash.decode(stream);
    }
    return decodedTxDemandVector;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.TxDemandVector);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof TxDemandVector)) {
      return false;
    }

    TxDemandVector other = (TxDemandVector) object;
    return Arrays.equals(this.TxDemandVector, other.TxDemandVector);
  }
}