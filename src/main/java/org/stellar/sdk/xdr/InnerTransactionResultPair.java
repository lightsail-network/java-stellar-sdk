// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import com.google.common.base.Objects;
import java.io.IOException;

// === xdr source ============================================================

//  struct InnerTransactionResultPair
//  {
//      Hash transactionHash;          // hash of the inner transaction
//      InnerTransactionResult result; // result for the inner transaction
//  };

//  ===========================================================================
public class InnerTransactionResultPair implements XdrElement {
  public InnerTransactionResultPair() {}

  private Hash transactionHash;

  public Hash getTransactionHash() {
    return this.transactionHash;
  }

  public void setTransactionHash(Hash value) {
    this.transactionHash = value;
  }

  private InnerTransactionResult result;

  public InnerTransactionResult getResult() {
    return this.result;
  }

  public void setResult(InnerTransactionResult value) {
    this.result = value;
  }

  public static void encode(
      XdrDataOutputStream stream, InnerTransactionResultPair encodedInnerTransactionResultPair)
      throws IOException {
    Hash.encode(stream, encodedInnerTransactionResultPair.transactionHash);
    InnerTransactionResult.encode(stream, encodedInnerTransactionResultPair.result);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static InnerTransactionResultPair decode(XdrDataInputStream stream) throws IOException {
    InnerTransactionResultPair decodedInnerTransactionResultPair = new InnerTransactionResultPair();
    decodedInnerTransactionResultPair.transactionHash = Hash.decode(stream);
    decodedInnerTransactionResultPair.result = InnerTransactionResult.decode(stream);
    return decodedInnerTransactionResultPair;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.transactionHash, this.result);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof InnerTransactionResultPair)) {
      return false;
    }

    InnerTransactionResultPair other = (InnerTransactionResultPair) object;
    return Objects.equal(this.transactionHash, other.transactionHash)
        && Objects.equal(this.result, other.result);
  }

  public static final class Builder {
    private Hash transactionHash;
    private InnerTransactionResult result;

    public Builder transactionHash(Hash transactionHash) {
      this.transactionHash = transactionHash;
      return this;
    }

    public Builder result(InnerTransactionResult result) {
      this.result = result;
      return this;
    }

    public InnerTransactionResultPair build() {
      InnerTransactionResultPair val = new InnerTransactionResultPair();
      val.setTransactionHash(transactionHash);
      val.setResult(result);
      return val;
    }
  }
}
