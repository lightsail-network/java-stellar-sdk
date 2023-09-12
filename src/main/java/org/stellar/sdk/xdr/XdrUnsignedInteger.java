package org.stellar.sdk.xdr;

import com.google.common.base.Objects;
import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Represents XDR Unsigned Integer.
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4506#section-4.2">XDR: External Data
 *     Representation Standard</a>
 */
public class XdrUnsignedInteger implements XdrElement {
  public static final long MAX_VALUE = (1L << 32) - 1;
  public static final long MIN_VALUE = 0;
  private final Long number;

  public XdrUnsignedInteger(Long number) {
    if (number < MIN_VALUE || number > MAX_VALUE) {
      throw new IllegalArgumentException("number must be between 0 and 2^32 - 1 inclusive");
    }
    this.number = number;
  }

  public XdrUnsignedInteger(Integer number) {
    if (number < 0) {
      throw new IllegalArgumentException(
          "number must be greater than or equal to 0 if you want to construct it from Integer");
    }
    this.number = number.longValue();
  }

  public Long getNumber() {
    return number;
  }

  public static XdrUnsignedInteger decode(XdrDataInputStream stream) throws IOException {
    int intValue = stream.readInt();
    long uint32Value = Integer.toUnsignedLong(intValue);
    return new XdrUnsignedInteger(uint32Value);
  }

  @Override
  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(number.intValue());
  }

  @Override
  public String toXdrBase64() throws IOException {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    return base64Encoding.encode(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static XdrUnsignedInteger fromXdrBase64(String xdr) throws IOException {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static XdrUnsignedInteger fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.number);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof XdrUnsignedInteger)) {
      return false;
    }

    XdrUnsignedInteger other = (XdrUnsignedInteger) object;
    return Objects.equal(this.number, other.number);
  }

  public String toString() {
    return "XdrUnsignedInteger(number=" + this.getNumber() + ")";
  }
}
