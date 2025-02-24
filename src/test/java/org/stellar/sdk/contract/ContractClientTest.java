package org.stellar.sdk.contract;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import javax.swing.*;
import org.junit.Ignore;
import org.junit.Test;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.contract.exception.NoSignatureNeededException;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.SCVal;

@Ignore
public class ContractClientTest {
  //    private static final String ENABLE_E2E_TESTS_VARIABLE = "ENABLE_E2E_TESTS";

  //    @Before
  //    public void setUp() {
  //        boolean enabled = "true".equals(System.getenv(ENABLE_E2E_TESTS_VARIABLE));
  //        if (!enabled) {
  //            throw new AssumptionViolatedException("Skipping e2e tests in non-CI environment");
  //        }
  //    }
  //

  @Test
  public void testContractClientWithoutParseResultFnSet() throws IOException {
    // stellar contract deploy --source-account dev --network testnet --wasm
    // ./target/wasm32-unknown-unknown/release/soroban_hello_world_contract.wasm --salt
    // 0000000000000000000000000000000000000000000000000000000000000000
    KeyPair submitter =
        KeyPair.fromSecretSeed("SAAPYAPTTRZMCUZFPG3G66V4ZMHTK4TWA6NS7U4F7Z3IMUD52EK4DDEV");
    String contractId = "CD3YDDSN3G3UG3QKNUI3CLWJKIRWGXPRFJDBNMXGDBQDR7THE2OZRGR2";
    String rpcUrl = "https://soroban-testnet.stellar.org";
    ContractClient client = new ContractClient(contractId, rpcUrl, Network.TESTNET);
    AssembledTransaction<SCVal> assembledTransaction =
        client.invoke(
            "hello",
            Collections.singletonList(Scv.toString("Stellar")),
            submitter.getAccountId(),
            null,
            null,
            100);
    assertTrue(assembledTransaction.isReadCall());
    assertEquals(
        assembledTransaction.result(),
        Scv.toVec(Arrays.asList(Scv.toString("Hello"), Scv.toString("Stellar"))));
    client.close();
  }

  @Test
  public void testContractClientWithParseResultFnSet() throws IOException {
    // stellar contract deploy --source-account dev --network testnet --wasm
    // ./target/wasm32-unknown-unknown/release/soroban_hello_world_contract.wasm --salt
    KeyPair submitter =
        KeyPair.fromSecretSeed("SAAPYAPTTRZMCUZFPG3G66V4ZMHTK4TWA6NS7U4F7Z3IMUD52EK4DDEV");
    String contractId = "CD3YDDSN3G3UG3QKNUI3CLWJKIRWGXPRFJDBNMXGDBQDR7THE2OZRGR2";
    String rpcUrl = "https://soroban-testnet.stellar.org";
    ContractClient client = new ContractClient(contractId, rpcUrl, Network.TESTNET);

    Function<SCVal, List<String>> parseResultXdrFn =
        scVal -> {
          Collection<SCVal> vec = Scv.fromVec(scVal);
          Iterator<SCVal> iterator = vec.iterator();
          String first = new String(Scv.fromString(iterator.next()), StandardCharsets.UTF_8);
          String second = new String(Scv.fromString(iterator.next()), StandardCharsets.UTF_8);
          return Arrays.asList(first, second);
        };

    AssembledTransaction<List<String>> assembledTransaction =
        client.invoke(
            "hello",
            Collections.singletonList(Scv.toString("Stellar")),
            submitter.getAccountId(),
            null,
            parseResultXdrFn,
            100);
    assertTrue(assembledTransaction.isReadCall());
    assertEquals(assembledTransaction.result(), Arrays.asList("Hello", "Stellar"));
    client.close();
  }

  @Test
  public void testContractClientForceSendReadCall() throws IOException {
    // stellar contract deploy --source-account dev --network testnet --wasm
    // ./target/wasm32-unknown-unknown/release/soroban_hello_world_contract.wasm --salt
    KeyPair submitter =
        KeyPair.fromSecretSeed("SAAPYAPTTRZMCUZFPG3G66V4ZMHTK4TWA6NS7U4F7Z3IMUD52EK4DDEV");
    String contractId = "CD3YDDSN3G3UG3QKNUI3CLWJKIRWGXPRFJDBNMXGDBQDR7THE2OZRGR2";
    String rpcUrl = "https://soroban-testnet.stellar.org";
    ContractClient client = new ContractClient(contractId, rpcUrl, Network.TESTNET);

    Function<SCVal, List<String>> parseResultXdrFn =
        scVal -> {
          Collection<SCVal> vec = Scv.fromVec(scVal);
          Iterator<SCVal> iterator = vec.iterator();
          String first = new String(Scv.fromString(iterator.next()), StandardCharsets.UTF_8);
          String second = new String(Scv.fromString(iterator.next()), StandardCharsets.UTF_8);
          return Arrays.asList(first, second);
        };

    AssembledTransaction<List<String>> assembledTransaction =
        client.invoke(
            "hello",
            Collections.singletonList(Scv.toString("Stellar")),
            submitter.getAccountId(),
            submitter,
            parseResultXdrFn,
            100);
    assertTrue(assembledTransaction.isReadCall());
    assertEquals(assembledTransaction.result(), Arrays.asList("Hello", "Stellar"));
    assertThrows(
        NoSignatureNeededException.class, () -> assembledTransaction.signAndSubmit(null, false));
    assertEquals(assembledTransaction.signAndSubmit(null, true), Arrays.asList("Hello", "Stellar"));
    client.close();
  }

  @Test
  public void testContractClientSignAuth() throws IOException {
    // stellar contract deploy --source-account dev --network testnet --wasm
    // ./target/wasm32-unknown-unknown/release/soroban_atomic_swap_contract.wasm --salt
    // 0000000000000000000000000000000000000000000000000000000000000000
    KeyPair submitter =
        KeyPair.fromSecretSeed("SAAPYAPTTRZMCUZFPG3G66V4ZMHTK4TWA6NS7U4F7Z3IMUD52EK4DDEV");
    KeyPair alice =
        KeyPair.fromSecretSeed("SBPTTA3D3QYQ6E2GSACAZDUFH2UILBNG3EBJCK3NNP7BE4O757KGZUGA");
    KeyPair bob =
        KeyPair.fromSecretSeed("SBJQCT3YSSVRHVGNMGDHJ35SZ635KXPJGGDEBHWWKCPZ7ZY46H2LM7KM");

    String swapContractId = "CDLMS36NCJ4S35RJVTJG32JB7XV2KEEAP5PRROF4H2UU657DDAIB5ZZI";
    String nativeAssetContractId = "CDLZFC3SYJYDZT7K67VZ75HPJVIEUVNIXF47ZG2FB2RMQQVU2HHGCYSC";
    String customAssetContractId = "CADEDRPB3MIT2QWLK5DGAFR3JMCIZMTEFT6R4KUGW5ZZYCQKAMPR5WAJ";

    String rpcUrl = "https://soroban-testnet.stellar.org";

    ContractClient client = new ContractClient(swapContractId, rpcUrl, Network.TESTNET);

    AssembledTransaction<SCVal> assembledTransaction =
        client.invoke(
            "swap",
            Arrays.asList(
                Scv.toAddress(alice.getAccountId()),
                Scv.toAddress(bob.getAccountId()),
                Scv.toAddress(nativeAssetContractId),
                Scv.toAddress(customAssetContractId),
                Scv.toInt128(BigInteger.valueOf(1000)),
                Scv.toInt128(BigInteger.valueOf(4500)),
                Scv.toInt128(BigInteger.valueOf(5000)),
                Scv.toInt128(BigInteger.valueOf(950))),
            submitter.getAccountId(),
            submitter,
            null,
            100);

    assertFalse(assembledTransaction.isReadCall());
    assertEquals(
        assembledTransaction.needsNonInvokerSigningBy(false),
        new HashSet<>(Arrays.asList(alice.getAccountId(), bob.getAccountId())));
    assembledTransaction.signAuthEntries(alice);
    assertEquals(
        assembledTransaction.needsNonInvokerSigningBy(false),
        new HashSet<>(Collections.singletonList(bob.getAccountId())));
    assembledTransaction.signAuthEntries(bob);
    assertEquals(assembledTransaction.needsNonInvokerSigningBy(false), new HashSet<>());
    SCVal result = assembledTransaction.signAndSubmit(submitter, false);
    assertEquals(result, Scv.toVoid());
    client.close();
  }
}
