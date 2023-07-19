package org.stellar.sdk;

import static org.junit.Assert.*;

import java.util.Collections;
import org.junit.Test;
import org.stellar.sdk.xdr.*;

public class InvokeHostFunctionOperationTest {
  CreateContractArgs createContractArgs =
      new CreateContractArgs.Builder()
          .contractIDPreimage(
              new ContractIDPreimage.Builder()
                  .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ADDRESS)
                  .fromAddress(
                      new ContractIDPreimage.ContractIDPreimageFromAddress.Builder()
                          .address(
                              new Address(
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
                                new Address(
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
                                new Address(
                                        "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW")
                                    .toSCAddress())
                            .nonce(new Int64(123123432L))
                            .signatureExpirationLedger(new Uint32(10))
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
}
