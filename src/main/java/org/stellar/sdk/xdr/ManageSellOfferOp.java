// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.stellar.sdk.Base64Factory;

/**
 * ManageSellOfferOp's original definition in the XDR file is:
 *
 * <pre>
 * struct ManageSellOfferOp
 * {
 *     Asset selling;
 *     Asset buying;
 *     int64 amount; // amount being sold. if set to 0, delete the offer
 *     Price price;  // price of thing being sold in terms of what you are buying
 *
 *     // 0=create a new offer, otherwise edit an existing offer
 *     int64 offerID;
 * };
 * </pre>
 */
public class ManageSellOfferOp implements XdrElement {
  public ManageSellOfferOp() {}

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

  private Int64 offerID;

  public Int64 getOfferID() {
    return this.offerID;
  }

  public void setOfferID(Int64 value) {
    this.offerID = value;
  }

  public static void encode(XdrDataOutputStream stream, ManageSellOfferOp encodedManageSellOfferOp)
      throws IOException {
    Asset.encode(stream, encodedManageSellOfferOp.selling);
    Asset.encode(stream, encodedManageSellOfferOp.buying);
    Int64.encode(stream, encodedManageSellOfferOp.amount);
    Price.encode(stream, encodedManageSellOfferOp.price);
    Int64.encode(stream, encodedManageSellOfferOp.offerID);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ManageSellOfferOp decode(XdrDataInputStream stream) throws IOException {
    ManageSellOfferOp decodedManageSellOfferOp = new ManageSellOfferOp();
    decodedManageSellOfferOp.selling = Asset.decode(stream);
    decodedManageSellOfferOp.buying = Asset.decode(stream);
    decodedManageSellOfferOp.amount = Int64.decode(stream);
    decodedManageSellOfferOp.price = Price.decode(stream);
    decodedManageSellOfferOp.offerID = Int64.decode(stream);
    return decodedManageSellOfferOp;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.selling, this.buying, this.amount, this.price, this.offerID);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ManageSellOfferOp)) {
      return false;
    }

    ManageSellOfferOp other = (ManageSellOfferOp) object;
    return Objects.equals(this.selling, other.selling)
        && Objects.equals(this.buying, other.buying)
        && Objects.equals(this.amount, other.amount)
        && Objects.equals(this.price, other.price)
        && Objects.equals(this.offerID, other.offerID);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64Factory.getInstance().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static ManageSellOfferOp fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ManageSellOfferOp fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private Asset selling;
    private Asset buying;
    private Int64 amount;
    private Price price;
    private Int64 offerID;

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

    public Builder offerID(Int64 offerID) {
      this.offerID = offerID;
      return this;
    }

    public ManageSellOfferOp build() {
      ManageSellOfferOp val = new ManageSellOfferOp();
      val.setSelling(this.selling);
      val.setBuying(this.buying);
      val.setAmount(this.amount);
      val.setPrice(this.price);
      val.setOfferID(this.offerID);
      return val;
    }
  }
}
