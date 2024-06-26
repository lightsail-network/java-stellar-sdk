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
 * ContractExecutable's original definition in the XDR file is:
 *
 * <pre>
 * union ContractExecutable switch (ContractExecutableType type)
 * {
 * case CONTRACT_EXECUTABLE_WASM:
 *     Hash wasm_hash;
 * case CONTRACT_EXECUTABLE_STELLAR_ASSET:
 *     void;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContractExecutable implements XdrElement {
  private ContractExecutableType discriminant;
  private Hash wasm_hash;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(discriminant.getValue());
    switch (discriminant) {
      case CONTRACT_EXECUTABLE_WASM:
        wasm_hash.encode(stream);
        break;
      case CONTRACT_EXECUTABLE_STELLAR_ASSET:
        break;
    }
  }

  public static ContractExecutable decode(XdrDataInputStream stream) throws IOException {
    ContractExecutable decodedContractExecutable = new ContractExecutable();
    ContractExecutableType discriminant = ContractExecutableType.decode(stream);
    decodedContractExecutable.setDiscriminant(discriminant);
    switch (decodedContractExecutable.getDiscriminant()) {
      case CONTRACT_EXECUTABLE_WASM:
        decodedContractExecutable.wasm_hash = Hash.decode(stream);
        break;
      case CONTRACT_EXECUTABLE_STELLAR_ASSET:
        break;
    }
    return decodedContractExecutable;
  }

  public static ContractExecutable fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ContractExecutable fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
