// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * ContractCostParams's original definition in the XDR file is:
 *
 * <pre>
 * typedef ContractCostParamEntry ContractCostParams&lt;CONTRACT_COST_COUNT_LIMIT&gt;;
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractCostParams implements XdrElement {
  private ContractCostParamEntry[] ContractCostParams;

  public void encode(XdrDataOutputStream stream) throws IOException {
    int ContractCostParamsSize = getContractCostParams().length;
    stream.writeInt(ContractCostParamsSize);
    for (int i = 0; i < ContractCostParamsSize; i++) {
      ContractCostParams[i].encode(stream);
    }
  }

  public static ContractCostParams decode(XdrDataInputStream stream) throws IOException {
    ContractCostParams decodedContractCostParams = new ContractCostParams();
    int ContractCostParamsSize = stream.readInt();
    decodedContractCostParams.ContractCostParams =
        new ContractCostParamEntry[ContractCostParamsSize];
    for (int i = 0; i < ContractCostParamsSize; i++) {
      decodedContractCostParams.ContractCostParams[i] = ContractCostParamEntry.decode(stream);
    }
    return decodedContractCostParams;
  }

  public static ContractCostParams fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ContractCostParams fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
