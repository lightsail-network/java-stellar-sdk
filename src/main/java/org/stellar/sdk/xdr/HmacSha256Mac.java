// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import org.stellar.sdk.Base64Factory;

/**
 * HmacSha256Mac's original definition in the XDR file is:
 *
 * <pre>
 * struct HmacSha256Mac
 * {
 *     opaque mac[32];
 * };
 * </pre>
 */
public class HmacSha256Mac implements XdrElement {
  public HmacSha256Mac() {}

  private byte[] mac;

  public byte[] getMac() {
    return this.mac;
  }

  public void setMac(byte[] value) {
    this.mac = value;
  }

  public static void encode(XdrDataOutputStream stream, HmacSha256Mac encodedHmacSha256Mac)
      throws IOException {
    int macsize = encodedHmacSha256Mac.mac.length;
    stream.write(encodedHmacSha256Mac.getMac(), 0, macsize);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static HmacSha256Mac decode(XdrDataInputStream stream) throws IOException {
    HmacSha256Mac decodedHmacSha256Mac = new HmacSha256Mac();
    int macsize = 32;
    decodedHmacSha256Mac.mac = new byte[macsize];
    stream.read(decodedHmacSha256Mac.mac, 0, macsize);
    return decodedHmacSha256Mac;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.mac);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof HmacSha256Mac)) {
      return false;
    }

    HmacSha256Mac other = (HmacSha256Mac) object;
    return Arrays.equals(this.mac, other.mac);
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

  public static HmacSha256Mac fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static HmacSha256Mac fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private byte[] mac;

    public Builder mac(byte[] mac) {
      this.mac = mac;
      return this;
    }

    public HmacSha256Mac build() {
      HmacSha256Mac val = new HmacSha256Mac();
      val.setMac(this.mac);
      return val;
    }
  }
}
