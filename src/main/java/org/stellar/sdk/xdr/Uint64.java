// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import com.google.common.base.Objects;
import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// === xdr source ============================================================

//  typedef unsigned hyper uint64;

//  ===========================================================================
public class Uint64 implements XdrElement {
  private XdrUnsignedHyperInteger uint64;

  public Uint64() {}

  public Uint64(XdrUnsignedHyperInteger uint64) {
    this.uint64 = uint64;
  }

  public XdrUnsignedHyperInteger getUint64() {
    return this.uint64;
  }

  public void setUint64(XdrUnsignedHyperInteger value) {
    this.uint64 = value;
  }

  public static void encode(XdrDataOutputStream stream, Uint64 encodedUint64) throws IOException {
    encodedUint64.uint64.encode(stream);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static Uint64 decode(XdrDataInputStream stream) throws IOException {
    Uint64 decodedUint64 = new Uint64();
    decodedUint64.uint64 = XdrUnsignedHyperInteger.decode(stream);
    return decodedUint64;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.uint64);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Uint64)) {
      return false;
    }

    Uint64 other = (Uint64) object;
    return Objects.equal(this.uint64, other.uint64);
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

  public static Uint64 fromXdrBase64(String xdr) throws IOException {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static Uint64 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
