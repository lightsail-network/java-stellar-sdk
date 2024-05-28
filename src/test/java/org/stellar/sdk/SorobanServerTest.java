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
import org.stellar.sdk.requests.sorobanrpc.EventFilterType;
import org.stellar.sdk.requests.sorobanrpc.GetEventsRequest;
import org.stellar.sdk.requests.sorobanrpc.GetLedgerEntriesRequest;
import org.stellar.sdk.requests.sorobanrpc.GetTransactionRequest;
import org.stellar.sdk.requests.sorobanrpc.SendTransactionRequest;
import org.stellar.sdk.requests.sorobanrpc.SimulateTransactionRequest;
import org.stellar.sdk.requests.sorobanrpc.SorobanRpcErrorResponse;
import org.stellar.sdk.requests.sorobanrpc.SorobanRpcRequest;
import org.stellar.sdk.responses.sorobanrpc.GetEventsResponse;
import org.stellar.sdk.responses.sorobanrpc.GetHealthResponse;
import org.stellar.sdk.responses.sorobanrpc.GetLatestLedgerResponse;
import org.stellar.sdk.responses.sorobanrpc.GetLedgerEntriesResponse;
import org.stellar.sdk.responses.sorobanrpc.GetNetworkResponse;
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
  public void testGetAccount()
      throws IOException, SorobanRpcErrorResponse, AccountNotFoundException {
    String accountId = "GDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIG54";
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"ecb18f82ec12484190673502d0486b98\",\n"
            + "    \"result\": {\n"
            + "        \"entries\": [\n"
            + "            {\n"
            + "                \"key\": \"AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JA==\",\n"
            + "                \"xdr\": \"AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JAAAABdIcDhpAAADHAAAAAwAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAADAAAAAAABfI8AAAAAZMK3qQ==\",\n"
            + "                \"lastModifiedLedgerSeq\": \"97423\",\n"
            + "                \"liveUntilLedgerSeq\": \"97673\"\n"
            + "            }\n"
            + "        ],\n"
            + "        \"latestLedger\": \"108023\"\n"
            + "    }\n"
            + "}";

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
  public void testGetAccountNotFoundThrows()
      throws IOException, SorobanRpcErrorResponse, AccountNotFoundException {
    String accountId = "GBG6OSICP2YJ5ROY4HBGNSVRQDCQ4RYPFFUH6I6BI7LHYNW2CM7AJVBE";
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"0376b51e6a744dd2abb3b83be4c2e6dd\",\n"
            + "    \"result\": {\n"
            + "        \"entries\": null,\n"
            + "        \"latestLedger\": \"109048\"\n"
            + "    }\n"
            + "}";

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
  public void testGetHealth() throws IOException, SorobanRpcErrorResponse {
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
            + "    \"result\": {\n"
            + "        \"status\": \"healthy\",\n"
            + "        \"latestLedger\": 50000,\n"
            + "        \"oldestLedger\": 1,\n"
            + "        \"ledgerRetentionWindow\": 10000\n"
            + "    }\n"
            + "}";

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
  public void testGetContractData() throws IOException, SorobanRpcErrorResponse {
    String json =
        "{\n"
            + "  \"jsonrpc\": \"2.0\",\n"
            + "  \"id\": \"839c6c921d40456db5ba8a1c4e1a0e70\",\n"
            + "  \"result\": {\n"
            + "    \"entries\": [\n"
            + "      {\n"
            + "        \"key\": \"AAAABgAAAAFgdoLyR3pr6M3w/fMr4T1fJaaGzAlP2T1ao9e2gjLQwAAAABQAAAABAAAAAA==\",\n"
            + "        \"xdr\": \"AAAABgAAAAFgdoLyR3pr6M3w/fMr4T1fJaaGzAlP2T1ao9e2gjLQwAAAABQAAAABAAAAAAAAAAAAAAATAAAAALnBupvoT7RHZ+oTeaPHSiSufpac3O3mc0u663Kqbko/AAAAAQAAAAEAAAAPAAAAB0NPVU5URVIAAAAAAwAAAAEAABD1\",\n"
            + "        \"lastModifiedLedgerSeq\": \"290\",\n"
            + "        \"liveUntilLedgerSeq\": \"490\"\n"
            + "      }\n"
            + "    ],\n"
            + "    \"latestLedger\": \"296\"\n"
            + "  }\n"
            + "}";

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
  public void testGetContractDataReturnNull() throws IOException, SorobanRpcErrorResponse {
    String json =
        "{\n"
            + "  \"jsonrpc\": \"2.0\",\n"
            + "  \"id\": \"7d61ef6b1f974ba886b323f4266b4211\",\n"
            + "  \"result\": {\n"
            + "    \"entries\": null,\n"
            + "    \"latestLedger\": \"191\"\n"
            + "  }\n"
            + "}";
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
  public void testGetLedgerEntries() throws IOException, SorobanRpcErrorResponse {
    String json =
        "{\n"
            + "  \"jsonrpc\": \"2.0\",\n"
            + "  \"id\": \"0ce70038b1804b3c93ca7abc137f3061\",\n"
            + "  \"result\": {\n"
            + "    \"entries\": [\n"
            + "      {\n"
            + "        \"key\": \"AAAAAAAAAACynni6I2ACEzWuORVM1b2y0k1ZDni0W6JlC/Ad/mfCSg==\",\n"
            + "        \"xdr\": \"AAAAAAAAAACynni6I2ACEzWuORVM1b2y0k1ZDni0W6JlC/Ad/mfCSgAAABdIdugAAAAAnwAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAA\",\n"
            + "        \"lastModifiedLedgerSeq\": \"159\",\n"
            + "        \"liveUntilLedgerSeq\": \"499\"\n"
            + "      },\n"
            + "      {\n"
            + "        \"key\": \"AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JA==\",\n"
            + "        \"xdr\": \"AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JAAAABdIcmH6AAAAoQAAAAgAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAADAAAAAAAAHAkAAAAAZMPQ0g==\",\n"
            + "        \"lastModifiedLedgerSeq\": \"7177\",\n"
            + "        \"liveUntilLedgerSeq\": \"7288\"\n"
            + "      }\n"
            + "    ],\n"
            + "    \"latestLedger\": \"7943\"\n"
            + "  }\n"
            + "}";

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
  public void testGetTransaction() throws IOException, SorobanRpcErrorResponse {
    String hash = "06dd9ee70bf93bbfe219e2b31363ab5a0361cc6285328592e4d3d1fed4c9025c";
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
            + "    \"result\": {\n"
            + "        \"status\": \"SUCCESS\",\n"
            + "        \"latestLedger\": \"79289\",\n"
            + "        \"latestLedgerCloseTime\": \"1690387240\",\n"
            + "        \"oldestLedger\": \"77850\",\n"
            + "        \"oldestLedgerCloseTime\": \"1690379694\",\n"
            + "        \"applicationOrder\": 1,\n"
            + "        \"envelopeXdr\": \"AAAAAgAAAADTYKIzfa0ubKp7qjOcF+ZO8sjQutzo1iHuDh8esi9q+wABNjQAATW1AAAAAQAAAAAAAAAAAAAAAQAAAAAAAAAYAAAAAAAAAAIAAAASAAAAAb3H+V1yFoNDBpje4rchxeaR7/hRNS2CAT2Dh6A8z6xrAAAADwAAAARuYW1lAAAAAAAAAAEAAAAAAAAAAwAAAAYAAAABvcf5XXIWg0MGmN7ityHF5pHv+FE1LYIBPYOHoDzPrGsAAAAPAAAACE1FVEFEQVRBAAAAAQAAAAAAAAAGAAAAAb3H+V1yFoNDBpje4rchxeaR7/hRNS2CAT2Dh6A8z6xrAAAAFAAAAAEAAAAAAAAAB++FkDTZODW0rvolF6AuIZf4w7+GQd8RofaH8u2pM+UPAAAAAAAAAAAAUrutAAAiqAAAAAAAAADIAAAAAAAAACgAAAABsi9q+wAAAEDgHR/5rU+bsXD/oPUFodyEgXFNbDm5T2+M1GarZXy+d+tZ757PBL9ysK41F1hUYz3p5CA7Urlpe3fnNjYcu1EM\",\n"
            + "        \"resultXdr\": \"AAAAAAABNCwAAAAAAAAAAQAAAAAAAAAYAAAAAJhEDjNV0Jj46jrxCj87qJ6JaYKJN4c+p5tvapkLwrn8AAAAAA==\",\n"
            + "        \"resultMetaXdr\": \"AAAAAwAAAAAAAAACAAAAAwABNbYAAAAAAAAAANNgojN9rS5sqnuqM5wX5k7yyNC63OjWIe4OHx6yL2r7AAAAF0h1s9QAATW1AAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQABNbYAAAAAAAAAANNgojN9rS5sqnuqM5wX5k7yyNC63OjWIe4OHx6yL2r7AAAAF0h1s9QAATW1AAAAAQAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAAAAAAAAAAAAAAAAAAMAAAAAAAE1tgAAAABkwUMZAAAAAAAAAAEAAAAAAAAAAgAAAAMAATW2AAAAAAAAAADTYKIzfa0ubKp7qjOcF+ZO8sjQutzo1iHuDh8esi9q+wAAABdIdbPUAAE1tQAAAAEAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAADAAAAAAABNbYAAAAAZMFDGQAAAAAAAAABAAE1tgAAAAAAAAAA02CiM32tLmyqe6oznBfmTvLI0Lrc6NYh7g4fHrIvavsAAAAXSHWz/AABNbUAAAABAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAACAAAAAAAAAAAAAAAAAAAAAwAAAAAAATW2AAAAAGTBQxkAAAAAAAAAAQAAAAAAAAAAAAAADgAAAAZUb2tlbkEAAAAAAAIAAAABAAAAAAAAAAAAAAACAAAAAAAAAAMAAAAPAAAAB2ZuX2NhbGwAAAAADQAAACC9x/ldchaDQwaY3uK3IcXmke/4UTUtggE9g4egPM+sawAAAA8AAAAEbmFtZQAAAAEAAAABAAAAAAAAAAG9x/ldchaDQwaY3uK3IcXmke/4UTUtggE9g4egPM+sawAAAAIAAAAAAAAAAgAAAA8AAAAJZm5fcmV0dXJuAAAAAAAADwAAAARuYW1lAAAADgAAAAZUb2tlbkEAAA==\",\n"
            + "        \"ledger\": \"79286\",\n"
            + "        \"createdAt\": \"1690387225\"\n"
            + "    }\n"
            + "}";

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
    server.getTransaction(hash);

    server.close();
    mockWebServer.close();
  }

  @Test
  public void testGetEvents() throws IOException, SorobanRpcErrorResponse {
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
            + "    \"result\": {\n"
            + "        \"events\": [\n"
            + "            {\n"
            + "                \"type\": \"contract\",\n"
            + "                \"ledger\": \"107\",\n"
            + "                \"ledgerClosedAt\": \"2023-07-28T14:57:02Z\",\n"
            + "                \"contractId\": \"CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K\",\n"
            + "                \"id\": \"0000000459561504768-0000000000\",\n"
            + "                \"pagingToken\": \"0000000459561504768-0000000000\",\n"
            + "                \"topic\": [\n"
            + "                    \"AAAADwAAAAdDT1VOVEVSAA==\",\n"
            + "                    \"AAAADwAAAAlpbmNyZW1lbnQAAAA=\"\n"
            + "                ],\n"
            + "                \"value\": \"AAAAAwAAAAQ=\",\n"
            + "                \"inSuccessfulContractCall\": true,\n"
            + "                \"txHash\": \"db86e94aa98b7d38213c041ebbb727fbaabf0b7c435de594f36c2d51fc61926d\"\n"
            + "            },\n"
            + "            {\n"
            + "                \"type\": \"contract\",\n"
            + "                \"ledger\": \"109\",\n"
            + "                \"ledgerClosedAt\": \"2023-07-28T14:57:04Z\",\n"
            + "                \"contractId\": \"CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K\",\n"
            + "                \"id\": \"0000000468151439360-0000000000\",\n"
            + "                \"pagingToken\": \"0000000468151439360-0000000000\",\n"
            + "                \"topic\": [\n"
            + "                    \"AAAADwAAAAdDT1VOVEVSAA==\",\n"
            + "                    \"AAAADwAAAAlpbmNyZW1lbnQAAAA=\"\n"
            + "                ],\n"
            + "                \"value\": \"AAAAAwAAAAU=\",\n"
            + "                \"inSuccessfulContractCall\": true,\n"
            + "                \"txHash\": \"db86e94aa98b7d38213c041ebbb727fbaabf0b7c435de594f36c2d51fc61926d\"\n"
            + "            }\n"
            + "        ],\n"
            + "        \"latestLedger\": \"187\"\n"
            + "    }\n"
            + "}";

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
    assertEquals(resp.getEvents().size(), 2);
    assertEquals(resp.getEvents().get(0).getType(), EventFilterType.CONTRACT);
    assertEquals(resp.getEvents().get(0).getLedger().longValue(), 107L);
    assertEquals(resp.getEvents().get(0).getLedgerClosedAt(), "2023-07-28T14:57:02Z");
    assertEquals(
        resp.getEvents().get(0).getContractId(),
        "CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K");
    assertEquals(resp.getEvents().get(0).getId(), "0000000459561504768-0000000000");
    assertEquals(resp.getEvents().get(0).getPagingToken(), "0000000459561504768-0000000000");
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
  public void testGetNetwork() throws IOException, SorobanRpcErrorResponse {
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
            + "    \"result\": {\n"
            + "        \"friendbotUrl\": \"http://localhost:8000/friendbot\",\n"
            + "        \"passphrase\": \"Standalone Network ; February 2017\",\n"
            + "        \"protocolVersion\": \"20\"\n"
            + "    }\n"
            + "}";

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
  public void testGetLatestLedger() throws IOException, SorobanRpcErrorResponse {
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
            + "    \"result\": {\n"
            + "        \"id\": \"e73d7654b72daa637f396669182c6072549736a9e3b6fcb8e685adb61f8c910a\",\n"
            + "        \"protocolVersion\": \"20\",\n"
            + "        \"sequence\": 24170\n"
            + "    }\n"
            + "}";

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
  public void testSimulateTransaction() throws IOException, SorobanRpcErrorResponse {
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"7a469b9d6ed4444893491be530862ce3\",\n"
            + "    \"result\": {\n"
            + "      \"transactionData\": \"AAAAAAAAAAIAAAAGAAAAAem354u9STQWq5b3Ed1j9tOemvL7xV0NPwhn4gXg0AP8AAAAFAAAAAEAAAAH8dTe2OoI0BnhlDbH0fWvXmvprkBvBAgKIcL9busuuMEAAAABAAAABgAAAAHpt+eLvUk0FquW9xHdY/bTnpry+8VdDT8IZ+IF4NAD/AAAABAAAAABAAAAAgAAAA8AAAAHQ291bnRlcgAAAAASAAAAAAAAAABYt8SiyPKXqo89JHEoH9/M7K/kjlZjMT7BjhKnPsqYoQAAAAEAHifGAAAFlAAAAIgAAAAAAAAAAg==\",\n"
            + "      \"minResourceFee\": \"58181\",\n"
            + "      \"events\": [\n"
            + "        \"AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAg6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=\",\n"
            + "        \"AAAAAQAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=\"\n"
            + "      ],\n"
            + "      \"results\": [\n"
            + "        {\n"
            + "          \"auth\": [\n"
            + "            \"AAAAAAAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA\"\n"
            + "          ],\n"
            + "          \"xdr\": \"AAAAAwAAABQ=\"\n"
            + "        }\n"
            + "      ],\n"
            + "      \"cost\": { \"cpuInsns\": \"1646885\", \"memBytes\": \"1296481\" },\n"
            + "      \"stateChanges\": [\n"
            + "        {\n"
            + "            \"type\": \"created\",\n"
            + "            \"key\": \"AAAAAAAAAABuaCbVXZ2DlXWarV6UxwbW3GNJgpn3ASChIFp5bxSIWg==\",\n"
            + "            \"before\": null,\n"
            + "            \"after\": \"AAAAZAAAAAAAAAAAbmgm1V2dg5V1mq1elMcG1txjSYKZ9wEgoSBaeW8UiFoAAAAAAAAAZAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=\"\n"
            + "        }\n"
            + "      ],\n"
            + "      \"latestLedger\": \"14245\"\n"
            + "    }\n"
            + "  }";

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
    assertEquals(resp.getCost().getCpuInstructions().longValue(), 1646885L);
    assertEquals(resp.getCost().getMemoryBytes().longValue(), 1296481L);
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
  public void testSimulateTransactionWithResourceLeeway()
      throws IOException, SorobanRpcErrorResponse {
    String json =
        "{\n"
            + "  \"jsonrpc\": \"2.0\",\n"
            + "  \"id\": \"7a469b9d6ed4444893491be530862ce3\",\n"
            + "  \"result\": {\n"
            + "    \"transactionData\": \"AAAAAAAAAAIAAAAGAAAAAem354u9STQWq5b3Ed1j9tOemvL7xV0NPwhn4gXg0AP8AAAAFAAAAAEAAAAH8dTe2OoI0BnhlDbH0fWvXmvprkBvBAgKIcL9busuuMEAAAABAAAABgAAAAHpt+eLvUk0FquW9xHdY/bTnpry+8VdDT8IZ+IF4NAD/AAAABAAAAABAAAAAgAAAA8AAAAHQ291bnRlcgAAAAASAAAAAAAAAABYt8SiyPKXqo89JHEoH9/M7K/kjlZjMT7BjhKnPsqYoQAAAAEAHifGAAAFlAAAAIgAAAAAAAAAAg==\",\n"
            + "    \"minResourceFee\": \"58181\",\n"
            + "    \"events\": [\n"
            + "      \"AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAg6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=\",\n"
            + "      \"AAAAAQAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=\"\n"
            + "    ],\n"
            + "    \"results\": [\n"
            + "      {\n"
            + "        \"auth\": [\n"
            + "          \"AAAAAAAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA\"\n"
            + "        ],\n"
            + "        \"xdr\": \"AAAAAwAAABQ=\"\n"
            + "      }\n"
            + "    ],\n"
            + "    \"cost\": { \"cpuInsns\": \"1646885\", \"memBytes\": \"1296481\" },\n"
            + "    \"latestLedger\": \"14245\"\n"
            + "  }\n"
            + "}\n";

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
    assertEquals(resp.getCost().getCpuInstructions().longValue(), 1646885L);
    assertEquals(resp.getCost().getMemoryBytes().longValue(), 1296481L);
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testPrepareTransaction()
      throws IOException, SorobanRpcErrorResponse, PrepareTransactionException {
    String json =
        "{\n"
            + "  \"jsonrpc\": \"2.0\",\n"
            + "  \"id\": \"7a469b9d6ed4444893491be530862ce3\",\n"
            + "  \"result\": {\n"
            + "    \"transactionData\": \"AAAAAAAAAAIAAAAGAAAAAem354u9STQWq5b3Ed1j9tOemvL7xV0NPwhn4gXg0AP8AAAAFAAAAAEAAAAH8dTe2OoI0BnhlDbH0fWvXmvprkBvBAgKIcL9busuuMEAAAABAAAABgAAAAHpt+eLvUk0FquW9xHdY/bTnpry+8VdDT8IZ+IF4NAD/AAAABAAAAABAAAAAgAAAA8AAAAHQ291bnRlcgAAAAASAAAAAAAAAABYt8SiyPKXqo89JHEoH9/M7K/kjlZjMT7BjhKnPsqYoQAAAAEAHifGAAAFlAAAAIgAAAAAAAAAAg==\",\n"
            + "    \"minResourceFee\": \"58181\",\n"
            + "    \"events\": [\n"
            + "      \"AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAg6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=\",\n"
            + "      \"AAAAAQAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=\"\n"
            + "    ],\n"
            + "    \"results\": [\n"
            + "      {\n"
            + "        \"auth\": [\n"
            + "          \"AAAAAAAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA\"\n"
            + "        ],\n"
            + "        \"xdr\": \"AAAAAwAAABQ=\"\n"
            + "      }\n"
            + "    ],\n"
            + "    \"cost\": { \"cpuInsns\": \"1646885\", \"memBytes\": \"1296481\" },\n"
            + "    \"latestLedger\": \"14245\"\n"
            + "  }\n"
            + "}\n";

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
            transaction.getAccountConverter(),
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
      throws IOException, SorobanRpcErrorResponse, PrepareTransactionException {
    // soroban data will be overwritten
    String json =
        "{\n"
            + "  \"jsonrpc\": \"2.0\",\n"
            + "  \"id\": \"7a469b9d6ed4444893491be530862ce3\",\n"
            + "  \"result\": {\n"
            + "    \"transactionData\": \"AAAAAAAAAAIAAAAGAAAAAem354u9STQWq5b3Ed1j9tOemvL7xV0NPwhn4gXg0AP8AAAAFAAAAAEAAAAH8dTe2OoI0BnhlDbH0fWvXmvprkBvBAgKIcL9busuuMEAAAABAAAABgAAAAHpt+eLvUk0FquW9xHdY/bTnpry+8VdDT8IZ+IF4NAD/AAAABAAAAABAAAAAgAAAA8AAAAHQ291bnRlcgAAAAASAAAAAAAAAABYt8SiyPKXqo89JHEoH9/M7K/kjlZjMT7BjhKnPsqYoQAAAAEAHifGAAAFlAAAAIgAAAAAAAAAAg==\",\n"
            + "    \"minResourceFee\": \"58181\",\n"
            + "    \"events\": [\n"
            + "      \"AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAg6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=\",\n"
            + "      \"AAAAAQAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=\"\n"
            + "    ],\n"
            + "    \"results\": [\n"
            + "      {\n"
            + "        \"auth\": [\n"
            + "          \"AAAAAAAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA\"\n"
            + "        ],\n"
            + "        \"xdr\": \"AAAAAwAAABQ=\"\n"
            + "      }\n"
            + "    ],\n"
            + "    \"cost\": { \"cpuInsns\": \"1646885\", \"memBytes\": \"1296481\" },\n"
            + "    \"latestLedger\": \"14245\"\n"
            + "  }\n"
            + "}\n";
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
            transaction.getAccountConverter(),
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
  public void testPrepareTransactionWithAuth()
      throws IOException, SorobanRpcErrorResponse, PrepareTransactionException {
    // origin auth will not be overwritten
    String json =
        "{\n"
            + "  \"jsonrpc\": \"2.0\",\n"
            + "  \"id\": \"7a469b9d6ed4444893491be530862ce3\",\n"
            + "  \"result\": {\n"
            + "    \"transactionData\": \"AAAAAAAAAAIAAAAGAAAAAem354u9STQWq5b3Ed1j9tOemvL7xV0NPwhn4gXg0AP8AAAAFAAAAAEAAAAH8dTe2OoI0BnhlDbH0fWvXmvprkBvBAgKIcL9busuuMEAAAABAAAABgAAAAHpt+eLvUk0FquW9xHdY/bTnpry+8VdDT8IZ+IF4NAD/AAAABAAAAABAAAAAgAAAA8AAAAHQ291bnRlcgAAAAASAAAAAAAAAABYt8SiyPKXqo89JHEoH9/M7K/kjlZjMT7BjhKnPsqYoQAAAAEAHifGAAAFlAAAAIgAAAAAAAAAAg==\",\n"
            + "    \"minResourceFee\": \"58181\",\n"
            + "    \"events\": [\n"
            + "      \"AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAg6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=\",\n"
            + "      \"AAAAAQAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=\"\n"
            + "    ],\n"
            + "    \"results\": [\n"
            + "      {\n"
            + "        \"auth\": [\n"
            + "          \"AAAAAAAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA\"\n"
            + "        ],\n"
            + "        \"xdr\": \"AAAAAwAAABQ=\"\n"
            + "      }\n"
            + "    ],\n"
            + "    \"cost\": { \"cpuInsns\": \"1646885\", \"memBytes\": \"1296481\" },\n"
            + "    \"latestLedger\": \"14245\"\n"
            + "  }\n"
            + "}\n";
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
            transaction.getAccountConverter(),
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
      throws IOException, SorobanRpcErrorResponse, PrepareTransactionException {
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"7b6ada2bdec04ee28147d1557aadc3cf\",\n"
            + "    \"result\": {\n"
            + "        \"error\": \"HostError: Error(WasmVm, MissingValue)\\n\\nEvent log (newest first):\\n   0: [Diagnostic Event] contract:CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K, topics:[error, Error(WasmVm, MissingValue)], data:[\\\"invoking unknown export\\\", increment]\\n   1: [Diagnostic Event] topics:[fn_call, Bytes(CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K), increment], data:[Address(Account(58b7c4a2c8f297aa8f3d2471281fdfccecafe48e5663313ec18e12a73eca98a1)), 10]\\n\\nBacktrace (newest first):\\n   0: soroban_env_host::vm::Vm::invoke_function_raw\\n   1: soroban_env_host::host::frame::<impl soroban_env_host::host::Host>::call_n_internal\\n   2: soroban_env_host::host::frame::<impl soroban_env_host::host::Host>::invoke_function\\n   3: preflight::preflight_invoke_hf_op::{{closure}}\\n   4: preflight::catch_preflight_panic\\n   5: _cgo_a3255893d7fd_Cfunc_preflight_invoke_hf_op\\n             at /tmp/go-build/cgo-gcc-prolog:99:11\\n   6: runtime.asmcgocall\\n             at ./runtime/asm_amd64.s:848\\n\\n\",\n"
            + "        \"transactionData\": null,\n"
            + "        \"events\": null,\n"
            + "        \"minResourceFee\": \"0\",\n"
            + "        \"cost\": {\n"
            + "            \"cpuInsns\": \"0\",\n"
            + "            \"memBytes\": \"0\"\n"
            + "        },\n"
            + "        \"latestLedger\": \"898\"\n"
            + "    }\n"
            + "}";

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
      throws IOException, SorobanRpcErrorResponse, PrepareTransactionException {
    String json =
        "{\n"
            + "  \"jsonrpc\": \"2.0\",\n"
            + "  \"id\": \"7a469b9d6ed4444893491be530862ce3\",\n"
            + "  \"result\": {\n"
            + "    \"transactionData\": \"AAAAAAAAAAIAAAAGAAAAAem354u9STQWq5b3Ed1j9tOemvL7xV0NPwhn4gXg0AP8AAAAFAAAAAEAAAAH8dTe2OoI0BnhlDbH0fWvXmvprkBvBAgKIcL9busuuMEAAAABAAAABgAAAAHpt+eLvUk0FquW9xHdY/bTnpry+8VdDT8IZ+IF4NAD/AAAABAAAAABAAAAAgAAAA8AAAAHQ291bnRlcgAAAAASAAAAAAAAAABYt8SiyPKXqo89JHEoH9/M7K/kjlZjMT7BjhKnPsqYoQAAAAEAHifGAAAFlAAAAIgAAAAAAAAAAg==\",\n"
            + "    \"minResourceFee\": \"58181\",\n"
            + "    \"events\": [\n"
            + "      \"AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAg6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=\",\n"
            + "      \"AAAAAQAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=\"\n"
            + "    ],\n"
            + "    \"results\": [],\n"
            + "    \"cost\": { \"cpuInsns\": \"1646885\", \"memBytes\": \"1296481\" },\n"
            + "    \"latestLedger\": \"14245\"\n"
            + "  }\n"
            + "}";

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
      throws IOException, SorobanRpcErrorResponse, PrepareTransactionException {
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"688dfcf3bcd04f52af4866e98dffe387\",\n"
            + "    \"result\": {\n"
            + "        \"status\": \"PENDING\",\n"
            + "        \"hash\": \"64977cc4bb7f8bf75bdc47570548a994667899d3319b72f95cb2a64e567ad52c\",\n"
            + "        \"latestLedger\": \"1479\",\n"
            + "        \"latestLedgerCloseTime\": \"1690594566\"\n"
            + "    }\n"
            + "}";

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
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
            + "    \"error\": {\n"
            + "        \"code\": -32601,\n"
            + "        \"message\": \"method not found\",\n"
            + "        \"data\": \"mockTest\"\n"
            + "    }\n"
            + "}";

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
    } catch (SorobanRpcErrorResponse e) {
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
        new TransactionBuilder(AccountConverter.enableMuxed(), source, Network.STANDALONE)
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
