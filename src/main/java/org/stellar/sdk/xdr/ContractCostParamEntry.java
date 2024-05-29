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
 * ContractCostParamEntry's original definition in the XDR file is:
 *
 * <pre>
 * struct ContractCostParamEntry {
 *     // use `ext` to add more terms (e.g. higher order polynomials) in the future
 *     ExtensionPoint ext;
 *
 *     int64 constTerm;
 *     int64 linearTerm;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContractCostParamEntry implements XdrElement {
  private ExtensionPoint ext;
  private Int64 constTerm;
  private Int64 linearTerm;

  public void encode(XdrDataOutputStream stream) throws IOException {
    ext.encode(stream);
    constTerm.encode(stream);
    linearTerm.encode(stream);
  }

  public static ContractCostParamEntry decode(XdrDataInputStream stream) throws IOException {
    ContractCostParamEntry decodedContractCostParamEntry = new ContractCostParamEntry();
    decodedContractCostParamEntry.ext = ExtensionPoint.decode(stream);
    decodedContractCostParamEntry.constTerm = Int64.decode(stream);
    decodedContractCostParamEntry.linearTerm = Int64.decode(stream);
    return decodedContractCostParamEntry;
  }

  public static ContractCostParamEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ContractCostParamEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
