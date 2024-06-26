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
 * ContractCodeCostInputs's original definition in the XDR file is:
 *
 * <pre>
 * struct ContractCodeCostInputs {
 *     ExtensionPoint ext;
 *     uint32 nInstructions;
 *     uint32 nFunctions;
 *     uint32 nGlobals;
 *     uint32 nTableEntries;
 *     uint32 nTypes;
 *     uint32 nDataSegments;
 *     uint32 nElemSegments;
 *     uint32 nImports;
 *     uint32 nExports;
 *     uint32 nDataSegmentBytes;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContractCodeCostInputs implements XdrElement {
  private ExtensionPoint ext;
  private Uint32 nInstructions;
  private Uint32 nFunctions;
  private Uint32 nGlobals;
  private Uint32 nTableEntries;
  private Uint32 nTypes;
  private Uint32 nDataSegments;
  private Uint32 nElemSegments;
  private Uint32 nImports;
  private Uint32 nExports;
  private Uint32 nDataSegmentBytes;

  public void encode(XdrDataOutputStream stream) throws IOException {
    ext.encode(stream);
    nInstructions.encode(stream);
    nFunctions.encode(stream);
    nGlobals.encode(stream);
    nTableEntries.encode(stream);
    nTypes.encode(stream);
    nDataSegments.encode(stream);
    nElemSegments.encode(stream);
    nImports.encode(stream);
    nExports.encode(stream);
    nDataSegmentBytes.encode(stream);
  }

  public static ContractCodeCostInputs decode(XdrDataInputStream stream) throws IOException {
    ContractCodeCostInputs decodedContractCodeCostInputs = new ContractCodeCostInputs();
    decodedContractCodeCostInputs.ext = ExtensionPoint.decode(stream);
    decodedContractCodeCostInputs.nInstructions = Uint32.decode(stream);
    decodedContractCodeCostInputs.nFunctions = Uint32.decode(stream);
    decodedContractCodeCostInputs.nGlobals = Uint32.decode(stream);
    decodedContractCodeCostInputs.nTableEntries = Uint32.decode(stream);
    decodedContractCodeCostInputs.nTypes = Uint32.decode(stream);
    decodedContractCodeCostInputs.nDataSegments = Uint32.decode(stream);
    decodedContractCodeCostInputs.nElemSegments = Uint32.decode(stream);
    decodedContractCodeCostInputs.nImports = Uint32.decode(stream);
    decodedContractCodeCostInputs.nExports = Uint32.decode(stream);
    decodedContractCodeCostInputs.nDataSegmentBytes = Uint32.decode(stream);
    return decodedContractCodeCostInputs;
  }

  public static ContractCodeCostInputs fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ContractCodeCostInputs fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
