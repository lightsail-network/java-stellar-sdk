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

/**
 * SCSpecUDTUnionV0's original definition in the XDR file is:
 *
 * <pre>
 * struct SCSpecUDTUnionV0
 * {
 *     string doc&lt;SC_SPEC_DOC_LIMIT&gt;;
 *     string lib&lt;80&gt;;
 *     string name&lt;60&gt;;
 *     SCSpecUDTUnionCaseV0 cases&lt;50&gt;;
 * };
 * </pre>
 */
public class SCSpecUDTUnionV0 implements XdrElement {
  public SCSpecUDTUnionV0() {}

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

  private SCSpecUDTUnionCaseV0[] cases;

  public SCSpecUDTUnionCaseV0[] getCases() {
    return this.cases;
  }

  public void setCases(SCSpecUDTUnionCaseV0[] value) {
    this.cases = value;
  }

  public static void encode(XdrDataOutputStream stream, SCSpecUDTUnionV0 encodedSCSpecUDTUnionV0)
      throws IOException {
    encodedSCSpecUDTUnionV0.doc.encode(stream);
    encodedSCSpecUDTUnionV0.lib.encode(stream);
    encodedSCSpecUDTUnionV0.name.encode(stream);
    int casessize = encodedSCSpecUDTUnionV0.getCases().length;
    stream.writeInt(casessize);
    for (int i = 0; i < casessize; i++) {
      SCSpecUDTUnionCaseV0.encode(stream, encodedSCSpecUDTUnionV0.cases[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SCSpecUDTUnionV0 decode(XdrDataInputStream stream) throws IOException {
    SCSpecUDTUnionV0 decodedSCSpecUDTUnionV0 = new SCSpecUDTUnionV0();
    decodedSCSpecUDTUnionV0.doc = XdrString.decode(stream, SC_SPEC_DOC_LIMIT);
    decodedSCSpecUDTUnionV0.lib = XdrString.decode(stream, 80);
    decodedSCSpecUDTUnionV0.name = XdrString.decode(stream, 60);
    int casessize = stream.readInt();
    decodedSCSpecUDTUnionV0.cases = new SCSpecUDTUnionCaseV0[casessize];
    for (int i = 0; i < casessize; i++) {
      decodedSCSpecUDTUnionV0.cases[i] = SCSpecUDTUnionCaseV0.decode(stream);
    }
    return decodedSCSpecUDTUnionV0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.doc, this.lib, this.name, Arrays.hashCode(this.cases));
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SCSpecUDTUnionV0)) {
      return false;
    }

    SCSpecUDTUnionV0 other = (SCSpecUDTUnionV0) object;
    return Objects.equals(this.doc, other.doc)
        && Objects.equals(this.lib, other.lib)
        && Objects.equals(this.name, other.name)
        && Arrays.equals(this.cases, other.cases);
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

  public static SCSpecUDTUnionV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCSpecUDTUnionV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private XdrString doc;
    private XdrString lib;
    private XdrString name;
    private SCSpecUDTUnionCaseV0[] cases;

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

    public Builder cases(SCSpecUDTUnionCaseV0[] cases) {
      this.cases = cases;
      return this;
    }

    public SCSpecUDTUnionV0 build() {
      SCSpecUDTUnionV0 val = new SCSpecUDTUnionV0();
      val.setDoc(this.doc);
      val.setLib(this.lib);
      val.setName(this.name);
      val.setCases(this.cases);
      return val;
    }
  }
}
