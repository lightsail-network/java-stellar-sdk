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
 * SimplePaymentResult's original definition in the XDR file is:
 *
 * <pre>
 * struct SimplePaymentResult
 * {
 *     AccountID destination;
 *     Asset asset;
 *     int64 amount;
 * };
 * </pre>
 */
public class SimplePaymentResult implements XdrElement {
  public SimplePaymentResult() {}

  private AccountID destination;

  public AccountID getDestination() {
    return this.destination;
  }

  public void setDestination(AccountID value) {
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

  public static void encode(
      XdrDataOutputStream stream, SimplePaymentResult encodedSimplePaymentResult)
      throws IOException {
    AccountID.encode(stream, encodedSimplePaymentResult.destination);
    Asset.encode(stream, encodedSimplePaymentResult.asset);
    Int64.encode(stream, encodedSimplePaymentResult.amount);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SimplePaymentResult decode(XdrDataInputStream stream) throws IOException {
    SimplePaymentResult decodedSimplePaymentResult = new SimplePaymentResult();
    decodedSimplePaymentResult.destination = AccountID.decode(stream);
    decodedSimplePaymentResult.asset = Asset.decode(stream);
    decodedSimplePaymentResult.amount = Int64.decode(stream);
    return decodedSimplePaymentResult;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.destination, this.asset, this.amount);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SimplePaymentResult)) {
      return false;
    }

    SimplePaymentResult other = (SimplePaymentResult) object;
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

  public static SimplePaymentResult fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SimplePaymentResult fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private AccountID destination;
    private Asset asset;
    private Int64 amount;

    public Builder destination(AccountID destination) {
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

    public SimplePaymentResult build() {
      SimplePaymentResult val = new SimplePaymentResult();
      val.setDestination(this.destination);
      val.setAsset(this.asset);
      val.setAmount(this.amount);
      return val;
    }
  }
}
