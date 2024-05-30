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
 * HostFunction's original definition in the XDR file is:
 *
 * <pre>
 * union HostFunction switch (HostFunctionType type)
 * {
 * case HOST_FUNCTION_TYPE_INVOKE_CONTRACT:
 *     InvokeContractArgs invokeContract;
 * case HOST_FUNCTION_TYPE_CREATE_CONTRACT:
 *     CreateContractArgs createContract;
 * case HOST_FUNCTION_TYPE_UPLOAD_CONTRACT_WASM:
 *     opaque wasm&lt;&gt;;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class HostFunction implements XdrElement {
  private HostFunctionType discriminant;
  private InvokeContractArgs invokeContract;
  private CreateContractArgs createContract;
  private byte[] wasm;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(discriminant.getValue());
    switch (discriminant) {
      case HOST_FUNCTION_TYPE_INVOKE_CONTRACT:
        invokeContract.encode(stream);
        break;
      case HOST_FUNCTION_TYPE_CREATE_CONTRACT:
        createContract.encode(stream);
        break;
      case HOST_FUNCTION_TYPE_UPLOAD_CONTRACT_WASM:
        int wasmSize = wasm.length;
        stream.writeInt(wasmSize);
        stream.write(getWasm(), 0, wasmSize);
        break;
    }
  }

  public static HostFunction decode(XdrDataInputStream stream) throws IOException {
    HostFunction decodedHostFunction = new HostFunction();
    HostFunctionType discriminant = HostFunctionType.decode(stream);
    decodedHostFunction.setDiscriminant(discriminant);
    switch (decodedHostFunction.getDiscriminant()) {
      case HOST_FUNCTION_TYPE_INVOKE_CONTRACT:
        decodedHostFunction.invokeContract = InvokeContractArgs.decode(stream);
        break;
      case HOST_FUNCTION_TYPE_CREATE_CONTRACT:
        decodedHostFunction.createContract = CreateContractArgs.decode(stream);
        break;
      case HOST_FUNCTION_TYPE_UPLOAD_CONTRACT_WASM:
        int wasmSize = stream.readInt();
        decodedHostFunction.wasm = new byte[wasmSize];
        stream.read(decodedHostFunction.wasm, 0, wasmSize);
        break;
    }
    return decodedHostFunction;
  }

  public static HostFunction fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static HostFunction fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
