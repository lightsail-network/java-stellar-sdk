// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * LedgerUpgradeType's original definition in the XDR file is:
 *
 * <pre>
 * enum LedgerUpgradeType
 * {
 *     LEDGER_UPGRADE_VERSION = 1,
 *     LEDGER_UPGRADE_BASE_FEE = 2,
 *     LEDGER_UPGRADE_MAX_TX_SET_SIZE = 3,
 *     LEDGER_UPGRADE_BASE_RESERVE = 4,
 *     LEDGER_UPGRADE_FLAGS = 5,
 *     LEDGER_UPGRADE_CONFIG = 6,
 *     LEDGER_UPGRADE_MAX_SOROBAN_TX_SET_SIZE = 7
 * };
 * </pre>
 */
public enum LedgerUpgradeType implements XdrElement {
  LEDGER_UPGRADE_VERSION(1),
  LEDGER_UPGRADE_BASE_FEE(2),
  LEDGER_UPGRADE_MAX_TX_SET_SIZE(3),
  LEDGER_UPGRADE_BASE_RESERVE(4),
  LEDGER_UPGRADE_FLAGS(5),
  LEDGER_UPGRADE_CONFIG(6),
  LEDGER_UPGRADE_MAX_SOROBAN_TX_SET_SIZE(7);

  private final int value;

  LedgerUpgradeType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static LedgerUpgradeType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 1:
        return LEDGER_UPGRADE_VERSION;
      case 2:
        return LEDGER_UPGRADE_BASE_FEE;
      case 3:
        return LEDGER_UPGRADE_MAX_TX_SET_SIZE;
      case 4:
        return LEDGER_UPGRADE_BASE_RESERVE;
      case 5:
        return LEDGER_UPGRADE_FLAGS;
      case 6:
        return LEDGER_UPGRADE_CONFIG;
      case 7:
        return LEDGER_UPGRADE_MAX_SOROBAN_TX_SET_SIZE;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, LedgerUpgradeType value)
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

  public static LedgerUpgradeType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static LedgerUpgradeType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
