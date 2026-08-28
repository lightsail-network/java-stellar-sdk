package org.stellar.sdk.contract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.requests.sorobanrpc.SimulateTransactionRequest;
import org.stellar.sdk.requests.sorobanrpc.SorobanRpcRequest;
import org.stellar.sdk.xdr.SCVal;

/**
 * Covers how the CAP-71 {@code useUpgradedAuth} flag flows from the contract layer down to the
 * simulateTransaction request.
 */
public class ContractClientUseUpgradedAuthTest {
  private static final String CONTRACT_ID =
      "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
  private static final KeyPair SIGNER =
      KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");

  @Test
  public void testInvokeSimulatesWithUseUpgradedAuthByDefault() throws IOException {
    try (MockWebServer mockWebServer = new MockWebServer()) {
      RecordingDispatcher dispatcher = new RecordingDispatcher();
      mockWebServer.setDispatcher(dispatcher);
      mockWebServer.start();

      try (ContractClient client =
          new ContractClient(CONTRACT_ID, mockWebServer.url("").toString(), Network.TESTNET)) {
        client.invoke(
            "increment", Collections.<SCVal>emptyList(), SIGNER.getAccountId(), SIGNER, null, 100);
      }

      assertEquals(1, dispatcher.simulateRequests.size());
      assertTrue(dispatcher.simulateRequests.get(0).isUseUpgradedAuth());
    }
  }

  @Test
  public void testInvokeWithUseUpgradedAuthDisabled() throws IOException {
    try (MockWebServer mockWebServer = new MockWebServer()) {
      RecordingDispatcher dispatcher = new RecordingDispatcher();
      mockWebServer.setDispatcher(dispatcher);
      mockWebServer.start();

      try (ContractClient client =
          new ContractClient(CONTRACT_ID, mockWebServer.url("").toString(), Network.TESTNET)) {
        client.invoke(
            "increment",
            Collections.<SCVal>emptyList(),
            SIGNER.getAccountId(),
            SIGNER,
            null,
            100,
            300,
            30,
            true,
            true,
            false);
      }

      assertEquals(1, dispatcher.simulateRequests.size());
      assertFalse(dispatcher.simulateRequests.get(0).isUseUpgradedAuth());
    }
  }

  @Test
  public void testAssembledTransactionSimulateWithUseUpgradedAuthDisabled() throws IOException {
    try (MockWebServer mockWebServer = new MockWebServer()) {
      RecordingDispatcher dispatcher = new RecordingDispatcher();
      mockWebServer.setDispatcher(dispatcher);
      mockWebServer.start();

      try (ContractClient client =
          new ContractClient(CONTRACT_ID, mockWebServer.url("").toString(), Network.TESTNET)) {
        AssembledTransaction<SCVal> assembled =
            client.invoke(
                "increment",
                Collections.<SCVal>emptyList(),
                SIGNER.getAccountId(),
                SIGNER,
                null,
                100,
                300,
                30,
                false,
                false);
        assertEquals(0, dispatcher.simulateRequests.size());

        assembled.simulate(false, false);
        assertEquals(1, dispatcher.simulateRequests.size());
        assertFalse(dispatcher.simulateRequests.get(0).isUseUpgradedAuth());

        assembled.simulate(false);
        assertEquals(2, dispatcher.simulateRequests.size());
        assertTrue(dispatcher.simulateRequests.get(1).isUseUpgradedAuth());
      }
    }
  }

  @Test
  public void testAutomaticRestoreInheritsUseUpgradedAuth() throws IOException {
    // The restore transaction is derived from this one, so it simulates with the same credential
    // format instead of falling back to the ADDRESS_V2 default.
    try (MockWebServer mockWebServer = new MockWebServer()) {
      RestoringDispatcher dispatcher = new RestoringDispatcher();
      mockWebServer.setDispatcher(dispatcher);
      mockWebServer.start();

      try (ContractClient client =
          new ContractClient(CONTRACT_ID, mockWebServer.url("").toString(), Network.TESTNET)) {
        client.invoke(
            "increment",
            Collections.<SCVal>emptyList(),
            SIGNER.getAccountId(),
            SIGNER,
            null,
            100,
            300,
            30,
            true,
            true,
            false);
      }

      // The initial simulation, the restore transaction's own simulation, and the re-simulation.
      assertEquals(3, dispatcher.simulateRequests.size());
      for (SimulateTransactionRequest request : dispatcher.simulateRequests) {
        assertFalse(request.isUseUpgradedAuth());
      }
    }
  }

  /** Answers the account lookup and the simulation, recording every simulation request. */
  private static final class RecordingDispatcher extends Dispatcher {
    private final Gson gson = new Gson();
    private final String accountJson;
    private final String simulateJson;
    final List<SimulateTransactionRequest> simulateRequests = new ArrayList<>();

    RecordingDispatcher() throws IOException {
      accountJson =
          new String(
              Files.readAllBytes(Paths.get("src/test/resources/soroban_server/get_account.json")));
      simulateJson =
          new String(
              Files.readAllBytes(
                  Paths.get("src/test/resources/soroban_server/simulate_transaction.json")));
    }

    @NotNull
    @Override
    public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
      String body = recordedRequest.getBody().readUtf8();
      String method = gson.fromJson(body, JsonObject.class).get("method").getAsString();
      if ("getLedgerEntries".equals(method)) {
        return new MockResponse().setResponseCode(200).setBody(accountJson);
      }
      if ("simulateTransaction".equals(method)) {
        SorobanRpcRequest<SimulateTransactionRequest> request =
            gson.fromJson(
                body, new TypeToken<SorobanRpcRequest<SimulateTransactionRequest>>() {}.getType());
        simulateRequests.add(request.getParams());
        return new MockResponse().setResponseCode(200).setBody(simulateJson);
      }
      return new MockResponse().setResponseCode(404);
    }
  }

  /**
   * Drives the automatic-restoration path: the first simulation reports expired state, the rest
   * answer normally so the restore transaction can be signed and submitted.
   */
  private static final class RestoringDispatcher extends Dispatcher {
    private final Gson gson = new Gson();
    private final String accountJson;
    private final String restorePreambleJson;
    private final String simulateJson;
    private final String sendTransactionJson;
    private final String getTransactionJson;
    final List<SimulateTransactionRequest> simulateRequests = new ArrayList<>();

    RestoringDispatcher() throws IOException {
      accountJson = readFixture("get_account.json");
      restorePreambleJson = readFixture("simulate_transaction_with_restore_preamble.json");
      simulateJson = readFixture("simulate_transaction.json");
      sendTransactionJson = readFixture("send_transaction.json");
      getTransactionJson = readFixture("get_transaction.json");
    }

    private static String readFixture(String name) throws IOException {
      return new String(Files.readAllBytes(Paths.get("src/test/resources/soroban_server/" + name)));
    }

    @NotNull
    @Override
    public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
      String body = recordedRequest.getBody().readUtf8();
      String method = gson.fromJson(body, JsonObject.class).get("method").getAsString();
      switch (method) {
        case "getLedgerEntries":
          return new MockResponse().setResponseCode(200).setBody(accountJson);
        case "simulateTransaction":
          SorobanRpcRequest<SimulateTransactionRequest> request =
              gson.fromJson(
                  body,
                  new TypeToken<SorobanRpcRequest<SimulateTransactionRequest>>() {}.getType());
          simulateRequests.add(request.getParams());
          // Only the first simulation reports expired state; the restore transaction's own
          // simulation must not, or signing it would throw ExpiredStateException.
          String responseJson = simulateRequests.size() == 1 ? restorePreambleJson : simulateJson;
          return new MockResponse().setResponseCode(200).setBody(responseJson);
        case "sendTransaction":
          return new MockResponse().setResponseCode(200).setBody(sendTransactionJson);
        case "getTransaction":
          return new MockResponse().setResponseCode(200).setBody(getTransactionJson);
        default:
          return new MockResponse().setResponseCode(404);
      }
    }
  }
}
