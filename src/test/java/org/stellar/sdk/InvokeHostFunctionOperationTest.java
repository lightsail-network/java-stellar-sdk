package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.stellar.sdk.scval.ScvAddress;
import org.stellar.sdk.xdr.ContractExecutable;
import org.stellar.sdk.xdr.ContractExecutableType;
import org.stellar.sdk.xdr.ContractIDPreimage;
import org.stellar.sdk.xdr.ContractIDPreimageType;
import org.stellar.sdk.xdr.CreateContractArgs;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.HostFunction;
import org.stellar.sdk.xdr.HostFunctionType;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.SCSymbol;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.SCVec;
import org.stellar.sdk.xdr.SorobanAddressCredentials;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;
import org.stellar.sdk.xdr.SorobanAuthorizedFunction;
import org.stellar.sdk.xdr.SorobanAuthorizedFunctionType;
import org.stellar.sdk.xdr.SorobanAuthorizedInvocation;
import org.stellar.sdk.xdr.SorobanCredentials;
import org.stellar.sdk.xdr.SorobanCredentialsType;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrString;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

public class InvokeHostFunctionOperationTest {
  CreateContractArgs createContractArgs =
      new CreateContractArgs.Builder()
          .contractIDPreimage(
              new ContractIDPreimage.Builder()
                  .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ADDRESS)
                  .fromAddress(
                      new ContractIDPreimage.ContractIDPreimageFromAddress.Builder()
                          .address(
                              new ScvAddress(
                                      "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
                                  .toSCAddress())
                          .salt(new Uint256(new byte[32]))
                          .build())
                  .build())
          .executable(
              new ContractExecutable.Builder()
                  .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_TOKEN)
                  .build())
          .build();

  @Test
  public void testConstructorsFromHostFunction() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    HostFunction hostFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    InvokeHostFunctionOperation invokeHostFunctionOperation =
        InvokeHostFunctionOperation.builder()
            .hostFunction(hostFunction)
            .sourceAccount(source)
            .build();
    assertEquals(invokeHostFunctionOperation.getHostFunction(), hostFunction);
    assertTrue(invokeHostFunctionOperation.getAuth().isEmpty());
    String expectXdr =
        "AAAAAQAAAAAk4TTtavBWsGnEN3KxHw4Ohwi22ZJHWi8hlamN5pm0TgAAABgAAAABAAAAAAAAAAAAAAAAfzBiNMmJ6dZ/ad/ZMRmLYA89bOa2TCJAcB+2KwiDK4cAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAA";
    assertEquals(expectXdr, invokeHostFunctionOperation.toXdrBase64());
  }

  @Test
  public void testConstructorsFromHostFunctionAndSorobanAuthorizationEntries() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    HostFunction hostFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    SorobanAuthorizationEntry auth =
        new SorobanAuthorizationEntry.Builder()
            .credentials(
                new SorobanCredentials.Builder()
                    .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_SOURCE_ACCOUNT)
                    .build())
            .rootInvocation(
                new SorobanAuthorizedInvocation.Builder()
                    .subInvocations(new SorobanAuthorizedInvocation[] {})
                    .function(
                        new SorobanAuthorizedFunction.Builder()
                            .discriminant(
                                SorobanAuthorizedFunctionType
                                    .SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_HOST_FN)
                            .createContractHostFn(createContractArgs)
                            .build())
                    .build())
            .build();
    InvokeHostFunctionOperation invokeHostFunctionOperation =
        InvokeHostFunctionOperation.builder()
            .hostFunction(hostFunction)
            .auth(Collections.singletonList(auth))
            .sourceAccount(source)
            .build();
    invokeHostFunctionOperation.setSourceAccount(source);
    assertEquals(invokeHostFunctionOperation.getHostFunction(), hostFunction);
    assertEquals(invokeHostFunctionOperation.getAuth(), Collections.singletonList(auth));
    String expectXdr =
        "AAAAAQAAAAAk4TTtavBWsGnEN3KxHw4Ohwi22ZJHWi8hlamN5pm0TgAAABgAAAABAAAAAAAAAAAAAAAAfzBiNMmJ6dZ/ad/ZMRmLYA89bOa2TCJAcB+2KwiDK4cAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAABAAAAAAAAAAEAAAAAAAAAAAAAAAB/MGI0yYnp1n9p39kxGYtgDz1s5rZMIkBwH7YrCIMrhwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAA=";
    assertEquals(expectXdr, invokeHostFunctionOperation.toXdrBase64());
  }

  @Test
  public void testFromXdr() {
    HostFunction hostFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    InvokeHostFunctionOperation originOp =
        InvokeHostFunctionOperation.builder().hostFunction(hostFunction).build();
    org.stellar.sdk.xdr.Operation xdrObject = originOp.toXdr();
    Operation restartOp = Operation.fromXdr(xdrObject);
    assertEquals(restartOp, originOp);
  }

  @Test
  public void testEquals() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    HostFunction hostFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    InvokeHostFunctionOperation operation1 =
        InvokeHostFunctionOperation.builder()
            .hostFunction(hostFunction)
            .sourceAccount(source)
            .build();
    InvokeHostFunctionOperation operation2 =
        InvokeHostFunctionOperation.builder()
            .hostFunction(hostFunction)
            .sourceAccount(source)
            .build();
    assertEquals(operation1, operation2);
  }

  @Test
  public void testNotEqualsHostFunction() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    HostFunction hostFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    InvokeHostFunctionOperation operation1 =
        InvokeHostFunctionOperation.builder()
            .hostFunction(hostFunction)
            .sourceAccount(source)
            .build();

    CreateContractArgs createContractArgs2 =
        new CreateContractArgs.Builder()
            .contractIDPreimage(
                new ContractIDPreimage.Builder()
                    .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ADDRESS)
                    .fromAddress(
                        new ContractIDPreimage.ContractIDPreimageFromAddress.Builder()
                            .address(
                                new ScvAddress(
                                        "GAHJJJKMOKYE4RVPZEWZTKH5FVI4PA3VL7GK2LFNUBSGBV6OJP7TQSLX")
                                    .toSCAddress())
                            .salt(new Uint256(new byte[32]))
                            .build())
                    .build())
            .executable(
                new ContractExecutable.Builder()
                    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_TOKEN)
                    .build())
            .build();
    HostFunction hostFunction2 =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs2)
            .build();
    InvokeHostFunctionOperation operation2 =
        InvokeHostFunctionOperation.builder()
            .hostFunction(hostFunction2)
            .sourceAccount(source)
            .build();
    assertNotEquals(operation1, operation2);
  }

  @Test
  public void testNotEqualsAuth() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    HostFunction hostFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    SorobanAuthorizationEntry auth1 =
        new SorobanAuthorizationEntry.Builder()
            .credentials(
                new SorobanCredentials.Builder()
                    .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
                    .address(
                        new SorobanAddressCredentials.Builder()
                            .address(
                                new ScvAddress(
                                        "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW")
                                    .toSCAddress())
                            .nonce(new Int64(123123432L))
                            .signatureExpirationLedger(new Uint32(new XdrUnsignedInteger(10)))
                            .signatureArgs(new SCVec(new SCVal[] {}))
                            .build())
                    .build())
            .rootInvocation(
                new SorobanAuthorizedInvocation.Builder()
                    .subInvocations(new SorobanAuthorizedInvocation[] {})
                    .function(
                        new SorobanAuthorizedFunction.Builder()
                            .discriminant(
                                SorobanAuthorizedFunctionType
                                    .SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_HOST_FN)
                            .createContractHostFn(createContractArgs)
                            .build())
                    .build())
            .build();

    SorobanAuthorizationEntry auth2 =
        new SorobanAuthorizationEntry.Builder()
            .credentials(
                new SorobanCredentials.Builder()
                    .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_SOURCE_ACCOUNT)
                    .build())
            .rootInvocation(
                new SorobanAuthorizedInvocation.Builder()
                    .subInvocations(new SorobanAuthorizedInvocation[] {})
                    .function(
                        new SorobanAuthorizedFunction.Builder()
                            .discriminant(
                                SorobanAuthorizedFunctionType
                                    .SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_HOST_FN)
                            .createContractHostFn(createContractArgs)
                            .build())
                    .build())
            .build();
    InvokeHostFunctionOperation operation1 =
        InvokeHostFunctionOperation.builder()
            .hostFunction(hostFunction)
            .auth(Collections.singletonList(auth1))
            .sourceAccount(source)
            .build();
    InvokeHostFunctionOperation operation2 =
        InvokeHostFunctionOperation.builder()
            .hostFunction(hostFunction)
            .auth(Collections.singletonList(auth2))
            .sourceAccount(source)
            .build();
    assertNotEquals(operation1, operation2);
  }

  @Test
  public void testNotEqualsSource() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    HostFunction hostFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    InvokeHostFunctionOperation operation1 =
        InvokeHostFunctionOperation.builder()
            .hostFunction(hostFunction)
            .sourceAccount(source)
            .build();
    InvokeHostFunctionOperation operation2 =
        InvokeHostFunctionOperation.builder().hostFunction(hostFunction).build();
    assertNotEquals(operation1, operation2);
  }

  @Test
  public void testUploadContractWasmOperationBuilder() {
    byte[] wasm = new byte[] {0x00, 0x01, 0x02, 0x03, 0x34, 0x45, 0x66, 0x46};
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.uploadContractWasmOperationBuilder(wasm).build();
    HostFunction expectedFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_UPLOAD_CONTRACT_WASM)
            .wasm(wasm)
            .build();
    assertEquals(operation.getHostFunction(), expectedFunction);
    assertTrue(operation.getAuth().isEmpty());
    assertNull(operation.getSourceAccount());
    String expectedXdr = "AAAAAAAAABgAAAACAAAACAABAgM0RWZGAAAAAA==";
    assertEquals(expectedXdr, operation.toXdrBase64());
  }

  @Test
  public void createContractOperationBuilderWithWasmIdString() {
    byte[] wasmId =
        new byte[] {
          0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e,
          0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d,
          0x1e, 0x1f
        };
    String wasmIdString = Util.bytesToHex(wasmId);
    byte[] salt =
        new byte[] {
          0x11, 0x33, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e,
          0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d,
          0x1e, 0x1f
        };
    ScvAddress address = new ScvAddress("GAHJJJKMOKYE4RVPZEWZTKH5FVI4PA3VL7GK2LFNUBSGBV6OJP7TQSLX");
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.createContractOperationBuilder(wasmIdString, address, salt)
            .build();

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
    HostFunction expectedFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();

    assertEquals(operation.getHostFunction(), expectedFunction);
    assertTrue(operation.getAuth().isEmpty());
    assertNull(operation.getSourceAccount());
    String expectedXdr =
        "AAAAAAAAABgAAAABAAAAAAAAAAAAAAAADpSlTHKwTkavyS2ZqP0tUceDdV/MrSytoGRg185L/zgRMwIDBAUGBwgJCgsMDQ4PEBESExQVFhcYGRobHB0eHwAAAAAAAQIDBAUGBwgJCgsMDQ4PEBESExQVFhcYGRobHB0eHwAAAAA=";
    assertEquals(expectedXdr, operation.toXdrBase64());
  }

  @Test
  public void createContractOperationBuilderWithWasmIdBytes() {
    byte[] wasmId =
        new byte[] {
          0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e,
          0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d,
          0x1e, 0x1f
        };
    byte[] salt =
        new byte[] {
          0x11, 0x33, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e,
          0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d,
          0x1e, 0x1f
        };
    ScvAddress address = new ScvAddress("GAHJJJKMOKYE4RVPZEWZTKH5FVI4PA3VL7GK2LFNUBSGBV6OJP7TQSLX");
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.createContractOperationBuilder(wasmId, address, salt).build();

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
    HostFunction expectedFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();

    assertEquals(operation.getHostFunction(), expectedFunction);
    assertTrue(operation.getAuth().isEmpty());
    assertNull(operation.getSourceAccount());
    String expectedXdr =
        "AAAAAAAAABgAAAABAAAAAAAAAAAAAAAADpSlTHKwTkavyS2ZqP0tUceDdV/MrSytoGRg185L/zgRMwIDBAUGBwgJCgsMDQ4PEBESExQVFhcYGRobHB0eHwAAAAAAAQIDBAUGBwgJCgsMDQ4PEBESExQVFhcYGRobHB0eHwAAAAA=";
    assertEquals(expectedXdr, operation.toXdrBase64());
  }

  @Test
  public void createTokenContractOperationBuilderWithAddress() {
    ScvAddress address = new ScvAddress("GAHJJJKMOKYE4RVPZEWZTKH5FVI4PA3VL7GK2LFNUBSGBV6OJP7TQSLX");
    byte[] salt =
        new byte[] {
          0x11, 0x33, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e,
          0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d,
          0x1e, 0x1f
        };
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.createTokenContractOperationBuilder(address, salt).build();
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
    HostFunction expectedFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();

    assertEquals(operation.getHostFunction(), expectedFunction);
    assertTrue(operation.getAuth().isEmpty());
    assertNull(operation.getSourceAccount());
    String expectedXdr =
        "AAAAAAAAABgAAAABAAAAAAAAAAAAAAAADpSlTHKwTkavyS2ZqP0tUceDdV/MrSytoGRg185L/zgRMwIDBAUGBwgJCgsMDQ4PEBESExQVFhcYGRobHB0eHwAAAAEAAAAA";
    assertEquals(expectedXdr, operation.toXdrBase64());
  }

  @Test
  public void createTokenContractOperationBuilderWithAsset() {
    Asset asset =
        new AssetTypeCreditAlphaNum4(
            "CAT", "GAHJJJKMOKYE4RVPZEWZTKH5FVI4PA3VL7GK2LFNUBSGBV6OJP7TQSLX");
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.createTokenContractOperationBuilder(asset).build();
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
    HostFunction expectedFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();

    assertEquals(operation.getHostFunction(), expectedFunction);
    assertTrue(operation.getAuth().isEmpty());
    assertNull(operation.getSourceAccount());
    String expectedXdr =
        "AAAAAAAAABgAAAABAAAAAQAAAAFDQVQAAAAAAA6UpUxysE5Gr8ktmaj9LVHHg3VfzK0sraBkYNfOS/84AAAAAQAAAAA=";
    assertEquals(expectedXdr, operation.toXdrBase64());
  }

  @Test
  public void invokeContractFunctionOperationBuilder() {
    String contractId = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    String funcName = "hello";
    List<SCVal> parameters =
        Collections.singletonList(
            new SCVal.Builder()
                .discriminant(SCValType.SCV_SYMBOL)
                .sym(new SCSymbol(new XdrString("world")))
                .build());
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.invokeContractFunctionOperationBuilder(
                contractId, funcName, parameters)
            .build();

    SCVal contractIdScVal = new ScvAddress(contractId).toSCVal();
    SCVal functionNameScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_SYMBOL)
            .sym(new SCSymbol(new XdrString(funcName)))
            .build();
    SCVal paramScVal = parameters.get(0);
    List<SCVal> invokeContractParams =
        Arrays.asList(contractIdScVal, functionNameScVal, paramScVal);
    HostFunction expectedFunction =
        new HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_INVOKE_CONTRACT)
            .invokeContract(new SCVec(invokeContractParams.toArray(new SCVal[0])))
            .build();

    assertEquals(operation.getHostFunction(), expectedFunction);
    assertTrue(operation.getAuth().isEmpty());
    assertNull(operation.getSourceAccount());
    String expectedXdr =
        "AAAAAAAAABgAAAAAAAAAAwAAABIAAAABPww0v5OtDZlx0EzMkPcFURyDiq2XNKSi+w16A/x/6JoAAAAPAAAABWhlbGxvAAAAAAAADwAAAAV3b3JsZAAAAAAAAAA=";
    assertEquals(expectedXdr, operation.toXdrBase64());
  }
}
