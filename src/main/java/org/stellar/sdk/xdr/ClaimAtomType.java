// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * ClaimAtomType's original definition in the XDR file is:
 *
 * <pre>
 * enum ClaimAtomType
 * {
 *     CLAIM_ATOM_TYPE_V0 = 0,
 *     CLAIM_ATOM_TYPE_ORDER_BOOK = 1,
 *     CLAIM_ATOM_TYPE_LIQUIDITY_POOL = 2
 * };
 * </pre>
 */
public enum ClaimAtomType implements XdrElement {
  CLAIM_ATOM_TYPE_V0(0),
  CLAIM_ATOM_TYPE_ORDER_BOOK(1),
  CLAIM_ATOM_TYPE_LIQUIDITY_POOL(2);

  private final int value;

  ClaimAtomType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static ClaimAtomType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return CLAIM_ATOM_TYPE_V0;
      case 1:
        return CLAIM_ATOM_TYPE_ORDER_BOOK;
      case 2:
        return CLAIM_ATOM_TYPE_LIQUIDITY_POOL;
      default:
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(value);
  }

  public static ClaimAtomType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ClaimAtomType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
