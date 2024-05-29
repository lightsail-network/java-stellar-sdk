// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * LedgerEntryType's original definition in the XDR file is:
 *
 * <pre>
 * enum LedgerEntryType
 * {
 *     ACCOUNT = 0,
 *     TRUSTLINE = 1,
 *     OFFER = 2,
 *     DATA = 3,
 *     CLAIMABLE_BALANCE = 4,
 *     LIQUIDITY_POOL = 5,
 *     CONTRACT_DATA = 6,
 *     CONTRACT_CODE = 7,
 *     CONFIG_SETTING = 8,
 *     TTL = 9
 * };
 * </pre>
 */
public enum LedgerEntryType implements XdrElement {
  ACCOUNT(0),
  TRUSTLINE(1),
  OFFER(2),
  DATA(3),
  CLAIMABLE_BALANCE(4),
  LIQUIDITY_POOL(5),
  CONTRACT_DATA(6),
  CONTRACT_CODE(7),
  CONFIG_SETTING(8),
  TTL(9);

  private final int value;

  LedgerEntryType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static LedgerEntryType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return ACCOUNT;
      case 1:
        return TRUSTLINE;
      case 2:
        return OFFER;
      case 3:
        return DATA;
      case 4:
        return CLAIMABLE_BALANCE;
      case 5:
        return LIQUIDITY_POOL;
      case 6:
        return CONTRACT_DATA;
      case 7:
        return CONTRACT_CODE;
      case 8:
        return CONFIG_SETTING;
      case 9:
        return TTL;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(value);
  }

  public static LedgerEntryType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static LedgerEntryType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
