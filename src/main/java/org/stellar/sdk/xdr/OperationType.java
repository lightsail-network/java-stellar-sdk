// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

// === xdr source ============================================================

//  enum OperationType
//  {
//      CREATE_ACCOUNT = 0,
//      PAYMENT = 1,
//      PATH_PAYMENT_STRICT_RECEIVE = 2,
//      MANAGE_SELL_OFFER = 3,
//      CREATE_PASSIVE_SELL_OFFER = 4,
//      SET_OPTIONS = 5,
//      CHANGE_TRUST = 6,
//      ALLOW_TRUST = 7,
//      ACCOUNT_MERGE = 8,
//      INFLATION = 9,
//      MANAGE_DATA = 10,
//      BUMP_SEQUENCE = 11,
//      MANAGE_BUY_OFFER = 12,
//      PATH_PAYMENT_STRICT_SEND = 13,
//      CREATE_CLAIMABLE_BALANCE = 14,
//      CLAIM_CLAIMABLE_BALANCE = 15,
//      BEGIN_SPONSORING_FUTURE_RESERVES = 16,
//      END_SPONSORING_FUTURE_RESERVES = 17,
//      REVOKE_SPONSORSHIP = 18,
//      CLAWBACK = 19,
//      CLAWBACK_CLAIMABLE_BALANCE = 20,
//      SET_TRUST_LINE_FLAGS = 21,
//      LIQUIDITY_POOL_DEPOSIT = 22,
//      LIQUIDITY_POOL_WITHDRAW = 23,
//      INVOKE_HOST_FUNCTION = 24,
//      BUMP_FOOTPRINT_EXPIRATION = 25,
//      RESTORE_FOOTPRINT = 26
//  };

//  ===========================================================================
public enum OperationType implements XdrElement {
  CREATE_ACCOUNT(0),
  PAYMENT(1),
  PATH_PAYMENT_STRICT_RECEIVE(2),
  MANAGE_SELL_OFFER(3),
  CREATE_PASSIVE_SELL_OFFER(4),
  SET_OPTIONS(5),
  CHANGE_TRUST(6),
  ALLOW_TRUST(7),
  ACCOUNT_MERGE(8),
  INFLATION(9),
  MANAGE_DATA(10),
  BUMP_SEQUENCE(11),
  MANAGE_BUY_OFFER(12),
  PATH_PAYMENT_STRICT_SEND(13),
  CREATE_CLAIMABLE_BALANCE(14),
  CLAIM_CLAIMABLE_BALANCE(15),
  BEGIN_SPONSORING_FUTURE_RESERVES(16),
  END_SPONSORING_FUTURE_RESERVES(17),
  REVOKE_SPONSORSHIP(18),
  CLAWBACK(19),
  CLAWBACK_CLAIMABLE_BALANCE(20),
  SET_TRUST_LINE_FLAGS(21),
  LIQUIDITY_POOL_DEPOSIT(22),
  LIQUIDITY_POOL_WITHDRAW(23),
  INVOKE_HOST_FUNCTION(24),
  BUMP_FOOTPRINT_EXPIRATION(25),
  RESTORE_FOOTPRINT(26),
  ;
  private int mValue;

  OperationType(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static OperationType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return CREATE_ACCOUNT;
      case 1:
        return PAYMENT;
      case 2:
        return PATH_PAYMENT_STRICT_RECEIVE;
      case 3:
        return MANAGE_SELL_OFFER;
      case 4:
        return CREATE_PASSIVE_SELL_OFFER;
      case 5:
        return SET_OPTIONS;
      case 6:
        return CHANGE_TRUST;
      case 7:
        return ALLOW_TRUST;
      case 8:
        return ACCOUNT_MERGE;
      case 9:
        return INFLATION;
      case 10:
        return MANAGE_DATA;
      case 11:
        return BUMP_SEQUENCE;
      case 12:
        return MANAGE_BUY_OFFER;
      case 13:
        return PATH_PAYMENT_STRICT_SEND;
      case 14:
        return CREATE_CLAIMABLE_BALANCE;
      case 15:
        return CLAIM_CLAIMABLE_BALANCE;
      case 16:
        return BEGIN_SPONSORING_FUTURE_RESERVES;
      case 17:
        return END_SPONSORING_FUTURE_RESERVES;
      case 18:
        return REVOKE_SPONSORSHIP;
      case 19:
        return CLAWBACK;
      case 20:
        return CLAWBACK_CLAIMABLE_BALANCE;
      case 21:
        return SET_TRUST_LINE_FLAGS;
      case 22:
        return LIQUIDITY_POOL_DEPOSIT;
      case 23:
        return LIQUIDITY_POOL_WITHDRAW;
      case 24:
        return INVOKE_HOST_FUNCTION;
      case 25:
        return BUMP_FOOTPRINT_EXPIRATION;
      case 26:
        return RESTORE_FOOTPRINT;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, OperationType value) throws IOException {
    stream.writeInt(value.getValue());
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
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

  public static OperationType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static OperationType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
