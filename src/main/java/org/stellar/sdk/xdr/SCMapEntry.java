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

//  struct SCMapEntry
//  {
//      SCVal key;
//      SCVal val;
//  };

//  ===========================================================================
public class SCMapEntry implements XdrElement {
  public SCMapEntry() {}

  private SCVal key;

  public SCVal getKey() {
    return this.key;
  }

  public void setKey(SCVal value) {
    this.key = value;
  }

  private SCVal val;

  public SCVal getVal() {
    return this.val;
  }

  public void setVal(SCVal value) {
    this.val = value;
  }

  public static void encode(XdrDataOutputStream stream, SCMapEntry encodedSCMapEntry)
      throws IOException {
    SCVal.encode(stream, encodedSCMapEntry.key);
    SCVal.encode(stream, encodedSCMapEntry.val);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SCMapEntry decode(XdrDataInputStream stream) throws IOException {
    SCMapEntry decodedSCMapEntry = new SCMapEntry();
    decodedSCMapEntry.key = SCVal.decode(stream);
    decodedSCMapEntry.val = SCVal.decode(stream);
    return decodedSCMapEntry;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.key, this.val);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SCMapEntry)) {
      return false;
    }

    SCMapEntry other = (SCMapEntry) object;
    return Objects.equals(this.key, other.key) && Objects.equals(this.val, other.val);
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

  public static SCMapEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCMapEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private SCVal key;
    private SCVal val;

    public Builder key(SCVal key) {
      this.key = key;
      return this;
    }

    public Builder val(SCVal val) {
      this.val = val;
      return this;
    }

    public SCMapEntry build() {
      SCMapEntry val = new SCMapEntry();
      val.setKey(this.key);
      val.setVal(this.val);
      return val;
    }
  }
}
