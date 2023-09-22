// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  struct SCSpecTypeBytesN
//  {
//      uint32 n;
//  };

//  ===========================================================================
public class SCSpecTypeBytesN implements XdrElement {
  public SCSpecTypeBytesN() {}

  private Uint32 n;

  public Uint32 getN() {
    return this.n;
  }

  public void setN(Uint32 value) {
    this.n = value;
  }

  public static void encode(XdrDataOutputStream stream, SCSpecTypeBytesN encodedSCSpecTypeBytesN)
      throws IOException {
    Uint32.encode(stream, encodedSCSpecTypeBytesN.n);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SCSpecTypeBytesN decode(XdrDataInputStream stream) throws IOException {
    SCSpecTypeBytesN decodedSCSpecTypeBytesN = new SCSpecTypeBytesN();
    decodedSCSpecTypeBytesN.n = Uint32.decode(stream);
    return decodedSCSpecTypeBytesN;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.n);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SCSpecTypeBytesN)) {
      return false;
    }

    SCSpecTypeBytesN other = (SCSpecTypeBytesN) object;
    return Objects.equals(this.n, other.n);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static SCSpecTypeBytesN fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCSpecTypeBytesN fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private Uint32 n;

    public Builder n(Uint32 n) {
      this.n = n;
      return this;
    }

    public SCSpecTypeBytesN build() {
      SCSpecTypeBytesN val = new SCSpecTypeBytesN();
      val.setN(this.n);
      return val;
    }
  }
}
