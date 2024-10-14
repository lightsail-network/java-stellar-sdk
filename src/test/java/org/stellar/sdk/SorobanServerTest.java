package org.stellar.sdk;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.stellar.sdk.xdr.SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.stellar.sdk.exception.AccountNotFoundException;
import org.stellar.sdk.exception.PrepareTransactionException;
import org.stellar.sdk.exception.SorobanRpcException;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.operations.Operation;
import org.stellar.sdk.requests.sorobanrpc.EventFilterType;
import org.stellar.sdk.requests.sorobanrpc.GetEventsRequest;
import org.stellar.sdk.requests.sorobanrpc.GetLedgerEntriesRequest;
import org.stellar.sdk.requests.sorobanrpc.GetTransactionRequest;
import org.stellar.sdk.requests.sorobanrpc.GetTransactionsRequest;
import org.stellar.sdk.requests.sorobanrpc.SendTransactionRequest;
import org.stellar.sdk.requests.sorobanrpc.SimulateTransactionRequest;
import org.stellar.sdk.requests.sorobanrpc.SorobanRpcRequest;
import org.stellar.sdk.responses.sorobanrpc.GetEventsResponse;
import org.stellar.sdk.responses.sorobanrpc.GetFeeStatsResponse;
import org.stellar.sdk.responses.sorobanrpc.GetHealthResponse;
import org.stellar.sdk.responses.sorobanrpc.GetLatestLedgerResponse;
import org.stellar.sdk.responses.sorobanrpc.GetLedgerEntriesResponse;
import org.stellar.sdk.responses.sorobanrpc.GetNetworkResponse;
import org.stellar.sdk.responses.sorobanrpc.GetTransactionResponse;
import org.stellar.sdk.responses.sorobanrpc.GetTransactionsResponse;
import org.stellar.sdk.responses.sorobanrpc.GetVersionInfoResponse;
import org.stellar.sdk.responses.sorobanrpc.SendTransactionResponse;
import org.stellar.sdk.responses.sorobanrpc.SimulateTransactionResponse;
import org.stellar.sdk.xdr.ContractDataDurability;
import org.stellar.sdk.xdr.ContractExecutable;
import org.stellar.sdk.xdr.ContractExecutableType;
import org.stellar.sdk.xdr.ContractIDPreimage;
import org.stellar.sdk.xdr.ContractIDPreimageType;
import org.stellar.sdk.xdr.CreateContractArgs;
import org.stellar.sdk.xdr.ExtensionPoint;
import org.stellar.sdk.xdr.HostFunction;
import org.stellar.sdk.xdr.HostFunctionType;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.InvokeContractArgs;
import org.stellar.sdk.xdr.LedgerEntryType;
import org.stellar.sdk.xdr.LedgerFootprint;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.SCSymbol;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;
import org.stellar.sdk.xdr.SorobanAuthorizedFunction;
import org.stellar.sdk.xdr.SorobanAuthorizedFunctionType;
import org.stellar.sdk.xdr.SorobanAuthorizedInvocation;
import org.stellar.sdk.xdr.SorobanCredentials;
import org.stellar.sdk.xdr.SorobanCredentialsType;
import org.stellar.sdk.xdr.SorobanResources;
import org.stellar.sdk.xdr.SorobanTransactionData;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrString;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

public class SorobanServerTest {
  private final Gson gson = new Gson();

  @Test
  public void testGetAccount() throws IOException {
    String accountId = "GDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIG54";
    String filePath = "src/test/resources/soroban_server/get_account.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            GetLedgerEntriesRequest expectedRequest =
                new GetLedgerEntriesRequest(
                    singletonList("AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JA=="));
            SorobanRpcRequest<GetLedgerEntriesRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<GetLedgerEntriesRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getLedgerEntries")
                && sorobanRpcRequest.getParams().equals(expectedRequest)) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    TransactionBuilderAccount resp = server.getAccount(accountId);
    assertEquals(resp.getAccountId(), accountId);
    assertEquals(resp.getSequenceNumber().longValue(), 3418793967628L);
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testGetAccountMuxed() throws IOException {
    String accountId = "MDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIAAAAAAAAAPCIBOR2";
    String filePath = "src/test/resources/soroban_server/get_account_muxed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            GetLedgerEntriesRequest expectedRequest =
                new GetLedgerEntriesRequest(
                    singletonList("AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JA=="));
            SorobanRpcRequest<GetLedgerEntriesRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<GetLedgerEntriesRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getLedgerEntries")
                && sorobanRpcRequest.getParams().equals(expectedRequest)) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    TransactionBuilderAccount resp = server.getAccount(accountId);
    assertEquals(resp.getAccountId(), accountId);
    assertEquals(resp.getSequenceNumber().longValue(), 3418793967628L);
    server.close();
    mockWebServer.close();
  }

  @Test(expected = AccountNotFoundException.class)
  public void testGetAccountNotFoundThrows() throws IOException {
    String filePath = "src/test/resources/soroban_server/get_account_not_found.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    String accountId = "GBG6OSICP2YJ5ROY4HBGNSVRQDCQ4RYPFFUH6I6BI7LHYNW2CM7AJVBE";
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<GetLedgerEntriesRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<GetLedgerEntriesRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getLedgerEntries")) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    server.getAccount(accountId);
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testGetHealth() throws IOException, SorobanRpcException {
    String filePath = "src/test/resources/soroban_server/get_health.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<Void> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<Void>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getHealth")) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    GetHealthResponse resp = server.getHealth();
    assertEquals(resp.getStatus(), "healthy");
    assertEquals(resp.getLatestLedger().longValue(), 50000L);
    assertEquals(resp.getOldestLedger().longValue(), 1L);
    assertEquals(resp.getLedgerRetentionWindow().longValue(), 10000L);
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testGetFeeStats() throws IOException, SorobanRpcException {
    String filePath = "src/test/resources/soroban_server/get_fee_stats.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<Void> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<Void>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getFeeStats")) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    GetFeeStatsResponse resp = server.getFeeStats();
    assertEquals(resp.getLatestLedger().longValue(), 4519945L);
    assertEquals(
        resp.getInclusionFee(),
        new GetFeeStatsResponse.FeeDistribution(
            200L, 100L, 125L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 7L,
            10L));
    assertEquals(
        resp.getSorobanInclusionFee(),
        new GetFeeStatsResponse.FeeDistribution(
            210L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 100L, 120L, 190L, 200L, 10L,
            50L));
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testGetContractData() throws IOException, SorobanRpcException {
    String filePath = "src/test/resources/soroban_server/get_contract_data.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    String contractId = "CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K";
    SCVal key = SCVal.builder().discriminant(SCV_LEDGER_KEY_CONTRACT_INSTANCE).build();

    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {

            Address address = new Address(contractId);
            LedgerKey.LedgerKeyContractData ledgerKeyContractData =
                LedgerKey.LedgerKeyContractData.builder()
                    .contract(address.toSCAddress())
                    .key(key)
                    .durability(ContractDataDurability.PERSISTENT)
                    .build();
            LedgerKey ledgerKey =
                LedgerKey.builder()
                    .discriminant(LedgerEntryType.CONTRACT_DATA)
                    .contractData(ledgerKeyContractData)
                    .build();

            GetLedgerEntriesRequest expectedRequest = null;
            try {
              expectedRequest = new GetLedgerEntriesRequest(singletonList(ledgerKey.toXdrBase64()));
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
            SorobanRpcRequest<GetLedgerEntriesRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<GetLedgerEntriesRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getLedgerEntries")
                && sorobanRpcRequest.getParams().equals(expectedRequest)) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    Optional<GetLedgerEntriesResponse.LedgerEntryResult> resp =
        server.getContractData(contractId, key, SorobanServer.Durability.PERSISTENT);
    assertTrue(resp.isPresent());
    assertEquals(resp.get().getLastModifiedLedger().longValue(), 290L);
    assertEquals(resp.get().getLiveUntilLedger().longValue(), 490L);
    assertEquals(
        resp.get().getKey(),
        "AAAABgAAAAFgdoLyR3pr6M3w/fMr4T1fJaaGzAlP2T1ao9e2gjLQwAAAABQAAAABAAAAAA==");
    assertEquals(
        resp.get().getXdr(),
        "AAAABgAAAAFgdoLyR3pr6M3w/fMr4T1fJaaGzAlP2T1ao9e2gjLQwAAAABQAAAABAAAAAAAAAAAAAAATAAAAALnBupvoT7RHZ+oTeaPHSiSufpac3O3mc0u663Kqbko/AAAAAQAAAAEAAAAPAAAAB0NPVU5URVIAAAAAAwAAAAEAABD1");
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testGetContractDataReturnNull() throws IOException, SorobanRpcException {
    String filePath = "src/test/resources/soroban_server/get_contract_data_return_null.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    String contractId = "CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K";
    SCVal key = SCVal.builder().discriminant(SCV_LEDGER_KEY_CONTRACT_INSTANCE).build();

    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {

            Address address = new Address(contractId);
            LedgerKey.LedgerKeyContractData ledgerKeyContractData =
                LedgerKey.LedgerKeyContractData.builder()
                    .contract(address.toSCAddress())
                    .key(key)
                    .durability(ContractDataDurability.PERSISTENT)
                    .build();
            LedgerKey ledgerKey =
                LedgerKey.builder()
                    .discriminant(LedgerEntryType.CONTRACT_DATA)
                    .contractData(ledgerKeyContractData)
                    .build();

            GetLedgerEntriesRequest expectedRequest = null;
            try {
              expectedRequest = new GetLedgerEntriesRequest(singletonList(ledgerKey.toXdrBase64()));
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
            SorobanRpcRequest<GetLedgerEntriesRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<GetLedgerEntriesRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getLedgerEntries")
                && sorobanRpcRequest.getParams().equals(expectedRequest)) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    Optional<GetLedgerEntriesResponse.LedgerEntryResult> resp =
        server.getContractData(contractId, key, SorobanServer.Durability.PERSISTENT);
    assertFalse(resp.isPresent());
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testGetLedgerEntries() throws IOException, SorobanRpcException {
    String filePath = "src/test/resources/soroban_server/get_ledger_entries.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    String accountId0 = "GCZJ46F2ENQAEEZVVY4RKTGVXWZNETKZBZ4LIW5CMUF7AHP6M7BEV464";
    LedgerKey.LedgerKeyAccount ledgerKeyAccount0 =
        LedgerKey.LedgerKeyAccount.builder()
            .accountID(KeyPair.fromAccountId(accountId0).getXdrAccountId())
            .build();
    LedgerKey ledgerKey0 =
        LedgerKey.builder()
            .account(ledgerKeyAccount0)
            .discriminant(LedgerEntryType.ACCOUNT)
            .build();
    String ledgerKey0Xdr = ledgerKey0.toXdrBase64();

    String accountId1 = "GDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIG54";
    LedgerKey.LedgerKeyAccount ledgerKeyAccount1 =
        LedgerKey.LedgerKeyAccount.builder()
            .accountID(KeyPair.fromAccountId(accountId1).getXdrAccountId())
            .build();
    LedgerKey ledgerKey1 =
        LedgerKey.builder()
            .account(ledgerKeyAccount1)
            .discriminant(LedgerEntryType.ACCOUNT)
            .build();
    String ledgerKey1Xdr = ledgerKey1.toXdrBase64();

    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {

            // add ledgerKey0Xdr and ledgerKey1Xdr
            GetLedgerEntriesRequest expectedRequest =
                new GetLedgerEntriesRequest(Arrays.asList(ledgerKey0Xdr, ledgerKey1Xdr));
            SorobanRpcRequest<GetLedgerEntriesRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<GetLedgerEntriesRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getLedgerEntries")
                && sorobanRpcRequest.getParams().equals(expectedRequest)) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    GetLedgerEntriesResponse resp = server.getLedgerEntries(Arrays.asList(ledgerKey0, ledgerKey1));
    assertEquals(resp.getLatestLedger().longValue(), 7943L);
    assertEquals(resp.getEntries().size(), 2);
    assertEquals(
        resp.getEntries().get(0).getKey(),
        "AAAAAAAAAACynni6I2ACEzWuORVM1b2y0k1ZDni0W6JlC/Ad/mfCSg==");
    assertEquals(
        resp.getEntries().get(0).getXdr(),
        "AAAAAAAAAACynni6I2ACEzWuORVM1b2y0k1ZDni0W6JlC/Ad/mfCSgAAABdIdugAAAAAnwAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAA");
    assertEquals(
        resp.getEntries().get(1).getKey(),
        "AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JA==");
    assertEquals(
        resp.getEntries().get(1).getXdr(),
        "AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JAAAABdIcmH6AAAAoQAAAAgAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAADAAAAAAAAHAkAAAAAZMPQ0g==");
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testGetTransaction() throws IOException, SorobanRpcException {
    String filePath = "src/test/resources/soroban_server/get_transaction.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    String hash = "06dd9ee70bf93bbfe219e2b31363ab5a0361cc6285328592e4d3d1fed4c9025c";
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            GetTransactionRequest expectedRequest = new GetTransactionRequest(hash);
            SorobanRpcRequest<GetTransactionRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<GetTransactionRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getTransaction")
                && sorobanRpcRequest.getParams().equals(expectedRequest)) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    GetTransactionResponse tx = server.getTransaction(hash);
    assertEquals(tx.getStatus(), GetTransactionResponse.GetTransactionStatus.SUCCESS);
    assertEquals(
        tx.getTxHash(), "8faa3e6bb29d9d8469bbcabdbfd800f3be1899f4736a3a2fa83cd58617c072fe");

    server.close();
    mockWebServer.close();
  }

  @Test
  public void testGetTransactions() throws IOException, SorobanRpcException {
    String filePath = "src/test/resources/soroban_server/get_transactions.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    GetTransactionsRequest.PaginationOptions paginationOptions =
        GetTransactionsRequest.PaginationOptions.builder().limit(5L).build();
    GetTransactionsRequest getTransactionsRequest =
        GetTransactionsRequest.builder()
            .startLedger(1888539L)
            .pagination(paginationOptions)
            .build();

    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<GetTransactionsRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<GetTransactionsRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getTransactions")
                && sorobanRpcRequest.getParams().equals(getTransactionsRequest)) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    GetTransactionsResponse resp = server.getTransactions(getTransactionsRequest);
    assertEquals(resp.getLatestLedger().longValue(), 1888542L);
    assertEquals(resp.getLatestLedgerCloseTimestamp().longValue(), 1717166057L);
    assertEquals(resp.getOldestLedger().longValue(), 1871263L);
    assertEquals(resp.getOldestLedgerCloseTimestamp().longValue(), 1717075350L);
    assertEquals(resp.getCursor(), "8111217537191937");
    assertEquals(resp.getTransactions().size(), 5);
    assertEquals(
        resp.getTransactions().get(0).getStatus(),
        GetTransactionsResponse.TransactionStatus.FAILED);
    assertEquals(
        resp.getTransactions().get(0).getTxHash(),
        "171359fff0edbf0a9d9d11014d0407486ff9f6a6e8f7673f97244acccb355b2d");
    assertEquals(resp.getTransactions().get(0).getApplicationOrder().longValue(), 1L);
    assertEquals(resp.getTransactions().get(0).getFeeBump(), false);
    assertEquals(
        resp.getTransactions().get(0).getEnvelopeXdr(),
        resp.getTransactions().get(0).parseEnvelopeXdr().toXdrBase64());
    assertEquals(
        resp.getTransactions().get(0).getResultXdr(),
        resp.getTransactions().get(0).parseResultXdr().toXdrBase64());
    assertEquals(
        resp.getTransactions().get(0).getResultMetaXdr(),
        resp.getTransactions().get(0).parseResultMetaXdr().toXdrBase64());
    assertEquals(resp.getTransactions().get(0).getLedger().longValue(), 1888539L);
    assertEquals(resp.getTransactions().get(1).getCreatedAt().longValue(), 1717166042L);

    assertEquals(
        resp.getTransactions().get(3).getStatus(),
        GetTransactionsResponse.TransactionStatus.SUCCESS);
    assertEquals(
        resp.getTransactions().get(0).getTxHash(),
        "171359fff0edbf0a9d9d11014d0407486ff9f6a6e8f7673f97244acccb355b2d");
    assertEquals(resp.getTransactions().get(3).getApplicationOrder().longValue(), 4L);
    assertEquals(resp.getTransactions().get(3).getFeeBump(), false);
    assertEquals(
        resp.getTransactions().get(3).getEnvelopeXdr(),
        resp.getTransactions().get(3).parseEnvelopeXdr().toXdrBase64());
    assertEquals(
        resp.getTransactions().get(3).getResultXdr(),
        resp.getTransactions().get(3).parseResultXdr().toXdrBase64());
    assertEquals(
        resp.getTransactions().get(3).getResultMetaXdr(),
        resp.getTransactions().get(3).parseResultMetaXdr().toXdrBase64());
    assertEquals(resp.getTransactions().get(3).getLedger().longValue(), 1888539L);
    assertEquals(resp.getTransactions().get(3).getDiagnosticEventsXdr().size(), 19);
    for (int i = 0; i < 19; i++) {
      assertEquals(
          resp.getTransactions().get(3).getDiagnosticEventsXdr().get(i),
          resp.getTransactions().get(3).parseDiagnosticEventsXdr().get(i).toXdrBase64());
    }
    assertEquals(resp.getTransactions().get(0).getLedger().longValue(), 1888539L);
    assertEquals(resp.getTransactions().get(1).getCreatedAt().longValue(), 1717166042L);

    server.close();
    mockWebServer.close();
  }

  @Test
  public void testGetEvents() throws IOException, SorobanRpcException {
    String filePath = "src/test/resources/soroban_server/get_events.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    GetEventsRequest.EventFilter eventFilter =
        GetEventsRequest.EventFilter.builder()
            .contractIds(singletonList("CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K"))
            .type(EventFilterType.CONTRACT)
            .topic(Arrays.asList("AAAADwAAAAdDT1VOVEVSAA==", "AAAADwAAAAlpbmNyZW1lbnQAAAA="))
            .build();
    GetEventsRequest.PaginationOptions paginationOptions =
        GetEventsRequest.PaginationOptions.builder()
            .limit(10L)
            .cursor("0000007799660613632-0000000000")
            .build();
    GetEventsRequest getEventsRequest =
        GetEventsRequest.builder()
            .startLedger(100L)
            .filter(eventFilter)
            .pagination(paginationOptions)
            .build();

    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<GetEventsRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<GetEventsRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getEvents")
                && sorobanRpcRequest.getParams().equals(getEventsRequest)) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    GetEventsResponse resp = server.getEvents(getEventsRequest);
    assertEquals(resp.getLatestLedger().longValue(), 187L);
    assertEquals(resp.getCursor(), "0000000468151439360-0000000000");
    assertEquals(resp.getEvents().size(), 2);
    assertEquals(resp.getEvents().get(0).getType(), EventFilterType.CONTRACT);
    assertEquals(resp.getEvents().get(0).getLedger().longValue(), 107L);
    assertEquals(resp.getEvents().get(0).getLedgerClosedAt(), "2023-07-28T14:57:02Z");
    assertEquals(
        resp.getEvents().get(0).getContractId(),
        "CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K");
    assertEquals(resp.getEvents().get(0).getId(), "0000000459561504768-0000000000");
    assertEquals(resp.getEvents().get(0).getTopic().size(), 2);
    assertEquals(resp.getEvents().get(0).getTopic().get(0), "AAAADwAAAAdDT1VOVEVSAA==");
    assertEquals(resp.getEvents().get(0).getTopic().get(1), "AAAADwAAAAlpbmNyZW1lbnQAAAA=");
    assertEquals(resp.getEvents().get(0).getValue(), "AAAAAwAAAAQ=");
    assertEquals(resp.getEvents().get(0).getInSuccessfulContractCall(), true);
    assertEquals(
        resp.getEvents().get(0).getTransactionHash(),
        "db86e94aa98b7d38213c041ebbb727fbaabf0b7c435de594f36c2d51fc61926d");

    server.close();
    mockWebServer.close();
  }

  @Test
  public void testGetNetwork() throws IOException, SorobanRpcException {
    String filePath = "src/test/resources/soroban_server/get_network.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<Void> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<Void>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getNetwork")) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    GetNetworkResponse resp = server.getNetwork();
    assertEquals(resp.getFriendbotUrl(), "http://localhost:8000/friendbot");
    assertEquals(resp.getPassphrase(), "Standalone Network ; February 2017");
    assertEquals(resp.getProtocolVersion().longValue(), 20L);
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testGetVersionInfo() throws IOException, SorobanRpcException {
    String filePath = "src/test/resources/soroban_server/get_version_info.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<Void> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<Void>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getVersionInfo")) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    GetVersionInfoResponse resp = server.getVersionInfo();
    assertEquals(resp.getVersion(), "21.1.0");
    assertEquals(resp.getCommitHash(), "fcd2f0523f04279bae4502f3e3fa00ca627e6f6a");
    assertEquals(resp.getBuildTimestamp(), "2024-05-10T11:18:38");
    assertEquals(
        resp.getCaptiveCoreVersion(),
        "stellar-core 21.0.0.rc2 (c6f474133738ae5f6d11b07963ca841909210273)");
    assertEquals(resp.getProtocolVersion().intValue(), 21);
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testGetLatestLedger() throws IOException, SorobanRpcException {
    String filePath = "src/test/resources/soroban_server/get_latest_ledger.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<Void> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<Void>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getLatestLedger")) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    GetLatestLedgerResponse resp = server.getLatestLedger();
    assertEquals(resp.getId(), "e73d7654b72daa637f396669182c6072549736a9e3b6fcb8e685adb61f8c910a");
    assertEquals(resp.getProtocolVersion().intValue(), 20);
    assertEquals(resp.getSequence().intValue(), 24170);

    server.close();
    mockWebServer.close();
  }

  @Test
  public void testSimulateTransaction() throws IOException, SorobanRpcException {
    String filePath = "src/test/resources/soroban_server/simulate_transaction.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    Transaction transaction = buildSorobanTransaction(null, null);

    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<SimulateTransactionRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<SimulateTransactionRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("simulateTransaction")
                && sorobanRpcRequest
                    .getParams()
                    .getTransaction()
                    .equals(transaction.toEnvelopeXdrBase64())) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    SimulateTransactionResponse resp = server.simulateTransaction(transaction);
    assertEquals(resp.getLatestLedger().longValue(), 14245L);
    assertEquals(
        resp.getTransactionData(),
        "AAAAAAAAAAIAAAAGAAAAAem354u9STQWq5b3Ed1j9tOemvL7xV0NPwhn4gXg0AP8AAAAFAAAAAEAAAAH8dTe2OoI0BnhlDbH0fWvXmvprkBvBAgKIcL9busuuMEAAAABAAAABgAAAAHpt+eLvUk0FquW9xHdY/bTnpry+8VdDT8IZ+IF4NAD/AAAABAAAAABAAAAAgAAAA8AAAAHQ291bnRlcgAAAAASAAAAAAAAAABYt8SiyPKXqo89JHEoH9/M7K/kjlZjMT7BjhKnPsqYoQAAAAEAHifGAAAFlAAAAIgAAAAAAAAAAg==");
    assertEquals(resp.getEvents().size(), 2);
    assertEquals(
        resp.getEvents().get(0),
        "AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAg6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=");
    assertEquals(
        resp.getEvents().get(1),
        "AAAAAQAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=");
    assertEquals(resp.getMinResourceFee().longValue(), 58181L);
    assertEquals(resp.getResults().size(), 1);
    assertEquals(resp.getResults().get(0).getAuth().size(), 1);
    assertEquals(
        resp.getResults().get(0).getAuth().get(0),
        "AAAAAAAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA");
    assertEquals(resp.getResults().get(0).getXdr(), "AAAAAwAAABQ=");
    assertEquals(resp.getStateChanges().size(), 1);
    assertEquals(resp.getStateChanges().get(0).getType(), "created");
    assertEquals(
        resp.getStateChanges().get(0).getKey(),
        "AAAAAAAAAABuaCbVXZ2DlXWarV6UxwbW3GNJgpn3ASChIFp5bxSIWg==");
    assertNull(resp.getStateChanges().get(0).getBefore());
    assertEquals(
        resp.getStateChanges().get(0).getAfter(),
        "AAAAZAAAAAAAAAAAbmgm1V2dg5V1mq1elMcG1txjSYKZ9wEgoSBaeW8UiFoAAAAAAAAAZAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=");
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testSimulateTransactionWithResourceLeeway() throws IOException, SorobanRpcException {
    String filePath =
        "src/test/resources/soroban_server/simulate_transaction_with_resource_leeway.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    Transaction transaction = buildSorobanTransaction(null, null);
    BigInteger cpuInstructions = BigInteger.valueOf(20000L);
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<SimulateTransactionRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<SimulateTransactionRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("simulateTransaction")
                && sorobanRpcRequest
                    .getParams()
                    .getTransaction()
                    .equals(transaction.toEnvelopeXdrBase64())
                && sorobanRpcRequest
                    .getParams()
                    .getResourceConfig()
                    .getInstructionLeeway()
                    .equals(cpuInstructions)) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());

    SimulateTransactionRequest.ResourceConfig resourceConfig =
        new SimulateTransactionRequest.ResourceConfig(cpuInstructions);
    SimulateTransactionResponse resp = server.simulateTransaction(transaction, resourceConfig);
    assertEquals(resp.getLatestLedger().longValue(), 14245L);
    assertEquals(
        resp.getTransactionData(),
        "AAAAAAAAAAIAAAAGAAAAAem354u9STQWq5b3Ed1j9tOemvL7xV0NPwhn4gXg0AP8AAAAFAAAAAEAAAAH8dTe2OoI0BnhlDbH0fWvXmvprkBvBAgKIcL9busuuMEAAAABAAAABgAAAAHpt+eLvUk0FquW9xHdY/bTnpry+8VdDT8IZ+IF4NAD/AAAABAAAAABAAAAAgAAAA8AAAAHQ291bnRlcgAAAAASAAAAAAAAAABYt8SiyPKXqo89JHEoH9/M7K/kjlZjMT7BjhKnPsqYoQAAAAEAHifGAAAFlAAAAIgAAAAAAAAAAg==");
    assertEquals(resp.getEvents().size(), 2);
    assertEquals(
        resp.getEvents().get(0),
        "AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAg6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=");
    assertEquals(
        resp.getEvents().get(1),
        "AAAAAQAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=");
    assertEquals(resp.getMinResourceFee().longValue(), 58181L);
    assertEquals(resp.getResults().size(), 1);
    assertEquals(resp.getResults().get(0).getAuth().size(), 1);
    assertEquals(
        resp.getResults().get(0).getAuth().get(0),
        "AAAAAAAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA");
    assertEquals(resp.getResults().get(0).getXdr(), "AAAAAwAAABQ=");
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testPrepareTransaction()
      throws IOException, SorobanRpcException, PrepareTransactionException {
    String filePath =
        "src/test/resources/soroban_server/simulate_transaction_with_resource_leeway.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));

    Transaction transaction = buildSorobanTransaction(null, null);

    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<SimulateTransactionRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<SimulateTransactionRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("simulateTransaction")
                && sorobanRpcRequest
                    .getParams()
                    .getTransaction()
                    .equals(transaction.toEnvelopeXdrBase64())) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    Transaction newTx = server.prepareTransaction(transaction);

    SorobanTransactionData sorobanData =
        SorobanTransactionData.fromXdrBase64(
            "AAAAAAAAAAIAAAAGAAAAAem354u9STQWq5b3Ed1j9tOemvL7xV0NPwhn4gXg0AP8AAAAFAAAAAEAAAAH8dTe2OoI0BnhlDbH0fWvXmvprkBvBAgKIcL9busuuMEAAAABAAAABgAAAAHpt+eLvUk0FquW9xHdY/bTnpry+8VdDT8IZ+IF4NAD/AAAABAAAAABAAAAAgAAAA8AAAAHQ291bnRlcgAAAAASAAAAAAAAAABYt8SiyPKXqo89JHEoH9/M7K/kjlZjMT7BjhKnPsqYoQAAAAEAHifGAAAFlAAAAIgAAAAAAAAAAg==");
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.builder()
            .hostFunction(
                ((InvokeHostFunctionOperation) transaction.getOperations()[0]).getHostFunction())
            .sourceAccount(transaction.getOperations()[0].getSourceAccount())
            .auth(
                singletonList(
                    SorobanAuthorizationEntry.fromXdrBase64(
                        "AAAAAAAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA")))
            .build();
    Transaction expectedTx =
        new Transaction(
            transaction.getSourceAccount(),
            transaction.getFee() + 58181L,
            transaction.getSequenceNumber(),
            new Operation[] {operation},
            transaction.getMemo(),
            transaction.getPreconditions(),
            sorobanData,
            transaction.getNetwork());
    assertEquals(expectedTx, newTx);

    server.close();
    mockWebServer.close();
  }

  @Test
  public void testPrepareTransactionWithSorobanData()
      throws IOException, SorobanRpcException, PrepareTransactionException {
    String filePath =
        "src/test/resources/soroban_server/prepare_transaction_with_soroban_data.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    // soroban data will be overwritten
    LedgerKey ledgerKey =
        LedgerKey.builder()
            .discriminant(LedgerEntryType.ACCOUNT)
            .account(
                LedgerKey.LedgerKeyAccount.builder()
                    .accountID(
                        KeyPair.fromAccountId(
                                "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
                            .getXdrAccountId())
                    .build())
            .build();
    SorobanTransactionData originSorobanData =
        SorobanTransactionData.builder()
            .resources(
                SorobanResources.builder()
                    .footprint(
                        LedgerFootprint.builder()
                            .readOnly(new LedgerKey[] {ledgerKey})
                            .readWrite(new LedgerKey[] {})
                            .build())
                    .readBytes(new Uint32(new XdrUnsignedInteger(699)))
                    .writeBytes(new Uint32(new XdrUnsignedInteger(0)))
                    .instructions(new Uint32(new XdrUnsignedInteger(34567)))
                    .build())
            .resourceFee(new Int64(100L))
            .ext(ExtensionPoint.builder().discriminant(0).build())
            .build();
    Transaction transaction = buildSorobanTransaction(originSorobanData, null);

    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<SimulateTransactionRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<SimulateTransactionRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("simulateTransaction")
                && sorobanRpcRequest
                    .getParams()
                    .getTransaction()
                    .equals(transaction.toEnvelopeXdrBase64())) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    Transaction newTx = server.prepareTransaction(transaction);

    SorobanTransactionData sorobanData =
        SorobanTransactionData.fromXdrBase64(
            "AAAAAAAAAAEAAAAGAAAAAdeSi3LCcDzP6vfrn/TvTVBKVai5efybRQ6iyEK00c5hAAAAFAAAAAEAAAACAAAAAAAAAABPFZKkWLE8Tlrm5Jx81FUrXpm6EhpW/s8TXPUyf0D5PgAAAAAAAAAAbjEdZhNooxW4Z5oCpgPDCmGnVRwOxutuDO14EQ4kFmoAA3kUAAACGAAAASAAAAAAAAG4Sw==");
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.builder()
            .hostFunction(
                ((InvokeHostFunctionOperation) transaction.getOperations()[0]).getHostFunction())
            .sourceAccount(transaction.getOperations()[0].getSourceAccount())
            .auth(
                singletonList(
                    SorobanAuthorizationEntry.fromXdrBase64(
                        "AAAAAAAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA")))
            .build();
    Transaction expectedTx =
        new Transaction(
            transaction.getSourceAccount(),
            50000 + 12500, // baseFee + Soroban Fee
            transaction.getSequenceNumber(),
            new Operation[] {operation},
            transaction.getMemo(),
            transaction.getPreconditions(),
            sorobanData,
            transaction.getNetwork());
    assertEquals(expectedTx, newTx);

    server.close();
    mockWebServer.close();
  }

  @Test
  public void testPrepareTransactionWithAuth()
      throws IOException, SorobanRpcException, PrepareTransactionException {
    String filePath =
        "src/test/resources/soroban_server/simulate_transaction_with_resource_leeway.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    // origin auth will not be overwritten
    CreateContractArgs createContractArgs =
        CreateContractArgs.builder()
            .contractIDPreimage(
                ContractIDPreimage.builder()
                    .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ADDRESS)
                    .fromAddress(
                        ContractIDPreimage.ContractIDPreimageFromAddress.builder()
                            .address(
                                new Address(
                                        "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
                                    .toSCAddress())
                            .salt(new Uint256(new byte[32]))
                            .build())
                    .build())
            .executable(
                ContractExecutable.builder()
                    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_STELLAR_ASSET)
                    .build())
            .build();
    SorobanAuthorizationEntry auth =
        SorobanAuthorizationEntry.builder()
            .credentials(
                SorobanCredentials.builder()
                    .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_SOURCE_ACCOUNT)
                    .build())
            .rootInvocation(
                SorobanAuthorizedInvocation.builder()
                    .subInvocations(new SorobanAuthorizedInvocation[] {})
                    .function(
                        SorobanAuthorizedFunction.builder()
                            .discriminant(
                                SorobanAuthorizedFunctionType
                                    .SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_HOST_FN)
                            .createContractHostFn(createContractArgs)
                            .build())
                    .build())
            .build();

    Transaction transaction = buildSorobanTransaction(null, singletonList(auth));

    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<SimulateTransactionRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<SimulateTransactionRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("simulateTransaction")
                && sorobanRpcRequest
                    .getParams()
                    .getTransaction()
                    .equals(transaction.toEnvelopeXdrBase64())) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    Transaction newTx = server.prepareTransaction(transaction);

    SorobanTransactionData sorobanData =
        SorobanTransactionData.fromXdrBase64(
            "AAAAAAAAAAIAAAAGAAAAAem354u9STQWq5b3Ed1j9tOemvL7xV0NPwhn4gXg0AP8AAAAFAAAAAEAAAAH8dTe2OoI0BnhlDbH0fWvXmvprkBvBAgKIcL9busuuMEAAAABAAAABgAAAAHpt+eLvUk0FquW9xHdY/bTnpry+8VdDT8IZ+IF4NAD/AAAABAAAAABAAAAAgAAAA8AAAAHQ291bnRlcgAAAAASAAAAAAAAAABYt8SiyPKXqo89JHEoH9/M7K/kjlZjMT7BjhKnPsqYoQAAAAEAHifGAAAFlAAAAIgAAAAAAAAAAg==");
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.builder()
            .hostFunction(
                ((InvokeHostFunctionOperation) transaction.getOperations()[0]).getHostFunction())
            .sourceAccount(transaction.getOperations()[0].getSourceAccount())
            .auth(singletonList(auth))
            .build();
    Transaction expectedTx =
        new Transaction(
            transaction.getSourceAccount(),
            transaction.getFee() + 58181L,
            transaction.getSequenceNumber(),
            new Operation[] {operation},
            transaction.getMemo(),
            transaction.getPreconditions(),
            sorobanData,
            transaction.getNetwork());
    assertEquals(expectedTx, newTx);

    server.close();
    mockWebServer.close();
  }

  @Test(expected = PrepareTransactionException.class)
  public void testPrepareTransactionWithPrepareTransactionExceptionThrowsErrorResponse()
      throws IOException, SorobanRpcException, PrepareTransactionException {
    String filePath = "src/test/resources/soroban_server/prepare_transaction_error.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    Transaction transaction = buildSorobanTransaction(null, null);
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<SimulateTransactionRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<SimulateTransactionRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("simulateTransaction")
                && sorobanRpcRequest
                    .getParams()
                    .getTransaction()
                    .equals(transaction.toEnvelopeXdrBase64())) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    server.prepareTransaction(transaction);
    server.close();
    mockWebServer.close();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPrepareTransactionWithIllegalArgumentExceptionThrowsErrorResultsIsEmpty()
      throws IOException, SorobanRpcException, PrepareTransactionException {
    String filePath = "src/test/resources/soroban_server/prepare_transaction.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    Transaction transaction = buildSorobanTransaction(null, null);
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<SimulateTransactionRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<SimulateTransactionRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("simulateTransaction")
                && sorobanRpcRequest
                    .getParams()
                    .getTransaction()
                    .equals(transaction.toEnvelopeXdrBase64())) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    server.prepareTransaction(transaction);
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testSendTransaction()
      throws IOException, SorobanRpcException, PrepareTransactionException {
    String filePath = "src/test/resources/soroban_server/send_transaction.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    Transaction transaction = buildSorobanTransaction(null, null);
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<SendTransactionRequest> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<SendTransactionRequest>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("sendTransaction")
                && sorobanRpcRequest
                    .getParams()
                    .getTransaction()
                    .equals(transaction.toEnvelopeXdrBase64())) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    SendTransactionResponse response = server.sendTransaction(transaction);
    assertEquals(response.getStatus(), SendTransactionResponse.SendTransactionStatus.PENDING);
    assertEquals(response.getHash(), transaction.hashHex());
    assertEquals(response.getLatestLedger().longValue(), 1479L);
    assertEquals(response.getLatestLedgerCloseTime().longValue(), 1690594566L);

    server.close();
    mockWebServer.close();
  }

  @Test
  public void testSorobanRpcErrorResponseThrows() throws IOException {
    String filePath = "src/test/resources/soroban_server/soroban_rpc_error.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {
            SorobanRpcRequest<Void> sorobanRpcRequest =
                gson.fromJson(
                    recordedRequest.getBody().readUtf8(),
                    new TypeToken<SorobanRpcRequest<Void>>() {}.getType());
            if ("POST".equals(recordedRequest.getMethod())
                && sorobanRpcRequest.getMethod().equals("getNetwork")) {
              return new MockResponse().setResponseCode(200).setBody(json);
            }
            return new MockResponse().setResponseCode(404);
          }
        };
    mockWebServer.setDispatcher(dispatcher);
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());
    try {
      server.getNetwork();
      fail();
    } catch (SorobanRpcException e) {
      assertEquals(e.getCode().longValue(), -32601L);
      assertEquals(e.getMessage(), "method not found");
      assertEquals(e.getData(), "mockTest");
    }

    server.close();
    mockWebServer.close();
  }

  private Transaction buildSorobanTransaction(
      SorobanTransactionData sorobanData, List<SorobanAuthorizationEntry> auth) {
    String contractId = "CDU3PZ4LXVETIFVLS33RDXLD63JZ5GXS7PCV2DJ7BBT6EBPA2AB7YR5H";
    KeyPair txSubmitterKp =
        KeyPair.fromSecretSeed("SAAPYAPTTRZMCUZFPG3G66V4ZMHTK4TWA6NS7U4F7Z3IMUD52EK4DDEV");
    KeyPair opInvokerKp =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");

    TransactionBuilderAccount source = new Account(txSubmitterKp.getAccountId(), 3053721747476L);

    if (auth == null) {
      auth = new ArrayList<>();
    }

    InvokeContractArgs invokeContractArgs =
        InvokeContractArgs.builder()
            .contractAddress(new Address(contractId).toSCAddress())
            .functionName(new SCSymbol(new XdrString("increment")))
            .args(
                new SCVal[] {
                  new Address(opInvokerKp.getAccountId()).toSCVal(),
                  SCVal.builder()
                      .discriminant(SCValType.SCV_U32)
                      .u32(new Uint32(new XdrUnsignedInteger(10)))
                      .build()
                })
            .build();

    TransactionBuilder transactionBuilder =
        new TransactionBuilder(source, Network.STANDALONE)
            .setBaseFee(50000)
            .addPreconditions(
                TransactionPreconditions.builder().timeBounds(new TimeBounds(0, 0)).build())
            .addOperation(
                InvokeHostFunctionOperation.builder()
                    .sourceAccount(opInvokerKp.getAccountId())
                    .hostFunction(
                        HostFunction.builder()
                            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_INVOKE_CONTRACT)
                            .invokeContract(invokeContractArgs)
                            .build())
                    .auth(auth)
                    .build());

    if (sorobanData != null) {
      transactionBuilder.setSorobanData(sorobanData);
    }

    return transactionBuilder.build();
  }
}
