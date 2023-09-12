// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

// === xdr source ============================================================

//  struct ClaimOfferAtom
//  {
//      // emitted to identify the offer
//      AccountID sellerID; // Account that owns the offer
//      int64 offerID;
//
//      // amount and asset taken from the owner
//      Asset assetSold;
//      int64 amountSold;
//
//      // amount and asset sent to the owner
//      Asset assetBought;
//      int64 amountBought;
//  };

//  ===========================================================================
public class ClaimOfferAtom implements XdrElement {
  public ClaimOfferAtom() {}

  private AccountID sellerID;

  public AccountID getSellerID() {
    return this.sellerID;
  }

  public void setSellerID(AccountID value) {
    this.sellerID = value;
  }

  private Int64 offerID;

  public Int64 getOfferID() {
    return this.offerID;
  }

  public void setOfferID(Int64 value) {
    this.offerID = value;
  }

  private Asset assetSold;

  public Asset getAssetSold() {
    return this.assetSold;
  }

  public void setAssetSold(Asset value) {
    this.assetSold = value;
  }

  private Int64 amountSold;

  public Int64 getAmountSold() {
    return this.amountSold;
  }

  public void setAmountSold(Int64 value) {
    this.amountSold = value;
  }

  private Asset assetBought;

  public Asset getAssetBought() {
    return this.assetBought;
  }

  public void setAssetBought(Asset value) {
    this.assetBought = value;
  }

  private Int64 amountBought;

  public Int64 getAmountBought() {
    return this.amountBought;
  }

  public void setAmountBought(Int64 value) {
    this.amountBought = value;
  }

  public static void encode(XdrDataOutputStream stream, ClaimOfferAtom encodedClaimOfferAtom)
      throws IOException {
    AccountID.encode(stream, encodedClaimOfferAtom.sellerID);
    Int64.encode(stream, encodedClaimOfferAtom.offerID);
    Asset.encode(stream, encodedClaimOfferAtom.assetSold);
    Int64.encode(stream, encodedClaimOfferAtom.amountSold);
    Asset.encode(stream, encodedClaimOfferAtom.assetBought);
    Int64.encode(stream, encodedClaimOfferAtom.amountBought);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ClaimOfferAtom decode(XdrDataInputStream stream) throws IOException {
    ClaimOfferAtom decodedClaimOfferAtom = new ClaimOfferAtom();
    decodedClaimOfferAtom.sellerID = AccountID.decode(stream);
    decodedClaimOfferAtom.offerID = Int64.decode(stream);
    decodedClaimOfferAtom.assetSold = Asset.decode(stream);
    decodedClaimOfferAtom.amountSold = Int64.decode(stream);
    decodedClaimOfferAtom.assetBought = Asset.decode(stream);
    decodedClaimOfferAtom.amountBought = Int64.decode(stream);
    return decodedClaimOfferAtom;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.sellerID,
        this.offerID,
        this.assetSold,
        this.amountSold,
        this.assetBought,
        this.amountBought);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ClaimOfferAtom)) {
      return false;
    }

    ClaimOfferAtom other = (ClaimOfferAtom) object;
    return Objects.equals(this.sellerID, other.sellerID)
        && Objects.equals(this.offerID, other.offerID)
        && Objects.equals(this.assetSold, other.assetSold)
        && Objects.equals(this.amountSold, other.amountSold)
        && Objects.equals(this.assetBought, other.assetBought)
        && Objects.equals(this.amountBought, other.amountBought);
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

  public static ClaimOfferAtom fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ClaimOfferAtom fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private AccountID sellerID;
    private Int64 offerID;
    private Asset assetSold;
    private Int64 amountSold;
    private Asset assetBought;
    private Int64 amountBought;

    public Builder sellerID(AccountID sellerID) {
      this.sellerID = sellerID;
      return this;
    }

    public Builder offerID(Int64 offerID) {
      this.offerID = offerID;
      return this;
    }

    public Builder assetSold(Asset assetSold) {
      this.assetSold = assetSold;
      return this;
    }

    public Builder amountSold(Int64 amountSold) {
      this.amountSold = amountSold;
      return this;
    }

    public Builder assetBought(Asset assetBought) {
      this.assetBought = assetBought;
      return this;
    }

    public Builder amountBought(Int64 amountBought) {
      this.amountBought = amountBought;
      return this;
    }

    public ClaimOfferAtom build() {
      ClaimOfferAtom val = new ClaimOfferAtom();
      val.setSellerID(this.sellerID);
      val.setOfferID(this.offerID);
      val.setAssetSold(this.assetSold);
      val.setAmountSold(this.amountSold);
      val.setAssetBought(this.assetBought);
      val.setAmountBought(this.amountBought);
      return val;
    }
  }
}
