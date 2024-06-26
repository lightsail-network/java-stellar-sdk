// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * ManageOfferEffect's original definition in the XDR file is:
 *
 * <pre>
 * enum ManageOfferEffect
 * {
 *     MANAGE_OFFER_CREATED = 0,
 *     MANAGE_OFFER_UPDATED = 1,
 *     MANAGE_OFFER_DELETED = 2
 * };
 * </pre>
 */
public enum ManageOfferEffect implements XdrElement {
  MANAGE_OFFER_CREATED(0),
  MANAGE_OFFER_UPDATED(1),
  MANAGE_OFFER_DELETED(2);

  private final int value;

  ManageOfferEffect(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static ManageOfferEffect decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return MANAGE_OFFER_CREATED;
      case 1:
        return MANAGE_OFFER_UPDATED;
      case 2:
        return MANAGE_OFFER_DELETED;
      default:
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(value);
  }

  public static ManageOfferEffect fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ManageOfferEffect fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
