package org.stellar.sdk;

import static com.google.common.collect.ImmutableList.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.stellar.sdk.xdr.SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import org.stellar.sdk.xdr.ContractEntryBodyType;
import org.stellar.sdk.xdr.ContractExecutable;
import org.stellar.sdk.xdr.ContractExecutableType;
import org.stellar.sdk.xdr.ContractIDPreimage;
import org.stellar.sdk.xdr.ContractIDPreimageType;
import org.stellar.sdk.xdr.CreateContractArgs;
import org.stellar.sdk.xdr.ExtensionPoint;
import org.stellar.sdk.xdr.HostFunction;
import org.stellar.sdk.xdr.HostFunctionType;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.LedgerEntryType;
import org.stellar.sdk.xdr.LedgerFootprint;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.SCSymbol;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.SCVec;
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
import org.stellar.sdk.xdr.XdrDataInputStream;
import org.stellar.sdk.xdr.XdrDataOutputStream;
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
            + "                \"lastModifiedLedgerSeq\": \"97423\"\n"
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
                    of("AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JA=="));
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
            + "        \"status\": \"healthy\"\n"
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
            + "        \"lastModifiedLedgerSeq\": \"290\"\n"
            + "      }\n"
            + "    ],\n"
            + "    \"latestLedger\": \"296\"\n"
            + "  }\n"
            + "}";

    String contractId = "CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K";
    SCVal key = new SCVal.Builder().discriminant(SCV_LEDGER_KEY_CONTRACT_INSTANCE).build();

    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {

            Address address = new Address(contractId);
            LedgerKey.LedgerKeyContractData ledgerKeyContractData =
                new LedgerKey.LedgerKeyContractData.Builder()
                    .contract(address.toSCAddress())
                    .key(key)
                    .durability(ContractDataDurability.PERSISTENT)
                    .bodyType(ContractEntryBodyType.DATA_ENTRY)
                    .build();
            LedgerKey ledgerKey =
                new LedgerKey.Builder()
                    .discriminant(LedgerEntryType.CONTRACT_DATA)
                    .contractData(ledgerKeyContractData)
                    .build();

            GetLedgerEntriesRequest expectedRequest =
                new GetLedgerEntriesRequest(of(ledgerKeyToXdrBase64(ledgerKey)));
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
    SCVal key = new SCVal.Builder().discriminant(SCV_LEDGER_KEY_CONTRACT_INSTANCE).build();

    MockWebServer mockWebServer = new MockWebServer();
    Dispatcher dispatcher =
        new Dispatcher() {
          @NotNull
          @Override
          public MockResponse dispatch(@NotNull RecordedRequest recordedRequest)
              throws InterruptedException {

            Address address = new Address(contractId);
            LedgerKey.LedgerKeyContractData ledgerKeyContractData =
                new LedgerKey.LedgerKeyContractData.Builder()
                    .contract(address.toSCAddress())
                    .key(key)
                    .durability(ContractDataDurability.PERSISTENT)
                    .bodyType(ContractEntryBodyType.DATA_ENTRY)
                    .build();
            LedgerKey ledgerKey =
                new LedgerKey.Builder()
                    .discriminant(LedgerEntryType.CONTRACT_DATA)
                    .contractData(ledgerKeyContractData)
                    .build();

            GetLedgerEntriesRequest expectedRequest =
                new GetLedgerEntriesRequest(of(ledgerKeyToXdrBase64(ledgerKey)));
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
            + "        \"lastModifiedLedgerSeq\": \"159\"\n"
            + "      },\n"
            + "      {\n"
            + "        \"key\": \"AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JA==\",\n"
            + "        \"xdr\": \"AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JAAAABdIcmH6AAAAoQAAAAgAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAADAAAAAAAAHAkAAAAAZMPQ0g==\",\n"
            + "        \"lastModifiedLedgerSeq\": \"7177\"\n"
            + "      }\n"
            + "    ],\n"
            + "    \"latestLedger\": \"7943\"\n"
            + "  }\n"
            + "}";

    String accountId0 = "GCZJ46F2ENQAEEZVVY4RKTGVXWZNETKZBZ4LIW5CMUF7AHP6M7BEV464";
    LedgerKey.LedgerKeyAccount ledgerKeyAccount0 =
        new LedgerKey.LedgerKeyAccount.Builder()
            .accountID(KeyPair.fromAccountId(accountId0).getXdrAccountId())
            .build();
    LedgerKey ledgerKey0 =
        new LedgerKey.Builder()
            .account(ledgerKeyAccount0)
            .discriminant(LedgerEntryType.ACCOUNT)
            .build();
    String ledgerKey0Xdr = ledgerKeyToXdrBase64(ledgerKey0);

    String accountId1 = "GDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIG54";
    LedgerKey.LedgerKeyAccount ledgerKeyAccount1 =
        new LedgerKey.LedgerKeyAccount.Builder()
            .accountID(KeyPair.fromAccountId(accountId1).getXdrAccountId())
            .build();
    LedgerKey ledgerKey1 =
        new LedgerKey.Builder()
            .account(ledgerKeyAccount1)
            .discriminant(LedgerEntryType.ACCOUNT)
            .build();
    String ledgerKey1Xdr = ledgerKeyToXdrBase64(ledgerKey1);

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
        resp.getEntries().asList().get(0).getKey(),
        "AAAAAAAAAACynni6I2ACEzWuORVM1b2y0k1ZDni0W6JlC/Ad/mfCSg==");
    assertEquals(
        resp.getEntries().asList().get(0).getXdr(),
        "AAAAAAAAAACynni6I2ACEzWuORVM1b2y0k1ZDni0W6JlC/Ad/mfCSgAAABdIdugAAAAAnwAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAA");
    assertEquals(
        resp.getEntries().asList().get(1).getKey(),
        "AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JA==");
    assertEquals(
        resp.getEntries().asList().get(1).getXdr(),
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
            + "                \"contractId\": \"607682f2477a6be8cdf0fdf32be13d5f25a686cc094fd93d5aa3d7b68232d0c0\",\n"
            + "                \"id\": \"0000000459561504768-0000000000\",\n"
            + "                \"pagingToken\": \"0000000459561504768-0000000000\",\n"
            + "                \"topic\": [\n"
            + "                    \"AAAADwAAAAdDT1VOVEVSAA==\",\n"
            + "                    \"AAAADwAAAAlpbmNyZW1lbnQAAAA=\"\n"
            + "                ],\n"
            + "                \"value\": {\n"
            + "                    \"xdr\": \"AAAAAwAAAAQ=\"\n"
            + "                },\n"
            + "                \"inSuccessfulContractCall\": true\n"
            + "            },\n"
            + "            {\n"
            + "                \"type\": \"contract\",\n"
            + "                \"ledger\": \"109\",\n"
            + "                \"ledgerClosedAt\": \"2023-07-28T14:57:04Z\",\n"
            + "                \"contractId\": \"607682f2477a6be8cdf0fdf32be13d5f25a686cc094fd93d5aa3d7b68232d0c0\",\n"
            + "                \"id\": \"0000000468151439360-0000000000\",\n"
            + "                \"pagingToken\": \"0000000468151439360-0000000000\",\n"
            + "                \"topic\": [\n"
            + "                    \"AAAADwAAAAdDT1VOVEVSAA==\",\n"
            + "                    \"AAAADwAAAAlpbmNyZW1lbnQAAAA=\"\n"
            + "                ],\n"
            + "                \"value\": {\n"
            + "                    \"xdr\": \"AAAAAwAAAAU=\"\n"
            + "                },\n"
            + "                \"inSuccessfulContractCall\": true\n"
            + "            }\n"
            + "        ],\n"
            + "        \"latestLedger\": \"187\"\n"
            + "    }\n"
            + "}";

    GetEventsRequest.EventFilter eventFilter =
        GetEventsRequest.EventFilter.builder()
            .contractIds(of("607682f2477a6be8cdf0fdf32be13d5f25a686cc094fd93d5aa3d7b68232d0c0"))
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
            .startLedger("100")
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
        "607682f2477a6be8cdf0fdf32be13d5f25a686cc094fd93d5aa3d7b68232d0c0");
    assertEquals(resp.getEvents().get(0).getId(), "0000000459561504768-0000000000");
    assertEquals(resp.getEvents().get(0).getPagingToken(), "0000000459561504768-0000000000");
    assertEquals(resp.getEvents().get(0).getTopic().size(), 2);
    assertEquals(resp.getEvents().get(0).getTopic().get(0), "AAAADwAAAAdDT1VOVEVSAA==");
    assertEquals(resp.getEvents().get(0).getTopic().get(1), "AAAADwAAAAlpbmNyZW1lbnQAAAA=");
    assertEquals(resp.getEvents().get(0).getValue().getXdr(), "AAAAAwAAAAQ=");
    assertEquals(resp.getEvents().get(0).getInSuccessfulContractCall(), true);

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
            + "    \"id\": \"e1fabdcdf0244a2a9adfab94d7748b6c\",\n"
            + "    \"result\": {\n"
            + "        \"transactionData\": \"AAAAAAAAAAIAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAFAAAAAEAAAAAAAAAB300Hyg0HZG+Qie3zvsxLvugrNtFqd3AIntWy9bg2YvZAAAAAAAAAAEAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAEAAAAAEAAAACAAAADwAAAAdDb3VudGVyAAAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAQAAAAAAFcLDAAAF8AAAAQgAAAMcAAAAAAAAAJw=\",\n"
            + "        \"events\": [\n"
            + "            \"AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAgxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=\",\n"
            + "            \"AAAAAQAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=\"\n"
            + "        ],\n"
            + "        \"minResourceFee\": \"58595\",\n"
            + "        \"results\": [\n"
            + "            {\n"
            + "                \"auth\": [\n"
            + "                    \"AAAAAAAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA\"\n"
            + "                ],\n"
            + "                \"xdr\": \"AAAAAwAAABQ=\"\n"
            + "            }\n"
            + "        ],\n"
            + "        \"cost\": {\n"
            + "            \"cpuInsns\": \"1240100\",\n"
            + "            \"memBytes\": \"161637\"\n"
            + "        },\n"
            + "        \"latestLedger\": \"1479\"\n"
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
    SimulateTransactionResponse resp = server.simulateTransaction(transaction);
    assertEquals(resp.getLatestLedger().longValue(), 1479L);
    assertEquals(
        resp.getTransactionData(),
        "AAAAAAAAAAIAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAFAAAAAEAAAAAAAAAB300Hyg0HZG+Qie3zvsxLvugrNtFqd3AIntWy9bg2YvZAAAAAAAAAAEAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAEAAAAAEAAAACAAAADwAAAAdDb3VudGVyAAAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAQAAAAAAFcLDAAAF8AAAAQgAAAMcAAAAAAAAAJw=");
    assertEquals(resp.getEvents().size(), 2);
    assertEquals(
        resp.getEvents().get(0),
        "AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAgxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=");
    assertEquals(
        resp.getEvents().get(1),
        "AAAAAQAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=");
    assertEquals(resp.getMinResourceFee().longValue(), 58595L);
    assertEquals(resp.getResults().size(), 1);
    assertEquals(resp.getResults().get(0).getAuth().size(), 1);
    assertEquals(
        resp.getResults().get(0).getAuth().get(0),
        "AAAAAAAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA");
    assertEquals(resp.getResults().get(0).getXdr(), "AAAAAwAAABQ=");
    assertEquals(resp.getCost().getCpuInstructions().longValue(), 1240100L);
    assertEquals(resp.getCost().getMemoryBytes().longValue(), 161637L);
    server.close();
    mockWebServer.close();
  }

  @Test
  public void testPrepareTransaction()
      throws IOException, SorobanRpcErrorResponse, PrepareTransactionException {
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"e1fabdcdf0244a2a9adfab94d7748b6c\",\n"
            + "    \"result\": {\n"
            + "        \"transactionData\": \"AAAAAAAAAAIAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAFAAAAAEAAAAAAAAAB300Hyg0HZG+Qie3zvsxLvugrNtFqd3AIntWy9bg2YvZAAAAAAAAAAEAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAEAAAAAEAAAACAAAADwAAAAdDb3VudGVyAAAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAQAAAAAAFcLDAAAF8AAAAQgAAAMcAAAAAAAAAJw=\",\n"
            + "        \"events\": [\n"
            + "            \"AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAgxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=\",\n"
            + "            \"AAAAAQAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=\"\n"
            + "        ],\n"
            + "        \"minResourceFee\": \"58595\",\n"
            + "        \"results\": [\n"
            + "            {\n"
            + "                \"auth\": [\n"
            + "                    \"AAAAAAAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA\"\n"
            + "                ],\n"
            + "                \"xdr\": \"AAAAAwAAABQ=\"\n"
            + "            }\n"
            + "        ],\n"
            + "        \"cost\": {\n"
            + "            \"cpuInsns\": \"1240100\",\n"
            + "            \"memBytes\": \"161637\"\n"
            + "        },\n"
            + "        \"latestLedger\": \"1479\"\n"
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
    Transaction newTx = server.prepareTransaction(transaction);

    SorobanTransactionData sorobanData =
        SorobanTransactionData.fromXdrBase64(
            "AAAAAAAAAAIAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAFAAAAAEAAAAAAAAAB300Hyg0HZG+Qie3zvsxLvugrNtFqd3AIntWy9bg2YvZAAAAAAAAAAEAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAEAAAAAEAAAACAAAADwAAAAdDb3VudGVyAAAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAQAAAAAAFcLDAAAF8AAAAQgAAAMcAAAAAAAAAJw=");
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.builder()
            .hostFunction(
                ((InvokeHostFunctionOperation) transaction.getOperations()[0]).getHostFunction())
            .sourceAccount(transaction.getOperations()[0].getSourceAccount())
            .auth(
                of(
                    sorobanAuthorizationEntryFromXdrBase64(
                        "AAAAAAAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA")))
            .build();
    Transaction expectedTx =
        new Transaction(
            transaction.getAccountConverter(),
            transaction.getSourceAccount(),
            transaction.getFee() + 58595L,
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
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"e1fabdcdf0244a2a9adfab94d7748b6c\",\n"
            + "    \"result\": {\n"
            + "        \"transactionData\": \"AAAAAAAAAAIAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAFAAAAAEAAAAAAAAAB300Hyg0HZG+Qie3zvsxLvugrNtFqd3AIntWy9bg2YvZAAAAAAAAAAEAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAEAAAAAEAAAACAAAADwAAAAdDb3VudGVyAAAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAQAAAAAAFcLDAAAF8AAAAQgAAAMcAAAAAAAAAJw=\",\n"
            + "        \"events\": [\n"
            + "            \"AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAgxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=\",\n"
            + "            \"AAAAAQAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=\"\n"
            + "        ],\n"
            + "        \"minResourceFee\": \"58595\",\n"
            + "        \"results\": [\n"
            + "            {\n"
            + "                \"auth\": [\n"
            + "                    \"AAAAAAAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA\"\n"
            + "                ],\n"
            + "                \"xdr\": \"AAAAAwAAABQ=\"\n"
            + "            }\n"
            + "        ],\n"
            + "        \"cost\": {\n"
            + "            \"cpuInsns\": \"1240100\",\n"
            + "            \"memBytes\": \"161637\"\n"
            + "        },\n"
            + "        \"latestLedger\": \"1479\"\n"
            + "    }\n"
            + "}";
    LedgerKey ledgerKey =
        new LedgerKey.Builder()
            .discriminant(LedgerEntryType.ACCOUNT)
            .account(
                new LedgerKey.LedgerKeyAccount.Builder()
                    .accountID(
                        KeyPair.fromAccountId(
                                "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
                            .getXdrAccountId())
                    .build())
            .build();
    SorobanTransactionData originSorobanData =
        new SorobanTransactionData.Builder()
            .resources(
                new SorobanResources.Builder()
                    .footprint(
                        new LedgerFootprint.Builder()
                            .readOnly(new LedgerKey[] {ledgerKey})
                            .readWrite(new LedgerKey[] {})
                            .build())
                    .extendedMetaDataSizeBytes(new Uint32(new XdrUnsignedInteger(216)))
                    .readBytes(new Uint32(new XdrUnsignedInteger(699)))
                    .writeBytes(new Uint32(new XdrUnsignedInteger(0)))
                    .instructions(new Uint32(new XdrUnsignedInteger(34567)))
                    .build())
            .refundableFee(new Int64(100L))
            .ext(new ExtensionPoint.Builder().discriminant(0).build())
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
            "AAAAAAAAAAIAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAFAAAAAEAAAAAAAAAB300Hyg0HZG+Qie3zvsxLvugrNtFqd3AIntWy9bg2YvZAAAAAAAAAAEAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAEAAAAAEAAAACAAAADwAAAAdDb3VudGVyAAAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAQAAAAAAFcLDAAAF8AAAAQgAAAMcAAAAAAAAAJw=");
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.builder()
            .hostFunction(
                ((InvokeHostFunctionOperation) transaction.getOperations()[0]).getHostFunction())
            .sourceAccount(transaction.getOperations()[0].getSourceAccount())
            .auth(
                of(
                    sorobanAuthorizationEntryFromXdrBase64(
                        "AAAAAAAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA")))
            .build();
    Transaction expectedTx =
        new Transaction(
            transaction.getAccountConverter(),
            transaction.getSourceAccount(),
            transaction.getFee() + 58595L,
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
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"e1fabdcdf0244a2a9adfab94d7748b6c\",\n"
            + "    \"result\": {\n"
            + "        \"transactionData\": \"AAAAAAAAAAIAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAFAAAAAEAAAAAAAAAB300Hyg0HZG+Qie3zvsxLvugrNtFqd3AIntWy9bg2YvZAAAAAAAAAAEAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAEAAAAAEAAAACAAAADwAAAAdDb3VudGVyAAAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAQAAAAAAFcLDAAAF8AAAAQgAAAMcAAAAAAAAAJw=\",\n"
            + "        \"events\": [\n"
            + "            \"AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAgxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=\",\n"
            + "            \"AAAAAQAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=\"\n"
            + "        ],\n"
            + "        \"minResourceFee\": \"58595\",\n"
            + "        \"results\": [\n"
            + "            {\n"
            + "                \"auth\": [\n"
            + "                    \"AAAAAAAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA\"\n"
            + "                ],\n"
            + "                \"xdr\": \"AAAAAwAAABQ=\"\n"
            + "            }\n"
            + "        ],\n"
            + "        \"cost\": {\n"
            + "            \"cpuInsns\": \"1240100\",\n"
            + "            \"memBytes\": \"161637\"\n"
            + "        },\n"
            + "        \"latestLedger\": \"1479\"\n"
            + "    }\n"
            + "}";
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

    Transaction transaction = buildSorobanTransaction(null, of(auth));

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
            "AAAAAAAAAAIAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAFAAAAAEAAAAAAAAAB300Hyg0HZG+Qie3zvsxLvugrNtFqd3AIntWy9bg2YvZAAAAAAAAAAEAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAEAAAAAEAAAACAAAADwAAAAdDb3VudGVyAAAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAQAAAAAAFcLDAAAF8AAAAQgAAAMcAAAAAAAAAJw=");
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.builder()
            .hostFunction(
                ((InvokeHostFunctionOperation) transaction.getOperations()[0]).getHostFunction())
            .sourceAccount(transaction.getOperations()[0].getSourceAccount())
            .auth(of(auth))
            .build();
    Transaction expectedTx =
        new Transaction(
            transaction.getAccountConverter(),
            transaction.getSourceAccount(),
            transaction.getFee() + 58595L,
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
            + "        \"error\": \"HostError: Error(WasmVm, MissingValue)\\n\\nEvent log (newest first):\\n   0: [Diagnostic Event] contract:607682f2477a6be8cdf0fdf32be13d5f25a686cc094fd93d5aa3d7b68232d0c0, topics:[error, Error(WasmVm, MissingValue)], data:[\\\"invoking unknown export\\\", increment]\\n   1: [Diagnostic Event] topics:[fn_call, Bytes(607682f2477a6be8cdf0fdf32be13d5f25a686cc094fd93d5aa3d7b68232d0c0), increment], data:[Address(Account(58b7c4a2c8f297aa8f3d2471281fdfccecafe48e5663313ec18e12a73eca98a1)), 10]\\n\\nBacktrace (newest first):\\n   0: soroban_env_host::vm::Vm::invoke_function_raw\\n   1: soroban_env_host::host::frame::<impl soroban_env_host::host::Host>::call_n_internal\\n   2: soroban_env_host::host::frame::<impl soroban_env_host::host::Host>::invoke_function\\n   3: preflight::preflight_invoke_hf_op::{{closure}}\\n   4: preflight::catch_preflight_panic\\n   5: _cgo_a3255893d7fd_Cfunc_preflight_invoke_hf_op\\n             at /tmp/go-build/cgo-gcc-prolog:99:11\\n   6: runtime.asmcgocall\\n             at ./runtime/asm_amd64.s:848\\n\\n\",\n"
            + "        \"transactionData\": \"\",\n"
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

  @Test(expected = PrepareTransactionException.class)
  public void testPrepareTransactionWithPrepareTransactionExceptionThrowsErrorInvalidResults()
      throws IOException, SorobanRpcErrorResponse, PrepareTransactionException {
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"e1fabdcdf0244a2a9adfab94d7748b6c\",\n"
            + "    \"result\": {\n"
            + "        \"transactionData\": \"AAAAAAAAAAIAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAFAAAAAEAAAAAAAAAB300Hyg0HZG+Qie3zvsxLvugrNtFqd3AIntWy9bg2YvZAAAAAAAAAAEAAAAGAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAAEAAAAAEAAAACAAAADwAAAAdDb3VudGVyAAAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAQAAAAAAFcLDAAAF8AAAAQgAAAMcAAAAAAAAAJw=\",\n"
            + "        \"events\": [\n"
            + "            \"AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAgxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=\",\n"
            + "            \"AAAAAQAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAABQ=\"\n"
            + "        ],\n"
            + "        \"minResourceFee\": \"58595\",\n"
            + "        \"results\": [\n"
            + "            {\n"
            + "                \"auth\": [\n"
            + "                    \"AAAAAAAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA\"\n"
            + "                ],\n"
            + "                \"xdr\": \"AAAAAwAAABQ=\"\n"
            + "            },\n"
            + "            {\n"
            + "                \"auth\": [\n"
            + "                    \"AAAAAAAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA\"\n"
            + "                ],\n"
            + "                \"xdr\": \"AAAAAwAAABQ=\"\n"
            + "            }\n"
            + "        ],\n"
            + "        \"cost\": {\n"
            + "            \"cpuInsns\": \"1240100\",\n"
            + "            \"memBytes\": \"161637\"\n"
            + "        },\n"
            + "        \"latestLedger\": \"1479\"\n"
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

  @Test
  public void testSendTransaction()
      throws IOException, SorobanRpcErrorResponse, PrepareTransactionException {
    String json =
        "{\n"
            + "    \"jsonrpc\": \"2.0\",\n"
            + "    \"id\": \"688dfcf3bcd04f52af4866e98dffe387\",\n"
            + "    \"result\": {\n"
            + "        \"status\": \"PENDING\",\n"
            + "        \"hash\": \"f59636c3bb27ad958c599632405ed657c3d7d55c717dbfd2644a68625e9d9e7e\",\n"
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
      SorobanTransactionData sorobanData, Collection<SorobanAuthorizationEntry> auth) {
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    KeyPair txSubmitterKp =
        KeyPair.fromSecretSeed("SAAPYAPTTRZMCUZFPG3G66V4ZMHTK4TWA6NS7U4F7Z3IMUD52EK4DDEV");
    KeyPair opInvokerKp =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");

    TransactionBuilderAccount source = new Account(txSubmitterKp.getAccountId(), 6171868004355L);

    if (auth == null) {
      auth = new ArrayList<>();
    }

    return new TransactionBuilder(AccountConverter.enableMuxed(), source, Network.STANDALONE)
        .setBaseFee(50000)
        .addPreconditions(
            TransactionPreconditions.builder().timeBounds(new TimeBounds(0, 0)).build())
        .addOperation(
            InvokeHostFunctionOperation.builder()
                .sourceAccount(opInvokerKp.getAccountId())
                .hostFunction(
                    new HostFunction.Builder()
                        .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_INVOKE_CONTRACT)
                        .invokeContract(
                            new SCVec(
                                new SCVal[] {
                                  new Address(contractId).toSCVal(),
                                  new SCVal.Builder()
                                      .discriminant(SCValType.SCV_SYMBOL)
                                      .sym(new SCSymbol(new XdrString("increment")))
                                      .build(),
                                  new Address(opInvokerKp.getAccountId()).toSCVal(),
                                  new SCVal.Builder()
                                      .discriminant(SCValType.SCV_U32)
                                      .u32(new Uint32(new XdrUnsignedInteger(10)))
                                      .build()
                                }))
                        .build())
                .auth(auth)
                .build())
        .setSorobanData(sorobanData)
        .build();
  }

  private static SorobanAuthorizationEntry sorobanAuthorizationEntryFromXdrBase64(
      String sorobanAuthorizationEntry) {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(sorobanAuthorizationEntry);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    XdrDataInputStream xdrInputStream = new XdrDataInputStream(inputStream);
    try {
      return SorobanAuthorizationEntry.decode(xdrInputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "invalid ledgerEntryData: " + sorobanAuthorizationEntry, e);
    }
  }

  private static String ledgerKeyToXdrBase64(LedgerKey ledgerKey) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    try {
      ledgerKey.encode(xdrDataOutputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid ledgerKey.", e);
    }
    BaseEncoding base64Encoding = BaseEncoding.base64();
    return base64Encoding.encode(byteArrayOutputStream.toByteArray());
  }
}
