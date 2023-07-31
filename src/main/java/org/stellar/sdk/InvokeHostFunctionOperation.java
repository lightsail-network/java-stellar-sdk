package org.stellar.sdk;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.xdr.ContractExecutable;
import org.stellar.sdk.xdr.ContractExecutableType;
import org.stellar.sdk.xdr.ContractIDPreimage;
import org.stellar.sdk.xdr.ContractIDPreimageType;
import org.stellar.sdk.xdr.CreateContractArgs;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.HostFunction;
import org.stellar.sdk.xdr.HostFunctionType;
import org.stellar.sdk.xdr.InvokeHostFunctionOp;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;
import org.stellar.sdk.xdr.Uint256;

/**
 * Represents <a
 * href="https://github.com/stellar/go/blob/7ff6ffae29d278f979fcd6c6bed8cd0d4b4d2e08/txnbuild/invoke_host_function.go#L8-L13"
 * target="_blank">InvokeHostFunction</a> operation.
 *
 * @see <a href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations"
 *     target="_blank">List of Operations</a>
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Value
public class InvokeHostFunctionOperation extends Operation {

  /** The host function to invoke. */
  @NonNull HostFunction hostFunction;

  /** The authorizations required to execute the host function */
  @Singular("auth")
  @NonNull
  Collection<SorobanAuthorizationEntry> auth;

  /**
   * Constructs a new InvokeHostFunctionOperation object from the XDR representation of the {@link
   * InvokeHostFunctionOperation}.
   *
   * @param op the XDR representation of the {@link InvokeHostFunctionOperation}.
   */
  public static InvokeHostFunctionOperation fromXdr(InvokeHostFunctionOp op) {
    return InvokeHostFunctionOperation.builder()
        .hostFunction(op.getHostFunction())
        .auth(Arrays.asList(op.getAuth()))
        .build();
  }

  public static InvokeHostFunctionOperationBuilder<?, ?> uploadContractWasmOperationBuilder(
      byte[] wasm) {
    HostFunction hostFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_UPLOAD_CONTRACT_WASM)
            .wasm(wasm)
            .build();
    return builder().hostFunction(hostFunction);
  }

  public static InvokeHostFunctionOperationBuilder<?, ?> createContractOperationBuilder(
      String wasmId, Address address, @Nullable byte[] salt) {
    byte[] wasmIdBytes = Util.hexToBytes(wasmId);
    return createContractOperationBuilder(wasmIdBytes, address, salt);
  }

  public static InvokeHostFunctionOperationBuilder<?, ?> createContractOperationBuilder(
      byte[] wasmId, Address address, @Nullable byte[] salt) {
    if (salt == null) {
      salt = new byte[32];
      new SecureRandom().nextBytes(salt);
    } else if (salt.length != 32) {
      throw new IllegalArgumentException("\"salt\" must be 32 bytes long");
    }

    if (wasmId.length != 32) {
      throw new IllegalArgumentException("\"wasmId\" must be 32 bytes long");
    }

    CreateContractArgs createContractArgs =
        new CreateContractArgs.Builder()
            .contractIDPreimage(
                new ContractIDPreimage.Builder()
                    .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ADDRESS)
                    .fromAddress(
                        new ContractIDPreimage.ContractIDPreimageFromAddress.Builder()
                            .address(address.toSCAddress())
                            .salt(new Uint256(salt))
                            .build())
                    .build())
            .executable(
                new ContractExecutable.Builder()
                    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_WASM)
                    .wasm_hash(new Hash(wasmId))
                    .build())
            .build();
    HostFunction hostFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    return builder().hostFunction(hostFunction);
  }

  public static InvokeHostFunctionOperationBuilder<?, ?> deployCreateTokenContractOperationBuilder(
      Asset asset) {
    CreateContractArgs createContractArgs =
        new CreateContractArgs.Builder()
            .contractIDPreimage(
                new ContractIDPreimage.Builder()
                    .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ASSET)
                    .fromAsset(asset.toXdr())
                    .build())
            .executable(
                new ContractExecutable.Builder()
                    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_TOKEN)
                    .build())
            .build();
    HostFunction hostFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    return builder().hostFunction(hostFunction);
  }

  public static InvokeHostFunctionOperationBuilder<?, ?> deployCreateTokenContractOperationBuilder(
      Address address, @Nullable byte[] salt) {
    if (salt == null) {
      salt = new byte[32];
      new SecureRandom().nextBytes(salt);
    } else if (salt.length != 32) {
      throw new IllegalArgumentException("\"salt\" must be 32 bytes long");
    }

    CreateContractArgs createContractArgs =
        new CreateContractArgs.Builder()
            .contractIDPreimage(
                new ContractIDPreimage.Builder()
                    .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ADDRESS)
                    .fromAddress(
                        new ContractIDPreimage.ContractIDPreimageFromAddress.Builder()
                            .address(address.toSCAddress())
                            .salt(new Uint256(salt))
                            .build())
                    .build())
            .executable(
                new ContractExecutable.Builder()
                    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_TOKEN)
                    .build())
            .build();
    HostFunction hostFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    return builder().hostFunction(hostFunction);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    InvokeHostFunctionOp op = new InvokeHostFunctionOp();
    op.setHostFunction(this.hostFunction);
    op.setAuth(this.auth.toArray(new SorobanAuthorizationEntry[0]));

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.INVOKE_HOST_FUNCTION);
    body.setInvokeHostFunctionOp(op);

    return body;
  }
}
