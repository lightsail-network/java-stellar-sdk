// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

// === xdr source ============================================================

//  struct SCSpecUDTEnumV0
//  {
//      string doc<SC_SPEC_DOC_LIMIT>;
//      string lib<80>;
//      string name<60>;
//      SCSpecUDTEnumCaseV0 cases<50>;
//  };

//  ===========================================================================
public class SCSpecUDTEnumV0 implements XdrElement {
  public SCSpecUDTEnumV0() {}

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

  private SCSpecUDTEnumCaseV0[] cases;

  public SCSpecUDTEnumCaseV0[] getCases() {
    return this.cases;
  }

  public void setCases(SCSpecUDTEnumCaseV0[] value) {
    this.cases = value;
  }

  public static void encode(XdrDataOutputStream stream, SCSpecUDTEnumV0 encodedSCSpecUDTEnumV0)
      throws IOException {
    encodedSCSpecUDTEnumV0.doc.encode(stream);
    encodedSCSpecUDTEnumV0.lib.encode(stream);
    encodedSCSpecUDTEnumV0.name.encode(stream);
    int casessize = encodedSCSpecUDTEnumV0.getCases().length;
    stream.writeInt(casessize);
    for (int i = 0; i < casessize; i++) {
      SCSpecUDTEnumCaseV0.encode(stream, encodedSCSpecUDTEnumV0.cases[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SCSpecUDTEnumV0 decode(XdrDataInputStream stream) throws IOException {
    SCSpecUDTEnumV0 decodedSCSpecUDTEnumV0 = new SCSpecUDTEnumV0();
    decodedSCSpecUDTEnumV0.doc = XdrString.decode(stream, SC_SPEC_DOC_LIMIT);
    decodedSCSpecUDTEnumV0.lib = XdrString.decode(stream, 80);
    decodedSCSpecUDTEnumV0.name = XdrString.decode(stream, 60);
    int casessize = stream.readInt();
    decodedSCSpecUDTEnumV0.cases = new SCSpecUDTEnumCaseV0[casessize];
    for (int i = 0; i < casessize; i++) {
      decodedSCSpecUDTEnumV0.cases[i] = SCSpecUDTEnumCaseV0.decode(stream);
    }
    return decodedSCSpecUDTEnumV0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.doc, this.lib, this.name, Arrays.hashCode(this.cases));
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SCSpecUDTEnumV0)) {
      return false;
    }

    SCSpecUDTEnumV0 other = (SCSpecUDTEnumV0) object;
    return Objects.equals(this.doc, other.doc)
        && Objects.equals(this.lib, other.lib)
        && Objects.equals(this.name, other.name)
        && Arrays.equals(this.cases, other.cases);
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

  public static SCSpecUDTEnumV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCSpecUDTEnumV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private XdrString doc;
    private XdrString lib;
    private XdrString name;
    private SCSpecUDTEnumCaseV0[] cases;

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

    public Builder cases(SCSpecUDTEnumCaseV0[] cases) {
      this.cases = cases;
      return this;
    }

    public SCSpecUDTEnumV0 build() {
      SCSpecUDTEnumV0 val = new SCSpecUDTEnumV0();
      val.setDoc(this.doc);
      val.setLib(this.lib);
      val.setName(this.name);
      val.setCases(this.cases);
      return val;
    }
  }
}
