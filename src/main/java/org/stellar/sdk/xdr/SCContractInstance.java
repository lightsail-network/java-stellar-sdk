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
 * SCContractInstance's original definition in the XDR file is:
 *
 * <pre>
 * struct SCContractInstance {
 *     ContractExecutable executable;
 *     SCMap&#42; storage;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SCContractInstance implements XdrElement {
  private ContractExecutable executable;
  private SCMap storage;

  public void encode(XdrDataOutputStream stream) throws IOException {
    executable.encode(stream);
    if (storage != null) {
      stream.writeInt(1);
      storage.encode(stream);
    } else {
      stream.writeInt(0);
    }
  }

  public static SCContractInstance decode(XdrDataInputStream stream) throws IOException {
    SCContractInstance decodedSCContractInstance = new SCContractInstance();
    decodedSCContractInstance.executable = ContractExecutable.decode(stream);
    int storagePresent = stream.readInt();
    if (storagePresent != 0) {
      decodedSCContractInstance.storage = SCMap.decode(stream);
    }
    return decodedSCContractInstance;
  }

  public static SCContractInstance fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCContractInstance fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
