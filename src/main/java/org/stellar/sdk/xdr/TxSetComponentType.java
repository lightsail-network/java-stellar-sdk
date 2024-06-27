// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * TxSetComponentType's original definition in the XDR file is:
 *
 * <pre>
 * enum TxSetComponentType
 * {
 *   // txs with effective fee &lt;= bid derived from a base fee (if any).
 *   // If base fee is not specified, no discount is applied.
 *   TXSET_COMP_TXS_MAYBE_DISCOUNTED_FEE = 0
 * };
 * </pre>
 */
public enum TxSetComponentType implements XdrElement {
  TXSET_COMP_TXS_MAYBE_DISCOUNTED_FEE(0);

  private final int value;

  TxSetComponentType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static TxSetComponentType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return TXSET_COMP_TXS_MAYBE_DISCOUNTED_FEE;
      default:
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(value);
  }

  public static TxSetComponentType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TxSetComponentType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
