package org.stellar.sdk.xdr;

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
