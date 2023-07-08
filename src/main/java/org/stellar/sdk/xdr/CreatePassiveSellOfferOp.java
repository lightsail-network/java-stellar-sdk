// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import com.google.common.base.Objects;
import java.io.IOException;

// === xdr source ============================================================

//  struct CreatePassiveSellOfferOp
//  {
//      Asset selling; // A
//      Asset buying;  // B
//      int64 amount;  // amount taker gets
//      Price price;   // cost of A in terms of B
//  };

//  ===========================================================================
public class CreatePassiveSellOfferOp implements XdrElement {
  public CreatePassiveSellOfferOp() {}

  private Asset selling;

  public Asset getSelling() {
    return this.selling;
  }

  public void setSelling(Asset value) {
    this.selling = value;
  }

  private Asset buying;

  public Asset getBuying() {
    return this.buying;
  }

  public void setBuying(Asset value) {
    this.buying = value;
  }

  private Int64 amount;

  public Int64 getAmount() {
    return this.amount;
  }

  public void setAmount(Int64 value) {
    this.amount = value;
  }

  private Price price;

  public Price getPrice() {
    return this.price;
  }

  public void setPrice(Price value) {
    this.price = value;
  }

  public static void encode(
      XdrDataOutputStream stream, CreatePassiveSellOfferOp encodedCreatePassiveSellOfferOp)
      throws IOException {
    Asset.encode(stream, encodedCreatePassiveSellOfferOp.selling);
    Asset.encode(stream, encodedCreatePassiveSellOfferOp.buying);
    Int64.encode(stream, encodedCreatePassiveSellOfferOp.amount);
    Price.encode(stream, encodedCreatePassiveSellOfferOp.price);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static CreatePassiveSellOfferOp decode(XdrDataInputStream stream) throws IOException {
    CreatePassiveSellOfferOp decodedCreatePassiveSellOfferOp = new CreatePassiveSellOfferOp();
    decodedCreatePassiveSellOfferOp.selling = Asset.decode(stream);
    decodedCreatePassiveSellOfferOp.buying = Asset.decode(stream);
    decodedCreatePassiveSellOfferOp.amount = Int64.decode(stream);
    decodedCreatePassiveSellOfferOp.price = Price.decode(stream);
    return decodedCreatePassiveSellOfferOp;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.selling, this.buying, this.amount, this.price);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof CreatePassiveSellOfferOp)) {
      return false;
    }

    CreatePassiveSellOfferOp other = (CreatePassiveSellOfferOp) object;
    return Objects.equal(this.selling, other.selling)
        && Objects.equal(this.buying, other.buying)
        && Objects.equal(this.amount, other.amount)
        && Objects.equal(this.price, other.price);
  }

  public static final class Builder {
    private Asset selling;
    private Asset buying;
    private Int64 amount;
    private Price price;

    public Builder selling(Asset selling) {
      this.selling = selling;
      return this;
    }

    public Builder buying(Asset buying) {
      this.buying = buying;
      return this;
    }

    public Builder amount(Int64 amount) {
      this.amount = amount;
      return this;
    }

    public Builder price(Price price) {
      this.price = price;
      return this;
    }

    public CreatePassiveSellOfferOp build() {
      CreatePassiveSellOfferOp val = new CreatePassiveSellOfferOp();
      val.setSelling(selling);
      val.setBuying(buying);
      val.setAmount(amount);
      val.setPrice(price);
      return val;
    }
  }
}
