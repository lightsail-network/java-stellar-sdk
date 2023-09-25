// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

// === xdr source ============================================================

//  struct PathPaymentStrictReceiveOp
//  {
//      Asset sendAsset; // asset we pay with
//      int64 sendMax;   // the maximum amount of sendAsset to
//                       // send (excluding fees).
//                       // The operation will fail if can't be met
//
//      MuxedAccount destination; // recipient of the payment
//      Asset destAsset;          // what they end up with
//      int64 destAmount;         // amount they end up with
//
//      Asset path<5>; // additional hops it must go through to get there
//  };

//  ===========================================================================
public class PathPaymentStrictReceiveOp implements XdrElement {
  public PathPaymentStrictReceiveOp() {}

  private Asset sendAsset;

  public Asset getSendAsset() {
    return this.sendAsset;
  }

  public void setSendAsset(Asset value) {
    this.sendAsset = value;
  }

  private Int64 sendMax;

  public Int64 getSendMax() {
    return this.sendMax;
  }

  public void setSendMax(Int64 value) {
    this.sendMax = value;
  }

  private MuxedAccount destination;

  public MuxedAccount getDestination() {
    return this.destination;
  }

  public void setDestination(MuxedAccount value) {
    this.destination = value;
  }

  private Asset destAsset;

  public Asset getDestAsset() {
    return this.destAsset;
  }

  public void setDestAsset(Asset value) {
    this.destAsset = value;
  }

  private Int64 destAmount;

  public Int64 getDestAmount() {
    return this.destAmount;
  }

  public void setDestAmount(Int64 value) {
    this.destAmount = value;
  }

  private Asset[] path;

  public Asset[] getPath() {
    return this.path;
  }

  public void setPath(Asset[] value) {
    this.path = value;
  }

  public static void encode(
      XdrDataOutputStream stream, PathPaymentStrictReceiveOp encodedPathPaymentStrictReceiveOp)
      throws IOException {
    Asset.encode(stream, encodedPathPaymentStrictReceiveOp.sendAsset);
    Int64.encode(stream, encodedPathPaymentStrictReceiveOp.sendMax);
    MuxedAccount.encode(stream, encodedPathPaymentStrictReceiveOp.destination);
    Asset.encode(stream, encodedPathPaymentStrictReceiveOp.destAsset);
    Int64.encode(stream, encodedPathPaymentStrictReceiveOp.destAmount);
    int pathsize = encodedPathPaymentStrictReceiveOp.getPath().length;
    stream.writeInt(pathsize);
    for (int i = 0; i < pathsize; i++) {
      Asset.encode(stream, encodedPathPaymentStrictReceiveOp.path[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static PathPaymentStrictReceiveOp decode(XdrDataInputStream stream) throws IOException {
    PathPaymentStrictReceiveOp decodedPathPaymentStrictReceiveOp = new PathPaymentStrictReceiveOp();
    decodedPathPaymentStrictReceiveOp.sendAsset = Asset.decode(stream);
    decodedPathPaymentStrictReceiveOp.sendMax = Int64.decode(stream);
    decodedPathPaymentStrictReceiveOp.destination = MuxedAccount.decode(stream);
    decodedPathPaymentStrictReceiveOp.destAsset = Asset.decode(stream);
    decodedPathPaymentStrictReceiveOp.destAmount = Int64.decode(stream);
    int pathsize = stream.readInt();
    decodedPathPaymentStrictReceiveOp.path = new Asset[pathsize];
    for (int i = 0; i < pathsize; i++) {
      decodedPathPaymentStrictReceiveOp.path[i] = Asset.decode(stream);
    }
    return decodedPathPaymentStrictReceiveOp;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.sendAsset,
        this.sendMax,
        this.destination,
        this.destAsset,
        this.destAmount,
        Arrays.hashCode(this.path));
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof PathPaymentStrictReceiveOp)) {
      return false;
    }

    PathPaymentStrictReceiveOp other = (PathPaymentStrictReceiveOp) object;
    return Objects.equals(this.sendAsset, other.sendAsset)
        && Objects.equals(this.sendMax, other.sendMax)
        && Objects.equals(this.destination, other.destination)
        && Objects.equals(this.destAsset, other.destAsset)
        && Objects.equals(this.destAmount, other.destAmount)
        && Arrays.equals(this.path, other.path);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.getEncoder().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static PathPaymentStrictReceiveOp fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static PathPaymentStrictReceiveOp fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private Asset sendAsset;
    private Int64 sendMax;
    private MuxedAccount destination;
    private Asset destAsset;
    private Int64 destAmount;
    private Asset[] path;

    public Builder sendAsset(Asset sendAsset) {
      this.sendAsset = sendAsset;
      return this;
    }

    public Builder sendMax(Int64 sendMax) {
      this.sendMax = sendMax;
      return this;
    }

    public Builder destination(MuxedAccount destination) {
      this.destination = destination;
      return this;
    }

    public Builder destAsset(Asset destAsset) {
      this.destAsset = destAsset;
      return this;
    }

    public Builder destAmount(Int64 destAmount) {
      this.destAmount = destAmount;
      return this;
    }

    public Builder path(Asset[] path) {
      this.path = path;
      return this;
    }

    public PathPaymentStrictReceiveOp build() {
      PathPaymentStrictReceiveOp val = new PathPaymentStrictReceiveOp();
      val.setSendAsset(this.sendAsset);
      val.setSendMax(this.sendMax);
      val.setDestination(this.destination);
      val.setDestAsset(this.destAsset);
      val.setDestAmount(this.destAmount);
      val.setPath(this.path);
      return val;
    }
  }
}
