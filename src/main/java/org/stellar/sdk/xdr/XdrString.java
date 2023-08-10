package org.stellar.sdk.xdr;

import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class XdrString implements XdrElement {
  private byte[] bytes;

  public XdrString(byte[] bytes) {
    this.bytes = bytes;
  }

  public XdrString(String text) {
    this.bytes = text.getBytes(Charset.forName("UTF-8"));
  }

  @Override
  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(this.bytes.length);
    stream.write(this.bytes, 0, this.bytes.length);
  }

  public static XdrString decode(XdrDataInputStream stream, int maxSize) throws IOException {
    int size = stream.readInt();
    if (size > maxSize) {
      throw new InvalidClassException("String length " + size + " exceeds max size " + maxSize);
    }
    byte[] bytes = new byte[size];
    stream.read(bytes);
    return new XdrString(bytes);
  }

  public byte[] getBytes() {
    return this.bytes;
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

  public static XdrString fromXdrBase64(String xdr, int maxSize) throws IOException {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(xdr);
    return fromXdrByteArray(bytes, maxSize);
  }

  public static XdrString fromXdrBase64(String xdr) throws IOException {
    return fromXdrBase64(xdr, Integer.MAX_VALUE);
  }

  public static XdrString fromXdrByteArray(byte[] xdr, int maxSize) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream, maxSize);
  }

  public static XdrString fromXdrByteArray(byte[] xdr) throws IOException {
    return fromXdrByteArray(xdr, Integer.MAX_VALUE);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.bytes);
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || !(object instanceof XdrString)) {
      return false;
    }

    XdrString other = (XdrString) object;
    return Arrays.equals(this.bytes, other.bytes);
  }

  @Override
  public String toString() {
    return new String(bytes, Charset.forName("UTF-8"));
  }
}
