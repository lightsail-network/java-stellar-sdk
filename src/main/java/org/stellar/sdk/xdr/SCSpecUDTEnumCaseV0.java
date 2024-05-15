// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.stellar.sdk.Base64Factory;

/**
 * SCSpecUDTEnumCaseV0's original definition in the XDR file is:
 *
 * <pre>
 * struct SCSpecUDTEnumCaseV0
 * {
 *     string doc&lt;SC_SPEC_DOC_LIMIT&gt;;
 *     string name&lt;60&gt;;
 *     uint32 value;
 * };
 * </pre>
 */
public class SCSpecUDTEnumCaseV0 implements XdrElement {
  public SCSpecUDTEnumCaseV0() {}

  private XdrString doc;

  public XdrString getDoc() {
    return this.doc;
  }

  public void setDoc(XdrString value) {
    this.doc = value;
  }

  private XdrString name;

  public XdrString getName() {
    return this.name;
  }

  public void setName(XdrString value) {
    this.name = value;
  }

  private Uint32 value;

  public Uint32 getValue() {
    return this.value;
  }

  public void setValue(Uint32 value) {
    this.value = value;
  }

  public static void encode(
      XdrDataOutputStream stream, SCSpecUDTEnumCaseV0 encodedSCSpecUDTEnumCaseV0)
      throws IOException {
    encodedSCSpecUDTEnumCaseV0.doc.encode(stream);
    encodedSCSpecUDTEnumCaseV0.name.encode(stream);
    Uint32.encode(stream, encodedSCSpecUDTEnumCaseV0.value);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SCSpecUDTEnumCaseV0 decode(XdrDataInputStream stream) throws IOException {
    SCSpecUDTEnumCaseV0 decodedSCSpecUDTEnumCaseV0 = new SCSpecUDTEnumCaseV0();
    decodedSCSpecUDTEnumCaseV0.doc = XdrString.decode(stream, SC_SPEC_DOC_LIMIT);
    decodedSCSpecUDTEnumCaseV0.name = XdrString.decode(stream, 60);
    decodedSCSpecUDTEnumCaseV0.value = Uint32.decode(stream);
    return decodedSCSpecUDTEnumCaseV0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.doc, this.name, this.value);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SCSpecUDTEnumCaseV0)) {
      return false;
    }

    SCSpecUDTEnumCaseV0 other = (SCSpecUDTEnumCaseV0) object;
    return Objects.equals(this.doc, other.doc)
        && Objects.equals(this.name, other.name)
        && Objects.equals(this.value, other.value);
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

  public static SCSpecUDTEnumCaseV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCSpecUDTEnumCaseV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private XdrString doc;
    private XdrString name;
    private Uint32 value;

    public Builder doc(XdrString doc) {
      this.doc = doc;
      return this;
    }

    public Builder name(XdrString name) {
      this.name = name;
      return this;
    }

    public Builder value(Uint32 value) {
      this.value = value;
      return this;
    }

    public SCSpecUDTEnumCaseV0 build() {
      SCSpecUDTEnumCaseV0 val = new SCSpecUDTEnumCaseV0();
      val.setDoc(this.doc);
      val.setName(this.name);
      val.setValue(this.value);
      return val;
    }
  }
}
