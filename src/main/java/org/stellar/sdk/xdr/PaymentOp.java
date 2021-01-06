// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;


import java.io.IOException;

import com.google.common.base.Objects;

// === xdr source ============================================================

//  struct PaymentOp
//  {
//      MuxedAccount destination; // recipient of the payment
//      Asset asset;              // what they end up with
//      int64 amount;             // amount they end up with
//  };

//  ===========================================================================
public class PaymentOp implements XdrElement {
  public PaymentOp () {}
  private MuxedAccount destination;
  public MuxedAccount getDestination() {
    return this.destination;
  }
  public void setDestination(MuxedAccount value) {
    this.destination = value;
  }
  private Asset asset;
  public Asset getAsset() {
    return this.asset;
  }
  public void setAsset(Asset value) {
    this.asset = value;
  }
  private Int64 amount;
  public Int64 getAmount() {
    return this.amount;
  }
  public void setAmount(Int64 value) {
    this.amount = value;
  }
  public static void encode(XdrDataOutputStream stream, PaymentOp encodedPaymentOp) throws IOException{
    MuxedAccount.encode(stream, encodedPaymentOp.destination);
    Asset.encode(stream, encodedPaymentOp.asset);
    Int64.encode(stream, encodedPaymentOp.amount);
  }
  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }
  public static PaymentOp decode(XdrDataInputStream stream) throws IOException {
    PaymentOp decodedPaymentOp = new PaymentOp();
    decodedPaymentOp.destination = MuxedAccount.decode(stream);
    decodedPaymentOp.asset = Asset.decode(stream);
    decodedPaymentOp.amount = Int64.decode(stream);
    return decodedPaymentOp;
  }
  @Override
  public int hashCode() {
    return Objects.hashCode(this.destination, this.asset, this.amount);
  }
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof PaymentOp)) {
      return false;
    }

    PaymentOp other = (PaymentOp) object;
    return Objects.equal(this.destination, other.destination) && Objects.equal(this.asset, other.asset) && Objects.equal(this.amount, other.amount);
  }

  public static final class Builder {
    private MuxedAccount destination;
    private Asset asset;
    private Int64 amount;

    public Builder destination(MuxedAccount destination) {
      this.destination = destination;
      return this;
    }

    public Builder asset(Asset asset) {
      this.asset = asset;
      return this;
    }

    public Builder amount(Int64 amount) {
      this.amount = amount;
      return this;
    }

    public PaymentOp build() {
      PaymentOp val = new PaymentOp();
      val.setDestination(destination);
      val.setAsset(asset);
      val.setAmount(amount);
      return val;
    }
  }
}
