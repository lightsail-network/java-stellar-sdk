// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

// === xdr source ============================================================

//  enum LiquidityPoolDepositResultCode
//  {
//      // codes considered as "success" for the operation
//      LIQUIDITY_POOL_DEPOSIT_SUCCESS = 0,
//
//      // codes considered as "failure" for the operation
//      LIQUIDITY_POOL_DEPOSIT_MALFORMED = -1,      // bad input
//      LIQUIDITY_POOL_DEPOSIT_NO_TRUST = -2,       // no trust line for one of the
//                                                  // assets
//      LIQUIDITY_POOL_DEPOSIT_NOT_AUTHORIZED = -3, // not authorized for one of the
//                                                  // assets
//      LIQUIDITY_POOL_DEPOSIT_UNDERFUNDED = -4,    // not enough balance for one of
//                                                  // the assets
//      LIQUIDITY_POOL_DEPOSIT_LINE_FULL = -5,      // pool share trust line doesn't
//                                                  // have sufficient limit
//      LIQUIDITY_POOL_DEPOSIT_BAD_PRICE = -6,      // deposit price outside bounds
//      LIQUIDITY_POOL_DEPOSIT_POOL_FULL = -7       // pool reserves are full
//  };

//  ===========================================================================
public enum LiquidityPoolDepositResultCode implements XdrElement {
  LIQUIDITY_POOL_DEPOSIT_SUCCESS(0),
  LIQUIDITY_POOL_DEPOSIT_MALFORMED(-1),
  LIQUIDITY_POOL_DEPOSIT_NO_TRUST(-2),
  LIQUIDITY_POOL_DEPOSIT_NOT_AUTHORIZED(-3),
  LIQUIDITY_POOL_DEPOSIT_UNDERFUNDED(-4),
  LIQUIDITY_POOL_DEPOSIT_LINE_FULL(-5),
  LIQUIDITY_POOL_DEPOSIT_BAD_PRICE(-6),
  LIQUIDITY_POOL_DEPOSIT_POOL_FULL(-7),
  ;
  private int mValue;

  LiquidityPoolDepositResultCode(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static LiquidityPoolDepositResultCode decode(XdrDataInputStream stream)
      throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return LIQUIDITY_POOL_DEPOSIT_SUCCESS;
      case -1:
        return LIQUIDITY_POOL_DEPOSIT_MALFORMED;
      case -2:
        return LIQUIDITY_POOL_DEPOSIT_NO_TRUST;
      case -3:
        return LIQUIDITY_POOL_DEPOSIT_NOT_AUTHORIZED;
      case -4:
        return LIQUIDITY_POOL_DEPOSIT_UNDERFUNDED;
      case -5:
        return LIQUIDITY_POOL_DEPOSIT_LINE_FULL;
      case -6:
        return LIQUIDITY_POOL_DEPOSIT_BAD_PRICE;
      case -7:
        return LIQUIDITY_POOL_DEPOSIT_POOL_FULL;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, LiquidityPoolDepositResultCode value)
      throws IOException {
    stream.writeInt(value.getValue());
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
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

  public static LiquidityPoolDepositResultCode fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static LiquidityPoolDepositResultCode fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
