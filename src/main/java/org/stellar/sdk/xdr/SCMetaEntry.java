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

//  union SCMetaEntry switch (SCMetaKind kind)
//  {
//  case SC_META_V0:
//      SCMetaV0 v0;
//  };

//  ===========================================================================
public class SCMetaEntry implements XdrElement {
  public SCMetaEntry() {}

  SCMetaKind kind;

  public SCMetaKind getDiscriminant() {
    return this.kind;
  }

  public void setDiscriminant(SCMetaKind value) {
    this.kind = value;
  }

  private SCMetaV0 v0;

  public SCMetaV0 getV0() {
    return this.v0;
  }

  public void setV0(SCMetaV0 value) {
    this.v0 = value;
  }

  public static final class Builder {
    private SCMetaKind discriminant;
    private SCMetaV0 v0;

    public Builder discriminant(SCMetaKind discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public Builder v0(SCMetaV0 v0) {
      this.v0 = v0;
      return this;
    }

    public SCMetaEntry build() {
      SCMetaEntry val = new SCMetaEntry();
      val.setDiscriminant(discriminant);
      val.setV0(this.v0);
      return val;
    }
  }

  public static void encode(XdrDataOutputStream stream, SCMetaEntry encodedSCMetaEntry)
      throws IOException {
    // Xdrgen::AST::Identifier
    // SCMetaKind
    stream.writeInt(encodedSCMetaEntry.getDiscriminant().getValue());
    switch (encodedSCMetaEntry.getDiscriminant()) {
      case SC_META_V0:
        SCMetaV0.encode(stream, encodedSCMetaEntry.v0);
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SCMetaEntry decode(XdrDataInputStream stream) throws IOException {
    SCMetaEntry decodedSCMetaEntry = new SCMetaEntry();
    SCMetaKind discriminant = SCMetaKind.decode(stream);
    decodedSCMetaEntry.setDiscriminant(discriminant);
    switch (decodedSCMetaEntry.getDiscriminant()) {
      case SC_META_V0:
        decodedSCMetaEntry.v0 = SCMetaV0.decode(stream);
        break;
    }
    return decodedSCMetaEntry;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.v0, this.kind);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SCMetaEntry)) {
      return false;
    }

    SCMetaEntry other = (SCMetaEntry) object;
    return Objects.equals(this.v0, other.v0) && Objects.equals(this.kind, other.kind);
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

  public static SCMetaEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCMetaEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
