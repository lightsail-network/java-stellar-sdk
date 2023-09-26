// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import org.stellar.sdk.Base64Factory;

// === xdr source ============================================================

//  struct LedgerFootprint
//  {
//      LedgerKey readOnly<>;
//      LedgerKey readWrite<>;
//  };

//  ===========================================================================
public class LedgerFootprint implements XdrElement {
  public LedgerFootprint() {}

  private LedgerKey[] readOnly;

  public LedgerKey[] getReadOnly() {
    return this.readOnly;
  }

  public void setReadOnly(LedgerKey[] value) {
    this.readOnly = value;
  }

  private LedgerKey[] readWrite;

  public LedgerKey[] getReadWrite() {
    return this.readWrite;
  }

  public void setReadWrite(LedgerKey[] value) {
    this.readWrite = value;
  }

  public static void encode(XdrDataOutputStream stream, LedgerFootprint encodedLedgerFootprint)
      throws IOException {
    int readOnlysize = encodedLedgerFootprint.getReadOnly().length;
    stream.writeInt(readOnlysize);
    for (int i = 0; i < readOnlysize; i++) {
      LedgerKey.encode(stream, encodedLedgerFootprint.readOnly[i]);
    }
    int readWritesize = encodedLedgerFootprint.getReadWrite().length;
    stream.writeInt(readWritesize);
    for (int i = 0; i < readWritesize; i++) {
      LedgerKey.encode(stream, encodedLedgerFootprint.readWrite[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static LedgerFootprint decode(XdrDataInputStream stream) throws IOException {
    LedgerFootprint decodedLedgerFootprint = new LedgerFootprint();
    int readOnlysize = stream.readInt();
    decodedLedgerFootprint.readOnly = new LedgerKey[readOnlysize];
    for (int i = 0; i < readOnlysize; i++) {
      decodedLedgerFootprint.readOnly[i] = LedgerKey.decode(stream);
    }
    int readWritesize = stream.readInt();
    decodedLedgerFootprint.readWrite = new LedgerKey[readWritesize];
    for (int i = 0; i < readWritesize; i++) {
      decodedLedgerFootprint.readWrite[i] = LedgerKey.decode(stream);
    }
    return decodedLedgerFootprint;
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.hashCode(this.readOnly), Arrays.hashCode(this.readWrite));
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof LedgerFootprint)) {
      return false;
    }

    LedgerFootprint other = (LedgerFootprint) object;
    return Arrays.equals(this.readOnly, other.readOnly)
        && Arrays.equals(this.readWrite, other.readWrite);
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

  public static LedgerFootprint fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static LedgerFootprint fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private LedgerKey[] readOnly;
    private LedgerKey[] readWrite;

    public Builder readOnly(LedgerKey[] readOnly) {
      this.readOnly = readOnly;
      return this;
    }

    public Builder readWrite(LedgerKey[] readWrite) {
      this.readWrite = readWrite;
      return this;
    }

    public LedgerFootprint build() {
      LedgerFootprint val = new LedgerFootprint();
      val.setReadOnly(this.readOnly);
      val.setReadWrite(this.readWrite);
      return val;
    }
  }
}
