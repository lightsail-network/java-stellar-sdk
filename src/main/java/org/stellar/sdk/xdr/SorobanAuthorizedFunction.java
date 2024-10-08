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
 * SorobanAuthorizedFunction's original definition in the XDR file is:
 *
 * <pre>
 * union SorobanAuthorizedFunction switch (SorobanAuthorizedFunctionType type)
 * {
 * case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN:
 *     InvokeContractArgs contractFn;
 * // This variant of auth payload for creating new contract instances
 * // doesn't allow specifying the constructor arguments, creating contracts
 * // with constructors that take arguments is only possible by authorizing
 * // `SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_V2_HOST_FN`
 * // (protocol 22+).
 * case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_HOST_FN:
 *     CreateContractArgs createContractHostFn;
 * // This variant of auth payload for creating new contract instances
 * // is only accepted in and after protocol 22. It allows authorizing the
 * // contract constructor arguments.
 * case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_V2_HOST_FN:
 *     CreateContractArgsV2 createContractV2HostFn;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SorobanAuthorizedFunction implements XdrElement {
  private SorobanAuthorizedFunctionType discriminant;
  private InvokeContractArgs contractFn;
  private CreateContractArgs createContractHostFn;
  private CreateContractArgsV2 createContractV2HostFn;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(discriminant.getValue());
    switch (discriminant) {
      case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN:
        contractFn.encode(stream);
        break;
      case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_HOST_FN:
        createContractHostFn.encode(stream);
        break;
      case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_V2_HOST_FN:
        createContractV2HostFn.encode(stream);
        break;
    }
  }

  public static SorobanAuthorizedFunction decode(XdrDataInputStream stream) throws IOException {
    SorobanAuthorizedFunction decodedSorobanAuthorizedFunction = new SorobanAuthorizedFunction();
    SorobanAuthorizedFunctionType discriminant = SorobanAuthorizedFunctionType.decode(stream);
    decodedSorobanAuthorizedFunction.setDiscriminant(discriminant);
    switch (decodedSorobanAuthorizedFunction.getDiscriminant()) {
      case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN:
        decodedSorobanAuthorizedFunction.contractFn = InvokeContractArgs.decode(stream);
        break;
      case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_HOST_FN:
        decodedSorobanAuthorizedFunction.createContractHostFn = CreateContractArgs.decode(stream);
        break;
      case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_V2_HOST_FN:
        decodedSorobanAuthorizedFunction.createContractV2HostFn =
            CreateContractArgsV2.decode(stream);
        break;
    }
    return decodedSorobanAuthorizedFunction;
  }

  public static SorobanAuthorizedFunction fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SorobanAuthorizedFunction fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
