package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;
import org.stellar.sdk.requests.ErrorResponse;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.SubmitTransactionResponse;
import org.stellar.sdk.responses.SubmitTransactionTimeoutResponseException;
import org.stellar.sdk.responses.SubmitTransactionUnknownResponseException;
import org.stellar.sdk.responses.operations.OperationResponse;

public class ServerTest {
  private final String publicRootResponse =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"account\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/accounts/{account_id}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"account_transactions\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/accounts/{account_id}/transactions{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"assets\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/assets{?asset_code,asset_issuer,cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"metrics\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/metrics\"\n"
          + "    },\n"
          + "    \"order_book\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/order_book{?selling_asset_type,selling_asset_code,selling_asset_issuer,buying_asset_type,buying_asset_code,buying_asset_issuer,limit}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/\"\n"
          + "    },\n"
          + "    \"transaction\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/transactions/{hash}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"transactions\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/transactions{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    }\n"
          + "  },\n"
          + "  \"horizon_version\": \"0.18.0-92259749c681df66a8347f846e94681a24f2a920\",\n"
          + "  \"core_version\": \"stellar-core 11.1.0 (324c1bd61b0e9bada63e0d696d799421b00a7950)\",\n"
          + "  \"history_latest_ledger\": 24345129,\n"
          + "  \"history_elder_ledger\": 1,\n"
          + "  \"core_latest_ledger\": 24345130,\n"
          + "  \"network_passphrase\": \"Public Global Stellar Network ; September 2015\",\n"
          + "  \"current_protocol_version\": 11,\n"
          + "  \"core_supported_protocol_version\": 11\n"
          + "}";
  private final String successResponse =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"transaction\": {\n"
          + "      \"href\": \"/transactions/2634d2cf5adcbd3487d1df042166eef53830115844fdde1588828667bf93ff42\"\n"
          + "    }\n"
          + "  },\n"
          + "  \"hash\": \"2634d2cf5adcbd3487d1df042166eef53830115844fdde1588828667bf93ff42\",\n"
          + "  \"ledger\": 826150,\n"
          + "  \"envelope_xdr\": \"AAAAAKu3N77S+cHLEDfVD2eW/CqRiN9yvAKH+qkeLjHQs1u+AAAAZAAMkoMAAAADAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAbYQq8ek1GitmNBUloGnetfWxSpxlsgK48Xi66dIL3MoAAAAAC+vCAAAAAAAAAAAB0LNbvgAAAEDadQ25SNHWTg0L+2wr/KNWd8/EwSNFkX/ncGmBGA3zkNGx7lAow78q8SQmnn2IsdkD9MwICirhsOYDNbaqShwO\",\n"
          + "  \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAA=\",\n"
          + "  \"result_meta_xdr\": \"AAAAAAAAAAEAAAACAAAAAAAMmyYAAAAAAAAAAG2EKvHpNRorZjQVJaBp3rX1sUqcZbICuPF4uunSC9zKAAAAAAvrwgAADJsmAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQAMmyYAAAAAAAAAAKu3N77S+cHLEDfVD2eW/CqRiN9yvAKH+qkeLjHQs1u+AAAAFzCfYtQADJKDAAAAAwAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAA\"\n"
          + "}";

  private final String failureResponse =
      "{\n"
          + "  \"type\": \"https://developers.stellar.org/api/errors/http-status-codes/horizon-specific/transaction-failed/\",\n"
          + "  \"title\": \"Transaction Failed\",\n"
          + "  \"status\": 400,\n"
          + "  \"detail\": \"TODO\",\n"
          + "  \"instance\": \"horizon-testnet-001.prd.stellar001.internal.stellar-ops.com/IxhaI70Tqo-112305\",\n"
          + "  \"extras\": {\n"
          + "    \"envelope_xdr\": \"AAAAAK4Pg4OEkjGmSN0AN37K/dcKyKPT2DC90xvjjawKp136AAAAZAAKsZQAAAABAAAAAAAAAAEAAAAJSmF2YSBGVFchAAAAAAAAAQAAAAAAAAABAAAAAG9wfBI7rRYoBlX3qRa0KOnI75W5BaPU6NbyKmm2t71MAAAAAAAAAAABMS0AAAAAAAAAAAEKp136AAAAQOWEjL+Sm+WP2puE9dLIxWlOibIEOz8PsXyG77jOCVdHZfQvkgB49Mu5wqKCMWWIsDSLFekwUsLaunvmXrpyBwQ=\",\n"
          + "    \"result_codes\": {\n"
          + "      \"transaction\": \"tx_failed\",\n"
          + "      \"operations\": [\n"
          + "        \"op_no_destination\"\n"
          + "      ]\n"
          + "    },\n"
          + "    \"result_xdr\": \"AAAAAAAAAGT/////AAAAAQAAAAAAAAAB////+wAAAAA=\"\n"
          + "  }\n"
          + "}";

  private final String timeoutResponse =
      "{\n"
          + "  \"type\": \"https://developers.stellar.org/api/errors/http-status-codes/horizon-specific/transaction-failed/\",\n"
          + "  \"title\": \"Timeout\",\n"
          + "  \"status\": 403,\n"
          + "  \"detail\": \"TODO\",\n"
          + "  \"instance\": \"horizon-testnet-001.prd.stellar001.internal.stellar-ops.com/IxhaI70Tqo-112305\"\n"
          + "}";

  private final String internalServerErrorResponse =
      "{\n"
          + "  \"type\":     \"https://developers.stellar.org/api/errors/http-status-codes/standard/\",\n"
          + "  \"title\":    \"Internal Server Error\",\n"
          + "  \"status\":   500,\n"
          + "  \"instance\": \"d3465740-ec3a-4a0b-9d4a-c9ea734ce58a\"\n"
          + "}";

  private String operationsPageResponse(String baseUrl) {
    return "{\n"
        + "  \"_links\": {\n"
        + "    \"self\": {\n"
        + "      \"href\": \""
        + baseUrl
        + "/operations?order=desc\\u0026limit=10\\u0026cursor=\"\n"
        + "    },\n"
        + "    \"next\": {\n"
        + "      \"href\": \""
        + baseUrl
        + "/operations?order=desc\\u0026limit=10\\u0026cursor=3695540185337857\"\n"
        + "    },\n"
        + "    \"prev\": {\n"
        + "      \"href\": \""
        + baseUrl
        + "/operations?order=asc\\u0026limit=10\\u0026cursor=3717508943056897\"\n"
        + "    }\n"
        + "  },\n"
        + "  \"_embedded\": {\n"
        + "    \"records\": [\n"
        + "      {\n"
        + "        \"_links\": {\n"
        + "          \"self\": {\n"
        + "            \"href\": \""
        + baseUrl
        + "/operations/3717508943056897\"\n"
        + "          },\n"
        + "          \"transaction\": {\n"
        + "            \"href\": \""
        + baseUrl
        + "/transactions/ce81d957352501a46d9b938462cbef76283dcba8108d2649e0d79279a8f36488\"\n"
        + "          },\n"
        + "          \"effects\": {\n"
        + "            \"href\": \""
        + baseUrl
        + "/operations/3717508943056897/effects\"\n"
        + "          },\n"
        + "          \"succeeds\": {\n"
        + "            \"href\": \""
        + baseUrl
        + "/effects?order=desc\\u0026cursor=3717508943056897\"\n"
        + "          },\n"
        + "          \"precedes\": {\n"
        + "            \"href\": \""
        + baseUrl
        + "/effects?order=asc\\u0026cursor=3717508943056897\"\n"
        + "          }\n"
        + "        },\n"
        + "        \"id\": \"3717508943056897\",\n"
        + "        \"paging_token\": \"3717508943056897\",\n"
        + "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
        + "        \"type\": \"create_account\",\n"
        + "        \"type_i\": 0,\n"
        + "        \"created_at\": \"2018-01-22T21:30:53Z\",\n"
        + "        \"transaction_hash\": \"dd9d10c80a344f4464df3ecaa63705a5ef4a0533ff2f2099d5ef371ab5e1c046\","
        + "        \"starting_balance\": \"10000.0\",\n"
        + "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n"
        + "        \"account\": \"GDFH4NIYMIIAKRVEJJZOIGWKXGQUF3XHJG6ZM6CEA64AMTVDN44LHOQE\"\n"
        + "      }\n"
        + "    ]\n"
        + "  }\n"
        + "}";
  }

  Transaction buildTransaction() throws IOException {
    return buildTransaction(Network.PUBLIC);
  }

  Transaction buildTransaction(Network network) throws IOException {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    TransactionBuilder builder =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, network)
            .addOperation(
                new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
            .addMemo(Memo.text("Hello world!"))
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE);

    assertEquals(1, builder.getOperationsCount());
    Transaction transaction = builder.build();
    assertEquals(2908908335136769L, transaction.getSequenceNumber());
    assertEquals(new Long(2908908335136769L), account.getSequenceNumber());
    transaction.sign(source);
    return transaction;
  }

  @Test
  public void testSubmitTransactionNetworkMisMatch()
      throws IOException, AccountRequiresMemoException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(publicRootResponse));
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(successResponse));
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    try {
      server.submitTransaction(this.buildTransaction(Network.TESTNET), true);
      fail("expected NetworkMismatchException exception");
    } catch (NetworkMismatchException e) {
      // expect exception
    }
  }

  @Test
  public void testSubmitTransactionSuccess() throws IOException, AccountRequiresMemoException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(publicRootResponse));
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(successResponse));
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    SubmitTransactionResponse response = server.submitTransaction(this.buildTransaction(), true);
    assertTrue(response.isSuccess());
    assertEquals(response.getLedger(), new Long(826150L));
    assertEquals(
        response.getHash(), "2634d2cf5adcbd3487d1df042166eef53830115844fdde1588828667bf93ff42");
    assertEquals(
        response.getEnvelopeXdr().get(),
        "AAAAAKu3N77S+cHLEDfVD2eW/CqRiN9yvAKH+qkeLjHQs1u+AAAAZAAMkoMAAAADAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAbYQq8ek1GitmNBUloGnetfWxSpxlsgK48Xi66dIL3MoAAAAAC+vCAAAAAAAAAAAB0LNbvgAAAEDadQ25SNHWTg0L+2wr/KNWd8/EwSNFkX/ncGmBGA3zkNGx7lAow78q8SQmnn2IsdkD9MwICirhsOYDNbaqShwO");
    assertEquals(response.getResultXdr().get(), "AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAA=");
    assertNull(response.getExtras());
  }

  @Test
  public void testSubmitTransactionFail() throws IOException, AccountRequiresMemoException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(publicRootResponse));
    mockWebServer.enqueue(new MockResponse().setResponseCode(400).setBody(failureResponse));
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    SubmitTransactionResponse response = server.submitTransaction(this.buildTransaction(), true);
    assertFalse(response.isSuccess());
    assertNull(response.getLedger());
    assertNull(response.getHash());
    assertEquals(
        response.getExtras().getEnvelopeXdr(),
        "AAAAAK4Pg4OEkjGmSN0AN37K/dcKyKPT2DC90xvjjawKp136AAAAZAAKsZQAAAABAAAAAAAAAAEAAAAJSmF2YSBGVFchAAAAAAAAAQAAAAAAAAABAAAAAG9wfBI7rRYoBlX3qRa0KOnI75W5BaPU6NbyKmm2t71MAAAAAAAAAAABMS0AAAAAAAAAAAEKp136AAAAQOWEjL+Sm+WP2puE9dLIxWlOibIEOz8PsXyG77jOCVdHZfQvkgB49Mu5wqKCMWWIsDSLFekwUsLaunvmXrpyBwQ=");
    assertEquals(
        response.getEnvelopeXdr().get(),
        "AAAAAK4Pg4OEkjGmSN0AN37K/dcKyKPT2DC90xvjjawKp136AAAAZAAKsZQAAAABAAAAAAAAAAEAAAAJSmF2YSBGVFchAAAAAAAAAQAAAAAAAAABAAAAAG9wfBI7rRYoBlX3qRa0KOnI75W5BaPU6NbyKmm2t71MAAAAAAAAAAABMS0AAAAAAAAAAAEKp136AAAAQOWEjL+Sm+WP2puE9dLIxWlOibIEOz8PsXyG77jOCVdHZfQvkgB49Mu5wqKCMWWIsDSLFekwUsLaunvmXrpyBwQ=");
    assertEquals(
        response.getExtras().getResultXdr(), "AAAAAAAAAGT/////AAAAAQAAAAAAAAAB////+wAAAAA=");
    assertEquals(response.getResultXdr().get(), "AAAAAAAAAGT/////AAAAAQAAAAAAAAAB////+wAAAAA=");
    assertNotNull(response.getExtras());
    assertEquals("tx_failed", response.getExtras().getResultCodes().getTransactionResultCode());
    assertEquals(
        "op_no_destination",
        response.getExtras().getResultCodes().getOperationsResultCodes().get(0));
  }

  @Test(expected = SubmitTransactionTimeoutResponseException.class)
  public void testSubmitTransactionTimeout() throws IOException, AccountRequiresMemoException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(publicRootResponse));
    mockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(504)
            .setBody(timeoutResponse)
            .setBodyDelay(5, TimeUnit.SECONDS));
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    // We're creating a new OkHttpClient to make this test faster
    OkHttpClient testSubmitHttpClient =
        new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();
    server.setSubmitHttpClient(testSubmitHttpClient);

    server.submitTransaction(this.buildTransaction(), true);
  }

  @Test(expected = SubmitTransactionTimeoutResponseException.class)
  public void testSubmitTransactionTimeoutWithoutResponse()
      throws IOException, AccountRequiresMemoException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(publicRootResponse));
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    // We're creating a new OkHttpClient to make this test faster
    OkHttpClient testSubmitHttpClient =
        new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();
    server.setSubmitHttpClient(testSubmitHttpClient);

    server.submitTransaction(this.buildTransaction(), true);
  }

  @Test
  public void testSubmitTransactionInternalError() throws IOException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(publicRootResponse));
    mockWebServer.enqueue(
        new MockResponse().setResponseCode(500).setBody(internalServerErrorResponse));
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());
    // We're creating a new OkHttpClient to make this test faster
    OkHttpClient testSubmitHttpClient =
        new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();
    server.setSubmitHttpClient(testSubmitHttpClient);

    try {
      server.submitTransaction(this.buildTransaction(), true);
      fail("submitTransaction didn't throw exception");
    } catch (SubmitTransactionUnknownResponseException e) {
      assertEquals(500, e.getCode());
    } catch (Exception e) {
      fail("submitTransaction thrown invalid exception");
    }
  }

  @Test
  public void testNextPage() throws IOException, URISyntaxException {
    final MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(
        new Dispatcher() {
          @Override
          public MockResponse dispatch(RecordedRequest request) {
            String baseUrl = request.getRequestUrl().toString().replaceAll(request.getPath(), "");
            String body = operationsPageResponse(baseUrl);
            return new MockResponse().setResponseCode(200).setBody(body);
          }
        });

    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    Page<OperationResponse> page = server.operations().execute();
    assertEquals(1, page.getRecords().size());
    assertEquals(
        "dd9d10c80a344f4464df3ecaa63705a5ef4a0533ff2f2099d5ef371ab5e1c046",
        page.getRecords().get(0).getTransactionHash());
    Page<OperationResponse> nextPage = page.getNextPage(server.getHttpClient());
    assertEquals(1, nextPage.getRecords().size());
  }

  /** The following tests are related to SEP-0029. */
  public static final String DESTINATION_ACCOUNT_MEMO_REQUIRED_A =
      "GCMDQXJJGQE6TJ5XUHJMJUUIWECC5S6VANRAOWIQMMV4ALW43JOY2SEB";

  public static final String DESTINATION_ACCOUNT_MEMO_REQUIRED_B =
      "GDUR2DMT5AQ7DJUGBIBB45NKRNQXGRJTWTQ7DPRP37EKBELSMK57RMZK";
  public static final String DESTINATION_ACCOUNT_MEMO_REQUIRED_C =
      "GCS36NBLT6OKYN5EUQOQ7ZZIM6WXXNX5ME4JGTCG3HVZOYXRRMNUHNMM";
  public static final String DESTINATION_ACCOUNT_MEMO_REQUIRED_D =
      "GAKQNN6GNGNPLYBVEDCD5QAIEHAZVNCQET3HAUR4YWQAP5RPBLU2W7UG";
  public static final String DESTINATION_ACCOUNT_NO_MEMO_REQUIRED =
      "GDYC2D4P2SRC5DCEDDK2OUFESSPCTZYLDOEF6NYHR2T7X5GUTEABCQC2";
  public static final String DESTINATION_ACCOUNT_NO_FOUND =
      "GD2OVSQPGD5FBJPMW4YN3FGDJ7JDFKNOMJT35T4H52FLHXJK5MFSR5RA";
  public static final String DESTINATION_ACCOUNT_FETCH_ERROR =
      "GB7WNQUTDLD6YJ4MR3KQN3Y6ZIDIGTA7GRKNH47HOGMP2ETFGRSLD6OG";
  public static final String DESTINATION_ACCOUNT_MEMO_ID =
      "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOG";

  private FeeBumpTransaction feeBump(Transaction inner) {
    KeyPair signer =
        KeyPair.fromSecretSeed("SA5ZEFDVFZ52GRU7YUGR6EDPBNRU2WLA6IQFQ7S2IH2DG3VFV3DOMV2Q");
    FeeBumpTransaction tx =
        new FeeBumpTransaction.Builder(inner)
            .setFeeAccount(signer.getAccountId())
            .setBaseFee(FeeBumpTransaction.MIN_BASE_FEE * 10)
            .build();
    tx.sign(signer);
    return tx;
  }

  @Test
  public void testCheckMemoRequiredWithMemo() throws IOException, AccountRequiresMemoException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(buildTestCheckMemoRequiredMockDispatcher());
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    KeyPair source =
        KeyPair.fromSecretSeed("SDQXFKA32UVQHUTLYJ42N56ZUEM5PNVVI4XE7EA5QFMLA2DHDCQX3GPY");
    Account account = new Account(source.getAccountId(), 1L);
    Transaction transaction =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
            .addOperation(
                new PaymentOperation.Builder(
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_A, new AssetTypeNative(), "10")
                    .build())
            .addOperation(
                new PathPaymentStrictReceiveOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_B,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new PathPaymentStrictSendOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_C,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new AccountMergeOperation.Builder(DESTINATION_ACCOUNT_MEMO_REQUIRED_D).build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .addMemo(new MemoText("Hello, Stellar."))
            .setBaseFee(100)
            .build();
    transaction.sign(source);
    server.submitTransaction(transaction);
    server.submitTransaction(feeBump(transaction));
  }

  @Test
  public void testCheckMemoRequiredWithMemoIdAddress()
      throws IOException, AccountRequiresMemoException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(buildTestCheckMemoRequiredMockDispatcher());
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    KeyPair source =
        KeyPair.fromSecretSeed("SDQXFKA32UVQHUTLYJ42N56ZUEM5PNVVI4XE7EA5QFMLA2DHDCQX3GPY");
    Account account = new Account(source.getAccountId(), 1L);
    Transaction transaction =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
            .addOperation(
                new PaymentOperation.Builder(
                        DESTINATION_ACCOUNT_MEMO_ID, new AssetTypeNative(), "10")
                    .build())
            .addOperation(
                new PathPaymentStrictReceiveOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_MEMO_ID,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new PathPaymentStrictSendOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_MEMO_ID,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(new AccountMergeOperation.Builder(DESTINATION_ACCOUNT_MEMO_ID).build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(100)
            .build();

    transaction.sign(source);
    server.submitTransaction(transaction);
    server.submitTransaction(feeBump(transaction));
  }

  @Test
  public void testCheckMemoRequiredWithSkipCheck()
      throws IOException, AccountRequiresMemoException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(buildTestCheckMemoRequiredMockDispatcher());
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    KeyPair source =
        KeyPair.fromSecretSeed("SDQXFKA32UVQHUTLYJ42N56ZUEM5PNVVI4XE7EA5QFMLA2DHDCQX3GPY");
    Account account = new Account(source.getAccountId(), 1L);
    Transaction transaction =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
            .addOperation(
                new PaymentOperation.Builder(
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_A, new AssetTypeNative(), "10")
                    .build())
            .addOperation(
                new PathPaymentStrictReceiveOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_NO_MEMO_REQUIRED,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new PathPaymentStrictSendOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_NO_MEMO_REQUIRED,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new AccountMergeOperation.Builder(DESTINATION_ACCOUNT_NO_MEMO_REQUIRED).build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(100)
            .build();
    transaction.sign(source);
    server.submitTransaction(transaction, true);
    server.submitTransaction(feeBump(transaction), true);
  }

  @Test
  public void testCheckMemoRequiredWithPaymentOperationNoMemo() throws IOException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(buildTestCheckMemoRequiredMockDispatcher());
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    KeyPair source =
        KeyPair.fromSecretSeed("SDQXFKA32UVQHUTLYJ42N56ZUEM5PNVVI4XE7EA5QFMLA2DHDCQX3GPY");
    Account account = new Account(source.getAccountId(), 1L);
    Transaction transaction =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
            .addOperation(
                new PaymentOperation.Builder(
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_A, new AssetTypeNative(), "10")
                    .build())
            .addOperation(
                new PathPaymentStrictReceiveOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_NO_MEMO_REQUIRED,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new PathPaymentStrictSendOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_NO_MEMO_REQUIRED,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new AccountMergeOperation.Builder(DESTINATION_ACCOUNT_NO_MEMO_REQUIRED).build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(100)
            .build();
    transaction.sign(source);
    try {
      server.submitTransaction(transaction);
      fail();
    } catch (AccountRequiresMemoException e) {
      assertEquals("Destination account requires a memo in the transaction.", e.getMessage());
      assertEquals(0, e.getOperationIndex());
      assertEquals(DESTINATION_ACCOUNT_MEMO_REQUIRED_A, e.getAccountId());
    }

    try {
      server.submitTransaction(feeBump(transaction));
      fail();
    } catch (AccountRequiresMemoException e) {
      assertEquals("Destination account requires a memo in the transaction.", e.getMessage());
      assertEquals(0, e.getOperationIndex());
      assertEquals(DESTINATION_ACCOUNT_MEMO_REQUIRED_A, e.getAccountId());
    }
  }

  @Test
  public void testCheckMemoRequiredWithPathPaymentStrictReceiveOperationNoMemo()
      throws IOException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(buildTestCheckMemoRequiredMockDispatcher());
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    KeyPair source =
        KeyPair.fromSecretSeed("SDQXFKA32UVQHUTLYJ42N56ZUEM5PNVVI4XE7EA5QFMLA2DHDCQX3GPY");
    Account account = new Account(source.getAccountId(), 1L);
    Transaction transaction =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
            .addOperation(
                new PaymentOperation.Builder(
                        DESTINATION_ACCOUNT_NO_MEMO_REQUIRED, new AssetTypeNative(), "10")
                    .build())
            .addOperation(
                new PathPaymentStrictReceiveOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_B,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new PathPaymentStrictSendOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_NO_MEMO_REQUIRED,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new AccountMergeOperation.Builder(DESTINATION_ACCOUNT_NO_MEMO_REQUIRED).build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(100)
            .build();
    transaction.sign(source);
    try {
      server.submitTransaction(transaction);
      fail();
    } catch (AccountRequiresMemoException e) {
      assertEquals("Destination account requires a memo in the transaction.", e.getMessage());
      assertEquals(1, e.getOperationIndex());
      assertEquals(DESTINATION_ACCOUNT_MEMO_REQUIRED_B, e.getAccountId());
    }

    try {
      server.submitTransaction(feeBump(transaction));
      fail();
    } catch (AccountRequiresMemoException e) {
      assertEquals("Destination account requires a memo in the transaction.", e.getMessage());
      assertEquals(1, e.getOperationIndex());
      assertEquals(DESTINATION_ACCOUNT_MEMO_REQUIRED_B, e.getAccountId());
    }
  }

  @Test
  public void testCheckMemoRequiredWithPathPaymentStrictSendOperationNoMemo() throws IOException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(buildTestCheckMemoRequiredMockDispatcher());
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    KeyPair source =
        KeyPair.fromSecretSeed("SDQXFKA32UVQHUTLYJ42N56ZUEM5PNVVI4XE7EA5QFMLA2DHDCQX3GPY");
    Account account = new Account(source.getAccountId(), 1L);
    Transaction transaction =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
            .addOperation(
                new PaymentOperation.Builder(
                        DESTINATION_ACCOUNT_NO_MEMO_REQUIRED, new AssetTypeNative(), "10")
                    .build())
            .addOperation(
                new PathPaymentStrictReceiveOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_NO_MEMO_REQUIRED,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new PathPaymentStrictSendOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_C,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new AccountMergeOperation.Builder(DESTINATION_ACCOUNT_NO_MEMO_REQUIRED).build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(100)
            .build();
    transaction.sign(source);
    try {
      server.submitTransaction(transaction);
      fail();
    } catch (AccountRequiresMemoException e) {
      assertEquals("Destination account requires a memo in the transaction.", e.getMessage());
      assertEquals(2, e.getOperationIndex());
      assertEquals(DESTINATION_ACCOUNT_MEMO_REQUIRED_C, e.getAccountId());
    }

    try {
      server.submitTransaction(feeBump(transaction));
      fail();
    } catch (AccountRequiresMemoException e) {
      assertEquals("Destination account requires a memo in the transaction.", e.getMessage());
      assertEquals(2, e.getOperationIndex());
      assertEquals(DESTINATION_ACCOUNT_MEMO_REQUIRED_C, e.getAccountId());
    }
  }

  @Test
  public void testCheckMemoRequiredWithAccountMergeOperationNoMemo() throws IOException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(buildTestCheckMemoRequiredMockDispatcher());
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    KeyPair source =
        KeyPair.fromSecretSeed("SDQXFKA32UVQHUTLYJ42N56ZUEM5PNVVI4XE7EA5QFMLA2DHDCQX3GPY");
    Account account = new Account(source.getAccountId(), 1L);
    Transaction transaction =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
            .addOperation(
                new PaymentOperation.Builder(
                        DESTINATION_ACCOUNT_NO_MEMO_REQUIRED, new AssetTypeNative(), "10")
                    .build())
            .addOperation(
                new PathPaymentStrictReceiveOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_NO_MEMO_REQUIRED,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new PathPaymentStrictSendOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_NO_MEMO_REQUIRED,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new AccountMergeOperation.Builder(DESTINATION_ACCOUNT_MEMO_REQUIRED_D).build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(100)
            .build();
    transaction.sign(source);
    try {
      server.submitTransaction(transaction);
      fail();
    } catch (AccountRequiresMemoException e) {
      assertEquals("Destination account requires a memo in the transaction.", e.getMessage());
      assertEquals(3, e.getOperationIndex());
      assertEquals(DESTINATION_ACCOUNT_MEMO_REQUIRED_D, e.getAccountId());
    }

    try {
      server.submitTransaction(feeBump(transaction));
      fail();
    } catch (AccountRequiresMemoException e) {
      assertEquals("Destination account requires a memo in the transaction.", e.getMessage());
      assertEquals(3, e.getOperationIndex());
      assertEquals(DESTINATION_ACCOUNT_MEMO_REQUIRED_D, e.getAccountId());
    }
  }

  @Test
  public void testCheckMemoRequiredTwoOperationsWithSameDestination() throws IOException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(buildTestCheckMemoRequiredMockDispatcher());
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    KeyPair source =
        KeyPair.fromSecretSeed("SDQXFKA32UVQHUTLYJ42N56ZUEM5PNVVI4XE7EA5QFMLA2DHDCQX3GPY");
    Account account = new Account(source.getAccountId(), 1L);
    Transaction transaction =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
            .addOperation(
                new PaymentOperation.Builder(
                        DESTINATION_ACCOUNT_NO_MEMO_REQUIRED, new AssetTypeNative(), "10")
                    .build())
            .addOperation(
                new PathPaymentStrictReceiveOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_NO_MEMO_REQUIRED,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new PathPaymentStrictSendOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_C,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new AccountMergeOperation.Builder(DESTINATION_ACCOUNT_MEMO_REQUIRED_D).build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(100)
            .build();
    transaction.sign(source);
    try {
      server.submitTransaction(transaction);
      fail();
    } catch (AccountRequiresMemoException e) {
      assertEquals("Destination account requires a memo in the transaction.", e.getMessage());
      assertEquals(2, e.getOperationIndex());
      assertEquals(DESTINATION_ACCOUNT_MEMO_REQUIRED_C, e.getAccountId());
    }

    try {
      server.submitTransaction(feeBump(transaction));
      fail();
    } catch (AccountRequiresMemoException e) {
      assertEquals("Destination account requires a memo in the transaction.", e.getMessage());
      assertEquals(2, e.getOperationIndex());
      assertEquals(DESTINATION_ACCOUNT_MEMO_REQUIRED_C, e.getAccountId());
    }
  }

  @Test
  public void testCheckMemoRequiredNoDestinationOperation() throws IOException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(buildTestCheckMemoRequiredMockDispatcher());
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    KeyPair source =
        KeyPair.fromSecretSeed("SDQXFKA32UVQHUTLYJ42N56ZUEM5PNVVI4XE7EA5QFMLA2DHDCQX3GPY");
    Account account = new Account(source.getAccountId(), 1L);
    Transaction transaction =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
            .addOperation(new ManageDataOperation.Builder("Hello", "Stellar".getBytes()).build())
            .addOperation(
                new PaymentOperation.Builder(
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_A, new AssetTypeNative(), "10")
                    .build())
            .addOperation(
                new PathPaymentStrictReceiveOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_A,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new PathPaymentStrictSendOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_C,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new AccountMergeOperation.Builder(DESTINATION_ACCOUNT_NO_MEMO_REQUIRED).build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(100)
            .build();
    transaction.sign(source);
    try {
      server.submitTransaction(transaction);
      fail();
    } catch (AccountRequiresMemoException e) {
      assertEquals("Destination account requires a memo in the transaction.", e.getMessage());
      assertEquals(1, e.getOperationIndex());
      assertEquals(DESTINATION_ACCOUNT_MEMO_REQUIRED_A, e.getAccountId());
    }

    try {
      server.submitTransaction(feeBump(transaction));
      fail();
    } catch (AccountRequiresMemoException e) {
      assertEquals("Destination account requires a memo in the transaction.", e.getMessage());
      assertEquals(1, e.getOperationIndex());
      assertEquals(DESTINATION_ACCOUNT_MEMO_REQUIRED_A, e.getAccountId());
    }
  }

  @Test
  public void testCheckMemoRequiredAccountNotFound()
      throws IOException, AccountRequiresMemoException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(buildTestCheckMemoRequiredMockDispatcher());
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    KeyPair source =
        KeyPair.fromSecretSeed("SDQXFKA32UVQHUTLYJ42N56ZUEM5PNVVI4XE7EA5QFMLA2DHDCQX3GPY");
    Account account = new Account(source.getAccountId(), 1L);
    Transaction transaction =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
            .addOperation(
                new PaymentOperation.Builder(
                        DESTINATION_ACCOUNT_NO_FOUND, new AssetTypeNative(), "10")
                    .build())
            .addOperation(
                new PathPaymentStrictReceiveOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_NO_FOUND,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new PathPaymentStrictSendOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_NO_FOUND,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(new AccountMergeOperation.Builder(DESTINATION_ACCOUNT_NO_FOUND).build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(100)
            .build();
    transaction.sign(source);
    server.submitTransaction(transaction);
    server.submitTransaction(feeBump(transaction));
  }

  @Test
  public void testCheckMemoRequiredFetchAccountError()
      throws IOException, AccountRequiresMemoException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.setDispatcher(buildTestCheckMemoRequiredMockDispatcher());
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    Server server = new Server(baseUrl.toString());

    KeyPair source =
        KeyPair.fromSecretSeed("SDQXFKA32UVQHUTLYJ42N56ZUEM5PNVVI4XE7EA5QFMLA2DHDCQX3GPY");
    Account account = new Account(source.getAccountId(), 1L);
    Transaction transaction =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
            .addOperation(
                new PaymentOperation.Builder(
                        DESTINATION_ACCOUNT_FETCH_ERROR, new AssetTypeNative(), "10")
                    .build())
            .addOperation(
                new PaymentOperation.Builder(
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_A, new AssetTypeNative(), "10")
                    .build())
            .addOperation(
                new PaymentOperation.Builder(
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_B, new AssetTypeNative(), "10")
                    .build())
            .addOperation(
                new PathPaymentStrictReceiveOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_C,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new PathPaymentStrictSendOperation.Builder(
                        new AssetTypeNative(),
                        "10",
                        DESTINATION_ACCOUNT_MEMO_REQUIRED_D,
                        new AssetTypeCreditAlphaNum4(
                            "BTC", "GA7GYB3QGLTZNHNGXN3BMANS6TC7KJT3TCGTR763J4JOU4QHKL37RVV2"),
                        "5")
                    .build())
            .addOperation(
                new AccountMergeOperation.Builder(DESTINATION_ACCOUNT_MEMO_REQUIRED_D).build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(100)
            .build();
    transaction.sign(source);
    try {
      server.submitTransaction(transaction);
      fail();
    } catch (ErrorResponse e) {
      assertEquals(400, e.getCode());
    }

    try {
      server.submitTransaction(feeBump(transaction));
      fail();
    } catch (ErrorResponse e) {
      assertEquals(400, e.getCode());
    }
  }

  private Dispatcher buildTestCheckMemoRequiredMockDispatcher() {
    final String memoRequiredResponse =
        "{\n"
            + "    \"data\": {\n"
            + "        \"config.memo_required\": \"MQ==\"\n"
            + "    }\n"
            + "}";
    final String noMemoRequiredResponse = "{\n" + "    \"data\": {\n" + "    }\n" + "}";
    final String successTransactionResponse =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"/transactions/2634d2cf5adcbd3487d1df042166eef53830115844fdde1588828667bf93ff42\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"hash\": \"2634d2cf5adcbd3487d1df042166eef53830115844fdde1588828667bf93ff42\",\n"
            + "  \"ledger\": 826150,\n"
            + "  \"envelope_xdr\": \"AAAAAKu3N77S+cHLEDfVD2eW/CqRiN9yvAKH+qkeLjHQs1u+AAAAZAAMkoMAAAADAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAbYQq8ek1GitmNBUloGnetfWxSpxlsgK48Xi66dIL3MoAAAAAC+vCAAAAAAAAAAAB0LNbvgAAAEDadQ25SNHWTg0L+2wr/KNWd8/EwSNFkX/ncGmBGA3zkNGx7lAow78q8SQmnn2IsdkD9MwICirhsOYDNbaqShwO\",\n"
            + "  \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAA=\",\n"
            + "  \"result_meta_xdr\": \"AAAAAAAAAAEAAAACAAAAAAAMmyYAAAAAAAAAAG2EKvHpNRorZjQVJaBp3rX1sUqcZbICuPF4uunSC9zKAAAAAAvrwgAADJsmAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQAMmyYAAAAAAAAAAKu3N77S+cHLEDfVD2eW/CqRiN9yvAKH+qkeLjHQs1u+AAAAFzCfYtQADJKDAAAAAwAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAA\"\n"
            + "}";
    final String rootResponse =
        "{\n"
            + "  \"_links\": {\n"
            + "    \"account\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/accounts/{account_id}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"account_transactions\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/accounts/{account_id}/transactions{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"assets\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/assets{?asset_code,asset_issuer,cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"metrics\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/metrics\"\n"
            + "    },\n"
            + "    \"order_book\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/order_book{?selling_asset_type,selling_asset_code,selling_asset_issuer,buying_asset_type,buying_asset_code,buying_asset_issuer,limit}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"self\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/\"\n"
            + "    },\n"
            + "    \"transaction\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/transactions/{hash}\",\n"
            + "      \"templated\": true\n"
            + "    },\n"
            + "    \"transactions\": {\n"
            + "      \"href\": \"https://horizon.stellar.org/transactions{?cursor,limit,order}\",\n"
            + "      \"templated\": true\n"
            + "    }\n"
            + "  },\n"
            + "  \"horizon_version\": \"0.18.0-92259749c681df66a8347f846e94681a24f2a920\",\n"
            + "  \"core_version\": \"stellar-core 11.1.0 (324c1bd61b0e9bada63e0d696d799421b00a7950)\",\n"
            + "  \"history_latest_ledger\": 24345129,\n"
            + "  \"history_elder_ledger\": 1,\n"
            + "  \"core_latest_ledger\": 24345130,\n"
            + "  \"network_passphrase\": \"Public Global Stellar Network ; September 2015\",\n"
            + "  \"current_protocol_version\": 11,\n"
            + "  \"core_supported_protocol_version\": 11\n"
            + "}";

    Dispatcher dispatcher =
        new Dispatcher() {
          @Override
          public MockResponse dispatch(RecordedRequest request) {
            String path = request.getPath();
            if ("/".equals(path)) {
              return new MockResponse().setResponseCode(200).setBody(rootResponse);
            }
            if (String.format("/accounts/%s", DESTINATION_ACCOUNT_MEMO_REQUIRED_A).equals(path)
                || String.format("/accounts/%s", DESTINATION_ACCOUNT_MEMO_REQUIRED_B).equals(path)
                || String.format("/accounts/%s", DESTINATION_ACCOUNT_MEMO_REQUIRED_C).equals(path)
                || String.format("/accounts/%s", DESTINATION_ACCOUNT_MEMO_REQUIRED_D)
                    .equals(path)) {
              return new MockResponse().setResponseCode(200).setBody(memoRequiredResponse);
            } else if (String.format("/accounts/%s", DESTINATION_ACCOUNT_NO_MEMO_REQUIRED)
                .equals(path)) {
              return new MockResponse().setResponseCode(200).setBody(noMemoRequiredResponse);
            } else if (String.format("/accounts/%s", DESTINATION_ACCOUNT_NO_FOUND).equals(path)) {
              return new MockResponse().setResponseCode(404);
            } else if (String.format("/accounts/%s", DESTINATION_ACCOUNT_FETCH_ERROR)
                .equals(path)) {
              return new MockResponse().setResponseCode(400);
            } else if ("/transactions".equals(path)) {
              return new MockResponse().setResponseCode(200).setBody(successTransactionResponse);
            } else {
              return new MockResponse().setResponseCode(404);
            }
          }
        };
    return dispatcher;
  }
}
