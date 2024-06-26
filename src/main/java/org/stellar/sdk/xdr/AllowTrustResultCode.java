// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * AllowTrustResultCode's original definition in the XDR file is:
 *
 * <pre>
 * enum AllowTrustResultCode
 * {
 *     // codes considered as "success" for the operation
 *     ALLOW_TRUST_SUCCESS = 0,
 *     // codes considered as "failure" for the operation
 *     ALLOW_TRUST_MALFORMED = -1,     // asset is not ASSET_TYPE_ALPHANUM
 *     ALLOW_TRUST_NO_TRUST_LINE = -2, // trustor does not have a trustline
 *                                     // source account does not require trust
 *     ALLOW_TRUST_TRUST_NOT_REQUIRED = -3,
 *     ALLOW_TRUST_CANT_REVOKE = -4,      // source account can't revoke trust,
 *     ALLOW_TRUST_SELF_NOT_ALLOWED = -5, // trusting self is not allowed
 *     ALLOW_TRUST_LOW_RESERVE = -6       // claimable balances can't be created
 *                                        // on revoke due to low reserves
 * };
 * </pre>
 */
public enum AllowTrustResultCode implements XdrElement {
  ALLOW_TRUST_SUCCESS(0),
  ALLOW_TRUST_MALFORMED(-1),
  ALLOW_TRUST_NO_TRUST_LINE(-2),
  ALLOW_TRUST_TRUST_NOT_REQUIRED(-3),
  ALLOW_TRUST_CANT_REVOKE(-4),
  ALLOW_TRUST_SELF_NOT_ALLOWED(-5),
  ALLOW_TRUST_LOW_RESERVE(-6);

  private final int value;

  AllowTrustResultCode(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static AllowTrustResultCode decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return ALLOW_TRUST_SUCCESS;
      case -1:
        return ALLOW_TRUST_MALFORMED;
      case -2:
        return ALLOW_TRUST_NO_TRUST_LINE;
      case -3:
        return ALLOW_TRUST_TRUST_NOT_REQUIRED;
      case -4:
        return ALLOW_TRUST_CANT_REVOKE;
      case -5:
        return ALLOW_TRUST_SELF_NOT_ALLOWED;
      case -6:
        return ALLOW_TRUST_LOW_RESERVE;
      default:
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(value);
  }

  public static AllowTrustResultCode fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static AllowTrustResultCode fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
