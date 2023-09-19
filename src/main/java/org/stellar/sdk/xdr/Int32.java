// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

// === xdr source ============================================================

//  typedef int int32;

//  ===========================================================================
public class Int32 implements XdrElement {
  private Integer int32;

  public Int32() {}

  public Int32(Integer int32) {
    this.int32 = int32;
  }

  public Integer getInt32() {
    return this.int32;
  }

  public void setInt32(Integer value) {
    this.int32 = value;
  }

  public static void encode(XdrDataOutputStream stream, Int32 encodedInt32) throws IOException {
    stream.writeInt(encodedInt32.int32);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static Int32 decode(XdrDataInputStream stream) throws IOException {
    Int32 decodedInt32 = new Int32();
    decodedInt32.int32 = stream.readInt();
    return decodedInt32;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.int32);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Int32)) {
      return false;
    }

    Int32 other = (Int32) object;
    return Objects.equals(this.int32, other.int32);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.getEncoder().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static Int32 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static Int32 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
