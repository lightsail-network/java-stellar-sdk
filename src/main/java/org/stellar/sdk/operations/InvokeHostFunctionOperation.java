package org.stellar.sdk.operations;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.Address;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.ContractExecutable;
import org.stellar.sdk.xdr.ContractExecutableExternalRef;
import org.stellar.sdk.xdr.ContractExecutableType;
import org.stellar.sdk.xdr.ContractIDPreimage;
import org.stellar.sdk.xdr.ContractIDPreimageType;
import org.stellar.sdk.xdr.CreateContractArgs;
import org.stellar.sdk.xdr.CreateContractArgsV2;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.HostFunction;
import org.stellar.sdk.xdr.HostFunctionType;
import org.stellar.sdk.xdr.InvokeContractArgs;
import org.stellar.sdk.xdr.InvokeHostFunctionOp;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.SCString;
import org.stellar.sdk.xdr.SCSymbol;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.XdrString;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#invoke-host-function"
 * target="_blank">InvokeHostFunction</a> operation.
 *
 * @see org.stellar.sdk.scval.Scv
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class InvokeHostFunctionOperation extends Operation {

  /** The host function to invoke. */
  @NonNull private final HostFunction hostFunction;

  /** The authorizations required to execute the host function */
  @NonNull @Builder.Default private final List<SorobanAuthorizationEntry> auth = new ArrayList<>();

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

  /**
   * This function will create an {@link InvokeHostFunctionOperationBuilder} with the "hostFunction"
   * parameter preset, so that you can conveniently build an {@link InvokeHostFunctionOperation} to
   * upload contract wasm.
   *
   * @param wasm The wasm bytes to upload.
   * @return {@link InvokeHostFunctionOperationBuilder}
   */
  public static InvokeHostFunctionOperationBuilder<?, ?> uploadContractWasmOperationBuilder(
      byte[] wasm) {
    HostFunction hostFunction =
        HostFunction.builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_UPLOAD_CONTRACT_WASM)
            .wasm(wasm)
            .build();
    return builder().hostFunction(hostFunction);
  }

  /**
   * This function will create an {@link InvokeHostFunctionOperationBuilder} with the "hostFunction"
   * parameter preset, so that you can conveniently build an {@link InvokeHostFunctionOperation} to
   * create contract.
   *
   * @param wasmId The hex-encoded wasm id to use for contract creation.
   * @param address The address to use to derive the contract ID.
   * @param constructorArgs The optional parameters to pass to the constructor of this contract.
   * @param salt The 32-byte salt to use to derive the contract ID, if null, a random salt will be
   *     generated.
   * @return {@link InvokeHostFunctionOperationBuilder}
   */
  public static InvokeHostFunctionOperationBuilder<?, ?> createContractOperationBuilder(
      String wasmId,
      Address address,
      @Nullable Collection<SCVal> constructorArgs,
      @Nullable byte[] salt) {
    byte[] wasmIdBytes = Util.hexToBytes(wasmId);
    return createContractOperationBuilder(wasmIdBytes, address, constructorArgs, salt);
  }

  /**
   * This function will create an {@link InvokeHostFunctionOperationBuilder} with the "hostFunction"
   * parameter preset, so that you can conveniently build an {@link InvokeHostFunctionOperation} to
   * create contract.
   *
   * @param wasmId The wasm id to use for contract creation.
   * @param address The address to use to derive the contract ID.
   * @param constructorArgs The optional parameters to pass to the constructor of this contract.
   * @param salt The 32-byte salt to use to derive the contract ID, if null, a random salt will be
   *     generated.
   * @return {@link InvokeHostFunctionOperationBuilder}
   */
  public static InvokeHostFunctionOperationBuilder<?, ?> createContractOperationBuilder(
      byte[] wasmId,
      Address address,
      @Nullable Collection<SCVal> constructorArgs,
      @Nullable byte[] salt) {
    if (wasmId.length != 32) {
      throw new IllegalArgumentException("\"wasmId\" must be 32 bytes long");
    }

    ContractExecutable executable =
        ContractExecutable.builder()
            .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_WASM)
            .wasm_hash(new Hash(wasmId))
            .build();
    return createContractOperationBuilder(executable, address, constructorArgs, salt);
  }

  /**
   * This function will create an {@link InvokeHostFunctionOperationBuilder} with the "hostFunction"
   * parameter preset, so that you can conveniently build an {@link InvokeHostFunctionOperation} to
   * create a contract from a <a href="https://stellar.org/protocol/cap-85"
   * target="_blank">CAP-85</a> external executable reference instead of from an uploaded Wasm hash.
   *
   * <p>The reference names an owner contract and a tag; the owner publishes the Wasm hash under
   * that tag, and the created contract follows it, so the owner can upgrade every contract that
   * references the tag at once.
   *
   * @param owner The contract that owns the executable. Only a contract can hold the persistent tag
   *     entry that names the Wasm, so this must be a contract address.
   * @param tag The owner-scoped tag naming the executable, encoded as UTF-8. Use {@link
   *     #createContractFromExternalRefOperationBuilder(Address, byte[], Address, Collection,
   *     byte[])} for a tag that is not text.
   * @param address The address to use to derive the contract ID.
   * @param constructorArgs The optional parameters to pass to the constructor of this contract.
   * @param salt The 32-byte salt to use to derive the contract ID, if null, a random salt will be
   *     generated.
   * @return {@link InvokeHostFunctionOperationBuilder}
   */
  public static InvokeHostFunctionOperationBuilder<?, ?>
      createContractFromExternalRefOperationBuilder(
          Address owner,
          String tag,
          Address address,
          @Nullable Collection<SCVal> constructorArgs,
          @Nullable byte[] salt) {
    return createContractFromExternalRefOperationBuilder(
        owner, tag.getBytes(StandardCharsets.UTF_8), address, constructorArgs, salt);
  }

  /**
   * This function will create an {@link InvokeHostFunctionOperationBuilder} with the "hostFunction"
   * parameter preset, so that you can conveniently build an {@link InvokeHostFunctionOperation} to
   * create a contract from a <a href="https://stellar.org/protocol/cap-85"
   * target="_blank">CAP-85</a> external executable reference instead of from an uploaded Wasm hash.
   *
   * <p>See {@link #createContractFromExternalRefOperationBuilder(Address, String, Address,
   * Collection, byte[])} for what the reference names. A tag is an unbounded {@code SCString} and
   * need not be valid UTF-8, so binary tags are passed through here undecoded.
   *
   * @param owner The contract that owns the executable. Only a contract can hold the persistent tag
   *     entry that names the Wasm, so this must be a contract address.
   * @param tag The owner-scoped tag naming the executable.
   * @param address The address to use to derive the contract ID.
   * @param constructorArgs The optional parameters to pass to the constructor of this contract.
   * @param salt The 32-byte salt to use to derive the contract ID, if null, a random salt will be
   *     generated.
   * @return {@link InvokeHostFunctionOperationBuilder}
   */
  public static InvokeHostFunctionOperationBuilder<?, ?>
      createContractFromExternalRefOperationBuilder(
          Address owner,
          byte[] tag,
          Address address,
          @Nullable Collection<SCVal> constructorArgs,
          @Nullable byte[] salt) {
    // Only a contract can hold the persistent tag entry that names the Wasm, so any other owner is
    // unresolvable and the deploy would fail on-chain.
    if (owner.getAddressType() != Address.AddressType.CONTRACT) {
      throw new IllegalArgumentException("\"owner\" must be a contract address");
    }
    // Caught here rather than at encode time, where it would surface as a NullPointerException
    // from deep inside XDR serialization.
    if (tag == null) {
      throw new IllegalArgumentException("\"tag\" must not be null");
    }

    ContractExecutable executable =
        ContractExecutable.builder()
            .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_EXTERNAL_REF)
            .external_ref(
                ContractExecutableExternalRef.builder()
                    .executable_owner(owner.toSCAddress())
                    .tag(new SCString(new XdrString(tag)))
                    .build())
            .build();
    return createContractOperationBuilder(executable, address, constructorArgs, salt);
  }

  /**
   * Builds a {@code CREATE_CONTRACT_V2} host function that deploys {@code executable} to the
   * contract ID derived from {@code address} and {@code salt}.
   */
  private static InvokeHostFunctionOperationBuilder<?, ?> createContractOperationBuilder(
      ContractExecutable executable,
      Address address,
      @Nullable Collection<SCVal> constructorArgs,
      @Nullable byte[] salt) {
    if (salt == null) {
      salt = new byte[32];
      new SecureRandom().nextBytes(salt);
    } else if (salt.length != 32) {
      throw new IllegalArgumentException("\"salt\" must be 32 bytes long");
    }

    CreateContractArgsV2 createContractArgs =
        CreateContractArgsV2.builder()
            .contractIDPreimage(
                ContractIDPreimage.builder()
                    .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ADDRESS)
                    .fromAddress(
                        ContractIDPreimage.ContractIDPreimageFromAddress.builder()
                            .address(address.toSCAddress())
                            .salt(new Uint256(salt))
                            .build())
                    .build())
            .executable(executable)
            .constructorArgs(
                constructorArgs != null ? constructorArgs.toArray(new SCVal[0]) : new SCVal[0])
            .build();
    HostFunction hostFunction =
        HostFunction.builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT_V2)
            .createContractV2(createContractArgs)
            .build();
    return builder().hostFunction(hostFunction);
  }

  /**
   * This function will create an {@link InvokeHostFunctionOperationBuilder} with the "hostFunction"
   * parameter preset, so that you can conveniently build an {@link InvokeHostFunctionOperation} to
   * create a token contract wrapping a classic asset.
   *
   * @param asset The classic asset to wrap.
   * @return {@link InvokeHostFunctionOperationBuilder}
   */
  public static InvokeHostFunctionOperationBuilder<?, ?> createStellarAssetContractOperationBuilder(
      Asset asset) {
    CreateContractArgs createContractArgs =
        CreateContractArgs.builder()
            .contractIDPreimage(
                ContractIDPreimage.builder()
                    .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ASSET)
                    .fromAsset(asset.toXdr())
                    .build())
            .executable(
                ContractExecutable.builder()
                    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_STELLAR_ASSET)
                    .build())
            .build();
    HostFunction hostFunction =
        HostFunction.builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    return builder().hostFunction(hostFunction);
  }

  /**
   * This function will create an {@link InvokeHostFunctionOperationBuilder} with the "hostFunction"
   * parameter preset, so that you can conveniently build an {@link InvokeHostFunctionOperation} to
   * invoke a contract function.
   *
   * @param contractId The ID of the contract to invoke.
   * @param functionName The name of the function to invoke.
   * @param parameters The parameters to pass to the method.
   * @return {@link InvokeHostFunctionOperationBuilder}
   * @see org.stellar.sdk.scval.Scv
   * @see <a
   *     href="https://developers.stellar.org/docs/build/guides/transactions/invoke-contract-tx-sdk"
   *     target="_blank">Interacting with Contracts</a>
   */
  public static InvokeHostFunctionOperationBuilder<?, ?> invokeContractFunctionOperationBuilder(
      String contractId, String functionName, @Nullable Collection<SCVal> parameters) {
    Address address = new Address(contractId);
    if (address.getAddressType() != Address.AddressType.CONTRACT) {
      throw new IllegalArgumentException("\"contractId\" must be a contract address");
    }
    SCSymbol functionNameSCSymbol = new SCSymbol(new XdrString(functionName));
    List<SCVal> invokeContractParams =
        new ArrayList<>((parameters != null ? parameters.size() : 0));

    if (parameters != null) {
      for (SCVal parameter : parameters) {
        if (parameter == null) {
          throw new IllegalArgumentException("\"parameters\" contains null element");
        }
        invokeContractParams.add(parameter);
      }
    }

    InvokeContractArgs invokeContractArgs =
        InvokeContractArgs.builder()
            .contractAddress(address.toSCAddress())
            .functionName(functionNameSCSymbol)
            .args(invokeContractParams.toArray(new SCVal[0]))
            .build();

    HostFunction hostFunction =
        HostFunction.builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_INVOKE_CONTRACT)
            .invokeContract(invokeContractArgs)
            .build();
    return builder().hostFunction(hostFunction);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
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
