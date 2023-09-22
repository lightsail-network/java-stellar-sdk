// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  struct SCSpecUDTStructV0
//  {
//      string doc<SC_SPEC_DOC_LIMIT>;
//      string lib<80>;
//      string name<60>;
//      SCSpecUDTStructFieldV0 fields<40>;
//  };

//  ===========================================================================
public class SCSpecUDTStructV0 implements XdrElement {
  public SCSpecUDTStructV0() {}

  private XdrString doc;

  public XdrString getDoc() {
    return this.doc;
  }

  public void setDoc(XdrString value) {
    this.doc = value;
  }

  private XdrString lib;

  public XdrString getLib() {
    return this.lib;
  }

  public void setLib(XdrString value) {
    this.lib = value;
  }

  private XdrString name;

  public XdrString getName() {
    return this.name;
  }

  public void setName(XdrString value) {
    this.name = value;
  }

  private SCSpecUDTStructFieldV0[] fields;

  public SCSpecUDTStructFieldV0[] getFields() {
    return this.fields;
  }

  public void setFields(SCSpecUDTStructFieldV0[] value) {
    this.fields = value;
  }

  public static void encode(XdrDataOutputStream stream, SCSpecUDTStructV0 encodedSCSpecUDTStructV0)
      throws IOException {
    encodedSCSpecUDTStructV0.doc.encode(stream);
    encodedSCSpecUDTStructV0.lib.encode(stream);
    encodedSCSpecUDTStructV0.name.encode(stream);
    int fieldssize = encodedSCSpecUDTStructV0.getFields().length;
    stream.writeInt(fieldssize);
    for (int i = 0; i < fieldssize; i++) {
      SCSpecUDTStructFieldV0.encode(stream, encodedSCSpecUDTStructV0.fields[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SCSpecUDTStructV0 decode(XdrDataInputStream stream) throws IOException {
    SCSpecUDTStructV0 decodedSCSpecUDTStructV0 = new SCSpecUDTStructV0();
    decodedSCSpecUDTStructV0.doc = XdrString.decode(stream, SC_SPEC_DOC_LIMIT);
    decodedSCSpecUDTStructV0.lib = XdrString.decode(stream, 80);
    decodedSCSpecUDTStructV0.name = XdrString.decode(stream, 60);
    int fieldssize = stream.readInt();
    decodedSCSpecUDTStructV0.fields = new SCSpecUDTStructFieldV0[fieldssize];
    for (int i = 0; i < fieldssize; i++) {
      decodedSCSpecUDTStructV0.fields[i] = SCSpecUDTStructFieldV0.decode(stream);
    }
    return decodedSCSpecUDTStructV0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.doc, this.lib, this.name, Arrays.hashCode(this.fields));
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SCSpecUDTStructV0)) {
      return false;
    }

    SCSpecUDTStructV0 other = (SCSpecUDTStructV0) object;
    return Objects.equals(this.doc, other.doc)
        && Objects.equals(this.lib, other.lib)
        && Objects.equals(this.name, other.name)
        && Arrays.equals(this.fields, other.fields);
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

  public static SCSpecUDTStructV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCSpecUDTStructV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private XdrString doc;
    private XdrString lib;
    private XdrString name;
    private SCSpecUDTStructFieldV0[] fields;

    public Builder doc(XdrString doc) {
      this.doc = doc;
      return this;
    }

    public Builder lib(XdrString lib) {
      this.lib = lib;
      return this;
    }

    public Builder name(XdrString name) {
      this.name = name;
      return this;
    }

    public Builder fields(SCSpecUDTStructFieldV0[] fields) {
      this.fields = fields;
      return this;
    }

    public SCSpecUDTStructV0 build() {
      SCSpecUDTStructV0 val = new SCSpecUDTStructV0();
      val.setDoc(this.doc);
      val.setLib(this.lib);
      val.setName(this.name);
      val.setFields(this.fields);
      return val;
    }
  }
}
