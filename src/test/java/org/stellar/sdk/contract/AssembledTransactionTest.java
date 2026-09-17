package org.stellar.sdk.contract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.stellar.sdk.Account;
import org.stellar.sdk.Address;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.SorobanServer;
import org.stellar.sdk.TimeBounds;
import org.stellar.sdk.TransactionBuilder;
import org.stellar.sdk.TransactionPreconditions;
import org.stellar.sdk.contract.exception.TransactionStillPendingException;
import org.stellar.sdk.exception.SorobanRpcException;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.responses.sorobanrpc.GetTransactionResponse;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.SCVal;

/**
 * Drives {@link AssembledTransaction#submit()} against a {@link MockWebServer} that scripts the
 * {@code getTransaction} answers, to pin down the polling behaviour. The clock and sleeper are
 * faked so the deadline and backoff arithmetic are exercised without real waiting.
 */
public class AssembledTransactionTest {
  private static final String FIXTURES = "src/test/resources/soroban_server/";

  private String getAccountJson;
  private String simulateJson;
  private String sendJson;
  private String txSuccessJson;
  private String txNotFoundJson;
  private String rpcErrorJson;

  private MockWebServer mockWebServer;
  private SorobanServer server;

  /** Number of {@code getTransaction} requests the mock has answered. */
  private final AtomicInteger getTransactionCalls = new AtomicInteger();

  /** Milliseconds each call to the injected sleeper was asked to wait. */
  private final List<Long> sleeps = new ArrayList<>();

  @Before
  public void setUp() throws IOException {
    getAccountJson = readFixture("get_account.json");
    simulateJson = readFixture("simulate_transaction.json");
    sendJson = readFixture("send_transaction.json");
    txSuccessJson = readFixture("get_transaction.json");
    txNotFoundJson = readFixture("get_transaction_not_found.json");
    rpcErrorJson = readFixture("soroban_rpc_error.json");
    mockWebServer = new MockWebServer();
  }

  @After
  public void tearDown() throws IOException {
    if (server != null) {
      server.close();
    }
    mockWebServer.close();
  }

  @Test
  public void submitSleepsWithExponentialBackoffBetweenPolls() {
    // NOT_FOUND three times, then SUCCESS.
    startServer(attempt -> attempt <= 3 ? ok(txNotFoundJson) : ok(txSuccessJson));
    AssembledTransaction<SCVal> tx = buildAndSign(/* submitTimeout= */ 300);
    tx.sleeper = sleeps::add;

    SCVal result = tx.submit();

    assertNotNull(result);
    assertEquals(
        GetTransactionResponse.GetTransactionStatus.SUCCESS,
        tx.getGetTransactionResponse().getStatus());
    // One immediate poll plus one poll after each sleep.
    assertEquals(4, getTransactionCalls.get());
    assertEquals(Arrays.asList(1_000L, 1_500L, 2_250L), sleeps);
  }

  @Test
  public void submitWithZeroTimeoutPollsOnceAndThrowsWhenStillPending() {
    startServer(attempt -> ok(txNotFoundJson));
    AssembledTransaction<SCVal> tx = buildAndSign(/* submitTimeout= */ 0);
    tx.sleeper = sleeps::add;

    try {
      tx.submit();
      fail("expected TransactionStillPendingException");
    } catch (TransactionStillPendingException e) {
      assertEquals(
          GetTransactionResponse.GetTransactionStatus.NOT_FOUND,
          tx.getGetTransactionResponse().getStatus());
    }
    assertEquals(1, getTransactionCalls.get());
    assertTrue(sleeps.isEmpty());
  }

  @Test
  public void submitStopsPollingOnceTheDeadlinePasses() {
    startServer(attempt -> ok(txNotFoundJson));
    AssembledTransaction<SCVal> tx = buildAndSign(/* submitTimeout= */ 2);
    useFakeClock(tx, /* oversleepMillis= */ 0);

    try {
      tx.submit();
      fail("expected TransactionStillPendingException");
    } catch (TransactionStillPendingException e) {
      // expected
    }

    // Immediate poll, sleep 1s, poll, sleep the 1s that is left (not the 1.5s backoff step), one
    // final poll at the deadline, stop.
    assertEquals(3, getTransactionCalls.get());
    assertEquals(Arrays.asList(1_000L, 1_000L), sleeps);
  }

  @Test
  public void submitPollsOnceMoreAfterAnOversleptFinalSleepButDoesNotSleepAgain() {
    startServer(attempt -> ok(txNotFoundJson));
    AssembledTransaction<SCVal> tx = buildAndSign(/* submitTimeout= */ 2);
    // Every sleep overshoots by half a second, as it may under load.
    useFakeClock(tx, /* oversleepMillis= */ 500);

    try {
      tx.submit();
      fail("expected TransactionStillPendingException");
    } catch (TransactionStillPendingException e) {
      // expected
    }

    // Poll at 0ms; sleep 1000 (wakes at 1500); poll; 500ms left so sleep 500 (wakes at 2500);
    // final poll past the deadline; no further sleep.
    assertEquals(3, getTransactionCalls.get());
    assertEquals(Arrays.asList(1_000L, 500L), sleeps);
  }

  @Test
  public void submitCapsTheBackoffAtSixSeconds() {
    // NOT_FOUND eight times, then SUCCESS.
    startServer(attempt -> attempt <= 8 ? ok(txNotFoundJson) : ok(txSuccessJson));
    AssembledTransaction<SCVal> tx = buildAndSign(/* submitTimeout= */ 600);
    useFakeClock(tx, /* oversleepMillis= */ 0);

    tx.submit();

    assertEquals(9, getTransactionCalls.get());
    assertEquals(
        Arrays.asList(1_000L, 1_500L, 2_250L, 3_375L, 5_062L, 6_000L, 6_000L, 6_000L), sleeps);
  }

  @Test
  public void submitWithNegativeTimeoutPollsOnce() {
    startServer(attempt -> ok(txNotFoundJson));
    AssembledTransaction<SCVal> tx = buildAndSign(/* submitTimeout= */ -5);
    useFakeClock(tx, /* oversleepMillis= */ 0);

    try {
      tx.submit();
      fail("expected TransactionStillPendingException");
    } catch (TransactionStillPendingException e) {
      // expected
    }
    assertEquals(1, getTransactionCalls.get());
    assertTrue(sleeps.isEmpty());
  }

  @Test
  public void defaultSleeperRestoresTheInterruptFlagAndThrows() {
    startServer(attempt -> ok(txNotFoundJson));
    AssembledTransaction<SCVal> tx = buildAndSign(/* submitTimeout= */ 2);

    Thread.currentThread().interrupt();
    try {
      tx.sleeper.accept(10_000L);
      fail("expected UnexpectedException");
    } catch (UnexpectedException e) {
      assertTrue(e.getCause() instanceof InterruptedException);
    } finally {
      // Thread.interrupted() reports and clears the flag, so the test thread is left clean.
      assertTrue("interrupt flag should have been restored", Thread.interrupted());
    }
  }

  @Test
  public void submitPropagatesNetworkErrorsRaisedWhilePolling() {
    // NOT_FOUND once, then a JSON-RPC error.
    startServer(attempt -> attempt == 1 ? ok(txNotFoundJson) : ok(rpcErrorJson));
    AssembledTransaction<SCVal> tx = buildAndSign(/* submitTimeout= */ 300);
    tx.sleeper = sleeps::add;

    try {
      tx.submit();
      fail("expected SorobanRpcException");
    } catch (SorobanRpcException e) {
      assertEquals(Integer.valueOf(-32601), e.getCode());
    }
    // The error is not retried.
    assertEquals(2, getTransactionCalls.get());
    assertEquals(Collections.singletonList(1_000L), sleeps);
  }

  /**
   * Replaces the transaction's clock and sleeper with a fake pair: the clock starts at zero and
   * only advances when the sleeper is called, by the requested duration plus {@code
   * oversleepMillis}. Every sleep request is recorded in {@link #sleeps}.
   */
  private void useFakeClock(AssembledTransaction<?> tx, long oversleepMillis) {
    AtomicLong nowNanos = new AtomicLong();
    tx.nanoClock = nowNanos::get;
    tx.sleeper =
        millis -> {
          sleeps.add(millis);
          nowNanos.addAndGet(TimeUnit.MILLISECONDS.toNanos(millis + oversleepMillis));
        };
  }

  private interface GetTransactionScript {
    MockResponse answer(int attempt);
  }

  private void startServer(GetTransactionScript script) {
    mockWebServer.setDispatcher(
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
            JsonObject request =
                JsonParser.parseString(recordedRequest.getBody().readUtf8()).getAsJsonObject();
            String method = request.get("method").getAsString();
            switch (method) {
              case "getLedgerEntries":
                return ok(getAccountJson);
              case "simulateTransaction":
                return ok(simulateJson);
              case "sendTransaction":
                return ok(sendJson);
              case "getTransaction":
                return script.answer(getTransactionCalls.incrementAndGet());
              default:
                return new MockResponse().setResponseCode(404);
            }
          }
        });
    try {
      mockWebServer.start();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    server = new SorobanServer(mockWebServer.url("").toString());
  }

  private AssembledTransaction<SCVal> buildAndSign(int submitTimeout) {
    // Same invocation as the simulate_transaction.json fixture was recorded for.
    KeyPair submitter =
        KeyPair.fromSecretSeed("SAAPYAPTTRZMCUZFPG3G66V4ZMHTK4TWA6NS7U4F7Z3IMUD52EK4DDEV");
    KeyPair invoker =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    String contractId = "CDU3PZ4LXVETIFVLS33RDXLD63JZ5GXS7PCV2DJ7BBT6EBPA2AB7YR5H";

    TransactionBuilder builder =
        new TransactionBuilder(new Account(submitter.getAccountId(), 0L), Network.STANDALONE)
            .setBaseFee(50_000)
            .addPreconditions(
                TransactionPreconditions.builder().timeBounds(new TimeBounds(0, 0)).build())
            .addOperation(
                InvokeHostFunctionOperation.invokeContractFunctionOperationBuilder(
                        contractId,
                        "increment",
                        Arrays.asList(
                            new Address(invoker.getAccountId()).toSCVal(), Scv.toUint32(10L)))
                    .sourceAccount(invoker.getAccountId())
                    .build());

    AssembledTransaction<SCVal> tx =
        new AssembledTransaction<>(builder, server, submitter, null, submitTimeout);
    return tx.simulate(false).sign(null, false);
  }

  private static MockResponse ok(String body) {
    return new MockResponse().setResponseCode(200).setBody(body);
  }

  private static String readFixture(String name) throws IOException {
    return new String(Files.readAllBytes(Paths.get(FIXTURES + name)), StandardCharsets.UTF_8);
  }
}
