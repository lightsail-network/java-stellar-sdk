// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  enum ManageSellOfferResultCode
//  {
//      // codes considered as "success" for the operation
//      MANAGE_SELL_OFFER_SUCCESS = 0,
//
//      // codes considered as "failure" for the operation
//      MANAGE_SELL_OFFER_MALFORMED = -1, // generated offer would be invalid
//      MANAGE_SELL_OFFER_SELL_NO_TRUST =
//          -2,                              // no trust line for what we're selling
//      MANAGE_SELL_OFFER_BUY_NO_TRUST = -3, // no trust line for what we're buying
//      MANAGE_SELL_OFFER_SELL_NOT_AUTHORIZED = -4, // not authorized to sell
//      MANAGE_SELL_OFFER_BUY_NOT_AUTHORIZED = -5,  // not authorized to buy
//      MANAGE_SELL_OFFER_LINE_FULL = -6, // can't receive more of what it's buying
//      MANAGE_SELL_OFFER_UNDERFUNDED = -7, // doesn't hold what it's trying to sell
//      MANAGE_SELL_OFFER_CROSS_SELF =
//          -8, // would cross an offer from the same user
//      MANAGE_SELL_OFFER_SELL_NO_ISSUER = -9, // no issuer for what we're selling
//      MANAGE_SELL_OFFER_BUY_NO_ISSUER = -10, // no issuer for what we're buying
//
//      // update errors
//      MANAGE_SELL_OFFER_NOT_FOUND =
//          -11, // offerID does not match an existing offer
//
//      MANAGE_SELL_OFFER_LOW_RESERVE =
//          -12 // not enough funds to create a new Offer
//  };

//  ===========================================================================
public enum ManageSellOfferResultCode implements XdrElement {
  MANAGE_SELL_OFFER_SUCCESS(0),
  MANAGE_SELL_OFFER_MALFORMED(-1),
  MANAGE_SELL_OFFER_SELL_NO_TRUST(-2),
  MANAGE_SELL_OFFER_BUY_NO_TRUST(-3),
  MANAGE_SELL_OFFER_SELL_NOT_AUTHORIZED(-4),
  MANAGE_SELL_OFFER_BUY_NOT_AUTHORIZED(-5),
  MANAGE_SELL_OFFER_LINE_FULL(-6),
  MANAGE_SELL_OFFER_UNDERFUNDED(-7),
  MANAGE_SELL_OFFER_CROSS_SELF(-8),
  MANAGE_SELL_OFFER_SELL_NO_ISSUER(-9),
  MANAGE_SELL_OFFER_BUY_NO_ISSUER(-10),
  MANAGE_SELL_OFFER_NOT_FOUND(-11),
  MANAGE_SELL_OFFER_LOW_RESERVE(-12),
  ;
  private int mValue;

  ManageSellOfferResultCode(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static ManageSellOfferResultCode decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return MANAGE_SELL_OFFER_SUCCESS;
      case -1:
        return MANAGE_SELL_OFFER_MALFORMED;
      case -2:
        return MANAGE_SELL_OFFER_SELL_NO_TRUST;
      case -3:
        return MANAGE_SELL_OFFER_BUY_NO_TRUST;
      case -4:
        return MANAGE_SELL_OFFER_SELL_NOT_AUTHORIZED;
      case -5:
        return MANAGE_SELL_OFFER_BUY_NOT_AUTHORIZED;
      case -6:
        return MANAGE_SELL_OFFER_LINE_FULL;
      case -7:
        return MANAGE_SELL_OFFER_UNDERFUNDED;
      case -8:
        return MANAGE_SELL_OFFER_CROSS_SELF;
      case -9:
        return MANAGE_SELL_OFFER_SELL_NO_ISSUER;
      case -10:
        return MANAGE_SELL_OFFER_BUY_NO_ISSUER;
      case -11:
        return MANAGE_SELL_OFFER_NOT_FOUND;
      case -12:
        return MANAGE_SELL_OFFER_LOW_RESERVE;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, ManageSellOfferResultCode value)
      throws IOException {
    stream.writeInt(value.getValue());
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static ManageSellOfferResultCode fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ManageSellOfferResultCode fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
