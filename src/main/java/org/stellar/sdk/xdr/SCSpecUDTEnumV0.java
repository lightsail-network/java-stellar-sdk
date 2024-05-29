// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * SCSpecUDTEnumV0's original definition in the XDR file is:
 *
 * <pre>
 * struct SCSpecUDTEnumV0
 * {
 *     string doc&lt;SC_SPEC_DOC_LIMIT&gt;;
 *     string lib&lt;80&gt;;
 *     string name&lt;60&gt;;
 *     SCSpecUDTEnumCaseV0 cases&lt;50&gt;;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SCSpecUDTEnumV0 implements XdrElement {
  private XdrString doc;
  private XdrString lib;
  private XdrString name;
  private SCSpecUDTEnumCaseV0[] cases;

  public void encode(XdrDataOutputStream stream) throws IOException {
    doc.encode(stream);
    lib.encode(stream);
    name.encode(stream);
    int casesSize = getCases().length;
    stream.writeInt(casesSize);
    for (int i = 0; i < casesSize; i++) {
      cases[i].encode(stream);
    }
  }

  public static SCSpecUDTEnumV0 decode(XdrDataInputStream stream) throws IOException {
    SCSpecUDTEnumV0 decodedSCSpecUDTEnumV0 = new SCSpecUDTEnumV0();
    decodedSCSpecUDTEnumV0.doc = XdrString.decode(stream, Constants.SC_SPEC_DOC_LIMIT);
    decodedSCSpecUDTEnumV0.lib = XdrString.decode(stream, 80);
    decodedSCSpecUDTEnumV0.name = XdrString.decode(stream, 60);
    int casesSize = stream.readInt();
    decodedSCSpecUDTEnumV0.cases = new SCSpecUDTEnumCaseV0[casesSize];
    for (int i = 0; i < casesSize; i++) {
      decodedSCSpecUDTEnumV0.cases[i] = SCSpecUDTEnumCaseV0.decode(stream);
    }
    return decodedSCSpecUDTEnumV0;
  }

  public static SCSpecUDTEnumV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCSpecUDTEnumV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
