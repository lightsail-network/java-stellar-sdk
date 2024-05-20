// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * SCSpecUDTErrorEnumV0's original definition in the XDR file is:
 *
 * <pre>
 * struct SCSpecUDTErrorEnumV0
 * {
 *     string doc&lt;SC_SPEC_DOC_LIMIT&gt;;
 *     string lib&lt;80&gt;;
 *     string name&lt;60&gt;;
 *     SCSpecUDTErrorEnumCaseV0 cases&lt;50&gt;;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SCSpecUDTErrorEnumV0 implements XdrElement {
  private XdrString doc;
  private XdrString lib;
  private XdrString name;
  private SCSpecUDTErrorEnumCaseV0[] cases;

  public static void encode(
      XdrDataOutputStream stream, SCSpecUDTErrorEnumV0 encodedSCSpecUDTErrorEnumV0)
      throws IOException {
    encodedSCSpecUDTErrorEnumV0.doc.encode(stream);
    encodedSCSpecUDTErrorEnumV0.lib.encode(stream);
    encodedSCSpecUDTErrorEnumV0.name.encode(stream);
    int casessize = encodedSCSpecUDTErrorEnumV0.getCases().length;
    stream.writeInt(casessize);
    for (int i = 0; i < casessize; i++) {
      SCSpecUDTErrorEnumCaseV0.encode(stream, encodedSCSpecUDTErrorEnumV0.cases[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SCSpecUDTErrorEnumV0 decode(XdrDataInputStream stream) throws IOException {
    SCSpecUDTErrorEnumV0 decodedSCSpecUDTErrorEnumV0 = new SCSpecUDTErrorEnumV0();
    decodedSCSpecUDTErrorEnumV0.doc = XdrString.decode(stream, SC_SPEC_DOC_LIMIT);
    decodedSCSpecUDTErrorEnumV0.lib = XdrString.decode(stream, 80);
    decodedSCSpecUDTErrorEnumV0.name = XdrString.decode(stream, 60);
    int casessize = stream.readInt();
    decodedSCSpecUDTErrorEnumV0.cases = new SCSpecUDTErrorEnumCaseV0[casessize];
    for (int i = 0; i < casessize; i++) {
      decodedSCSpecUDTErrorEnumV0.cases[i] = SCSpecUDTErrorEnumCaseV0.decode(stream);
    }
    return decodedSCSpecUDTErrorEnumV0;
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

  public static SCSpecUDTErrorEnumV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCSpecUDTErrorEnumV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
