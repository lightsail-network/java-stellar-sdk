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
 * PaymentOp's original definition in the XDR file is:
 *
 * <pre>
 * struct PaymentOp
 * {
 *     MuxedAccount destination; // recipient of the payment
 *     Asset asset;              // what they end up with
 *     int64 amount;             // amount they end up with
 * };
 * </pre>
 */
public class PaymentOp implements XdrElement {
  public PaymentOp() {}

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

  public static void encode(XdrDataOutputStream stream, PaymentOp encodedPaymentOp)
      throws IOException {
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
    return Objects.hash(this.destination, this.asset, this.amount);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof PaymentOp)) {
      return false;
    }

    PaymentOp other = (PaymentOp) object;
    return Objects.equals(this.destination, other.destination)
        && Objects.equals(this.asset, other.asset)
        && Objects.equals(this.amount, other.amount);
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

  public static PaymentOp fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static PaymentOp fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
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
      val.setDestination(this.destination);
      val.setAsset(this.asset);
      val.setAmount(this.amount);
      return val;
    }
  }
}
