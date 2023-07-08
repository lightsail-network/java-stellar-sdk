// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import com.google.common.base.Objects;
import java.io.IOException;

// === xdr source ============================================================

//  struct TransactionResultMeta
//  {
//      TransactionResultPair result;
//      LedgerEntryChanges feeProcessing;
//      TransactionMeta txApplyProcessing;
//  };

//  ===========================================================================
public class TransactionResultMeta implements XdrElement {
  public TransactionResultMeta() {}

  private TransactionResultPair result;

  public TransactionResultPair getResult() {
    return this.result;
  }

  public void setResult(TransactionResultPair value) {
    this.result = value;
  }

  private LedgerEntryChanges feeProcessing;

  public LedgerEntryChanges getFeeProcessing() {
    return this.feeProcessing;
  }

  public void setFeeProcessing(LedgerEntryChanges value) {
    this.feeProcessing = value;
  }

  private TransactionMeta txApplyProcessing;

  public TransactionMeta getTxApplyProcessing() {
    return this.txApplyProcessing;
  }

  public void setTxApplyProcessing(TransactionMeta value) {
    this.txApplyProcessing = value;
  }

  public static void encode(
      XdrDataOutputStream stream, TransactionResultMeta encodedTransactionResultMeta)
      throws IOException {
    TransactionResultPair.encode(stream, encodedTransactionResultMeta.result);
    LedgerEntryChanges.encode(stream, encodedTransactionResultMeta.feeProcessing);
    TransactionMeta.encode(stream, encodedTransactionResultMeta.txApplyProcessing);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static TransactionResultMeta decode(XdrDataInputStream stream) throws IOException {
    TransactionResultMeta decodedTransactionResultMeta = new TransactionResultMeta();
    decodedTransactionResultMeta.result = TransactionResultPair.decode(stream);
    decodedTransactionResultMeta.feeProcessing = LedgerEntryChanges.decode(stream);
    decodedTransactionResultMeta.txApplyProcessing = TransactionMeta.decode(stream);
    return decodedTransactionResultMeta;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.result, this.feeProcessing, this.txApplyProcessing);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof TransactionResultMeta)) {
      return false;
    }

    TransactionResultMeta other = (TransactionResultMeta) object;
    return Objects.equal(this.result, other.result)
        && Objects.equal(this.feeProcessing, other.feeProcessing)
        && Objects.equal(this.txApplyProcessing, other.txApplyProcessing);
  }

  public static final class Builder {
    private TransactionResultPair result;
    private LedgerEntryChanges feeProcessing;
    private TransactionMeta txApplyProcessing;

    public Builder result(TransactionResultPair result) {
      this.result = result;
      return this;
    }

    public Builder feeProcessing(LedgerEntryChanges feeProcessing) {
      this.feeProcessing = feeProcessing;
      return this;
    }

    public Builder txApplyProcessing(TransactionMeta txApplyProcessing) {
      this.txApplyProcessing = txApplyProcessing;
      return this;
    }

    public TransactionResultMeta build() {
      TransactionResultMeta val = new TransactionResultMeta();
      val.setResult(result);
      val.setFeeProcessing(feeProcessing);
      val.setTxApplyProcessing(txApplyProcessing);
      return val;
    }
  }
}
