// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * ChangeTrustResultCode's original definition in the XDR file is:
 *
 * <pre>
 * enum ChangeTrustResultCode
 * {
 *     // codes considered as "success" for the operation
 *     CHANGE_TRUST_SUCCESS = 0,
 *     // codes considered as "failure" for the operation
 *     CHANGE_TRUST_MALFORMED = -1,     // bad input
 *     CHANGE_TRUST_NO_ISSUER = -2,     // could not find issuer
 *     CHANGE_TRUST_INVALID_LIMIT = -3, // cannot drop limit below balance
 *                                      // cannot create with a limit of 0
 *     CHANGE_TRUST_LOW_RESERVE =
 *         -4, // not enough funds to create a new trust line,
 *     CHANGE_TRUST_SELF_NOT_ALLOWED = -5,   // trusting self is not allowed
 *     CHANGE_TRUST_TRUST_LINE_MISSING = -6, // Asset trustline is missing for pool
 *     CHANGE_TRUST_CANNOT_DELETE =
 *         -7, // Asset trustline is still referenced in a pool
 *     CHANGE_TRUST_NOT_AUTH_MAINTAIN_LIABILITIES =
 *         -8 // Asset trustline is deauthorized
 * };
 * </pre>
 */
public enum ChangeTrustResultCode implements XdrElement {
  CHANGE_TRUST_SUCCESS(0),
  CHANGE_TRUST_MALFORMED(-1),
  CHANGE_TRUST_NO_ISSUER(-2),
  CHANGE_TRUST_INVALID_LIMIT(-3),
  CHANGE_TRUST_LOW_RESERVE(-4),
  CHANGE_TRUST_SELF_NOT_ALLOWED(-5),
  CHANGE_TRUST_TRUST_LINE_MISSING(-6),
  CHANGE_TRUST_CANNOT_DELETE(-7),
  CHANGE_TRUST_NOT_AUTH_MAINTAIN_LIABILITIES(-8),
  ;
  private int mValue;

  ChangeTrustResultCode(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static ChangeTrustResultCode decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return CHANGE_TRUST_SUCCESS;
      case -1:
        return CHANGE_TRUST_MALFORMED;
      case -2:
        return CHANGE_TRUST_NO_ISSUER;
      case -3:
        return CHANGE_TRUST_INVALID_LIMIT;
      case -4:
        return CHANGE_TRUST_LOW_RESERVE;
      case -5:
        return CHANGE_TRUST_SELF_NOT_ALLOWED;
      case -6:
        return CHANGE_TRUST_TRUST_LINE_MISSING;
      case -7:
        return CHANGE_TRUST_CANNOT_DELETE;
      case -8:
        return CHANGE_TRUST_NOT_AUTH_MAINTAIN_LIABILITIES;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, ChangeTrustResultCode value)
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

  public static ChangeTrustResultCode fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ChangeTrustResultCode fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
