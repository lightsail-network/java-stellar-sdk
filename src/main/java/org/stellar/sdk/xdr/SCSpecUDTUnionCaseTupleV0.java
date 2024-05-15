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
 * SCSpecUDTUnionCaseTupleV0's original definition in the XDR file is:
 *
 * <pre>
 * struct SCSpecUDTUnionCaseTupleV0
 * {
 *     string doc&lt;SC_SPEC_DOC_LIMIT&gt;;
 *     string name&lt;60&gt;;
 *     SCSpecTypeDef type&lt;12&gt;;
 * };
 * </pre>
 */
public class SCSpecUDTUnionCaseTupleV0 implements XdrElement {
  public SCSpecUDTUnionCaseTupleV0() {}

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

  private SCSpecTypeDef[] type;

  public SCSpecTypeDef[] getType() {
    return this.type;
  }

  public void setType(SCSpecTypeDef[] value) {
    this.type = value;
  }

  public static void encode(
      XdrDataOutputStream stream, SCSpecUDTUnionCaseTupleV0 encodedSCSpecUDTUnionCaseTupleV0)
      throws IOException {
    encodedSCSpecUDTUnionCaseTupleV0.doc.encode(stream);
    encodedSCSpecUDTUnionCaseTupleV0.name.encode(stream);
    int typesize = encodedSCSpecUDTUnionCaseTupleV0.getType().length;
    stream.writeInt(typesize);
    for (int i = 0; i < typesize; i++) {
      SCSpecTypeDef.encode(stream, encodedSCSpecUDTUnionCaseTupleV0.type[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SCSpecUDTUnionCaseTupleV0 decode(XdrDataInputStream stream) throws IOException {
    SCSpecUDTUnionCaseTupleV0 decodedSCSpecUDTUnionCaseTupleV0 = new SCSpecUDTUnionCaseTupleV0();
    decodedSCSpecUDTUnionCaseTupleV0.doc = XdrString.decode(stream, SC_SPEC_DOC_LIMIT);
    decodedSCSpecUDTUnionCaseTupleV0.name = XdrString.decode(stream, 60);
    int typesize = stream.readInt();
    decodedSCSpecUDTUnionCaseTupleV0.type = new SCSpecTypeDef[typesize];
    for (int i = 0; i < typesize; i++) {
      decodedSCSpecUDTUnionCaseTupleV0.type[i] = SCSpecTypeDef.decode(stream);
    }
    return decodedSCSpecUDTUnionCaseTupleV0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.doc, this.name, Arrays.hashCode(this.type));
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SCSpecUDTUnionCaseTupleV0)) {
      return false;
    }

    SCSpecUDTUnionCaseTupleV0 other = (SCSpecUDTUnionCaseTupleV0) object;
    return Objects.equals(this.doc, other.doc)
        && Objects.equals(this.name, other.name)
        && Arrays.equals(this.type, other.type);
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

  public static SCSpecUDTUnionCaseTupleV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCSpecUDTUnionCaseTupleV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private XdrString doc;
    private XdrString name;
    private SCSpecTypeDef[] type;

    public Builder doc(XdrString doc) {
      this.doc = doc;
      return this;
    }

    public Builder name(XdrString name) {
      this.name = name;
      return this;
    }

    public Builder type(SCSpecTypeDef[] type) {
      this.type = type;
      return this;
    }

    public SCSpecUDTUnionCaseTupleV0 build() {
      SCSpecUDTUnionCaseTupleV0 val = new SCSpecUDTUnionCaseTupleV0();
      val.setDoc(this.doc);
      val.setName(this.name);
      val.setType(this.type);
      return val;
    }
  }
}
