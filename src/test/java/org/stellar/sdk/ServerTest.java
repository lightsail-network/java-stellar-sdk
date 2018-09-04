package org.stellar.sdk;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.SubmitTransactionResponse;
import org.stellar.sdk.responses.SubmitTransactionTimeoutResponseException;
import org.stellar.sdk.responses.SubmitTransactionUnknownResponseException;
import org.stellar.sdk.responses.operations.OperationResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ServerTest {
    private final String publicRootResponse = "{\n" +
            "  \"_links\": {\n" +
            "    \"account\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/accounts/{account_id}\",\n" +
            "      \"templated\": true\n" +
            "    },\n" +
            "    \"account_transactions\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/accounts/{account_id}/transactions{?cursor,limit,order}\",\n" +
            "      \"templated\": true\n" +
            "    },\n" +
            "    \"assets\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/assets{?asset_code,asset_issuer,cursor,limit,order}\",\n" +
            "      \"templated\": true\n" +
            "    },\n" +
            "    \"metrics\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/metrics\"\n" +
            "    },\n" +
            "    \"order_book\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/order_book{?selling_asset_type,selling_asset_code,selling_asset_issuer,buying_asset_type,buying_asset_code,buying_asset_issuer,limit}\",\n" +
            "      \"templated\": true\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/\"\n" +
            "    },\n" +
            "    \"transaction\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/transactions/{hash}\",\n" +
            "      \"templated\": true\n" +
            "    },\n" +
            "    \"transactions\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/transactions{?cursor,limit,order}\",\n" +
            "      \"templated\": true\n" +
            "    }\n" +
            "  },\n" +
            "  \"horizon_version\": \"0.18.0-92259749c681df66a8347f846e94681a24f2a920\",\n" +
            "  \"core_version\": \"stellar-core 11.1.0 (324c1bd61b0e9bada63e0d696d799421b00a7950)\",\n" +
            "  \"history_latest_ledger\": 24345129,\n" +
            "  \"history_elder_ledger\": 1,\n" +
            "  \"core_latest_ledger\": 24345130,\n" +
            "  \"network_passphrase\": \"Public Global Stellar Network ; September 2015\",\n" +
            "  \"current_protocol_version\": 11,\n" +
            "  \"core_supported_protocol_version\": 11\n" +
            "}";
    private final String successResponse =
            "{\n" +
            "  \"_links\": {\n" +
            "    \"transaction\": {\n" +
            "      \"href\": \"/transactions/2634d2cf5adcbd3487d1df042166eef53830115844fdde1588828667bf93ff42\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"hash\": \"2634d2cf5adcbd3487d1df042166eef53830115844fdde1588828667bf93ff42\",\n" +
            "  \"ledger\": 826150,\n" +
            "  \"envelope_xdr\": \"AAAAAKu3N77S+cHLEDfVD2eW/CqRiN9yvAKH+qkeLjHQs1u+AAAAZAAMkoMAAAADAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAbYQq8ek1GitmNBUloGnetfWxSpxlsgK48Xi66dIL3MoAAAAAC+vCAAAAAAAAAAAB0LNbvgAAAEDadQ25SNHWTg0L+2wr/KNWd8/EwSNFkX/ncGmBGA3zkNGx7lAow78q8SQmnn2IsdkD9MwICirhsOYDNbaqShwO\",\n" +
            "  \"result_xdr\": \"AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAA=\",\n" +
            "  \"result_meta_xdr\": \"AAAAAAAAAAEAAAACAAAAAAAMmyYAAAAAAAAAAG2EKvHpNRorZjQVJaBp3rX1sUqcZbICuPF4uunSC9zKAAAAAAvrwgAADJsmAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAQAMmyYAAAAAAAAAAKu3N77S+cHLEDfVD2eW/CqRiN9yvAKH+qkeLjHQs1u+AAAAFzCfYtQADJKDAAAAAwAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAA\"\n" +
            "}";

    private final String failureResponse =
            "{\n" +
            "  \"type\": \"https://stellar.org/horizon-errors/transaction_failed\",\n" +
            "  \"title\": \"Transaction Failed\",\n" +
            "  \"status\": 400,\n" +
            "  \"detail\": \"TODO\",\n" +
            "  \"instance\": \"horizon-testnet-001.prd.stellar001.internal.stellar-ops.com/IxhaI70Tqo-112305\",\n" +
            "  \"extras\": {\n" +
            "    \"envelope_xdr\": \"AAAAAK4Pg4OEkjGmSN0AN37K/dcKyKPT2DC90xvjjawKp136AAAAZAAKsZQAAAABAAAAAAAAAAEAAAAJSmF2YSBGVFchAAAAAAAAAQAAAAAAAAABAAAAAG9wfBI7rRYoBlX3qRa0KOnI75W5BaPU6NbyKmm2t71MAAAAAAAAAAABMS0AAAAAAAAAAAEKp136AAAAQOWEjL+Sm+WP2puE9dLIxWlOibIEOz8PsXyG77jOCVdHZfQvkgB49Mu5wqKCMWWIsDSLFekwUsLaunvmXrpyBwQ=\",\n" +
            "    \"result_codes\": {\n" +
            "      \"transaction\": \"tx_failed\",\n" +
            "      \"operations\": [\n" +
            "        \"op_no_destination\"\n" +
            "      ]\n" +
            "    },\n" +
            "    \"result_xdr\": \"AAAAAAAAAGT/////AAAAAQAAAAAAAAAB////+wAAAAA=\"\n" +
            "  }\n" +
            "}";

    private final String timeoutResponse =
            "{\n" +
            "  \"type\": \"https://stellar.org/horizon-errors/transaction_failed\",\n" +
            "  \"title\": \"Timeout\",\n" +
            "  \"status\": 403,\n" +
            "  \"detail\": \"TODO\",\n" +
            "  \"instance\": \"horizon-testnet-001.prd.stellar001.internal.stellar-ops.com/IxhaI70Tqo-112305\"\n" +
            "}";

    private final String internalServerErrorResponse =
            "{\n" +
            "  \"type\":     \"https://www.stellar.org/docs/horizon/problems/server_error\",\n" +
            "  \"title\":    \"Internal Server Error\",\n" +
            "  \"status\":   500,\n" +
            "  \"instance\": \"d3465740-ec3a-4a0b-9d4a-c9ea734ce58a\"\n" +
            "}";

    private final String operationsPageResponse = "{\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://horizon-testnet.stellar.org/operations?order=desc\\u0026limit=10\\u0026cursor=\"\n" +
            "    },\n" +
            "    \"next\": {\n" +
            "      \"href\": \"http://horizon-testnet.stellar.org/operations?order=desc\\u0026limit=10\\u0026cursor=3695540185337857\"\n" +
            "    },\n" +
            "    \"prev\": {\n" +
            "      \"href\": \"http://horizon-testnet.stellar.org/operations?order=asc\\u0026limit=10\\u0026cursor=3717508943056897\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"_embedded\": {\n" +
            "    \"records\": [\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/3717508943056897\"\n" +
            "          },\n" +
            "          \"transaction\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/transactions/ce81d957352501a46d9b938462cbef76283dcba8108d2649e0d79279a8f36488\"\n" +
            "          },\n" +
            "          \"effects\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/operations/3717508943056897/effects\"\n" +
            "          },\n" +
            "          \"succeeds\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=desc\\u0026cursor=3717508943056897\"\n" +
            "          },\n" +
            "          \"precedes\": {\n" +
            "            \"href\": \"http://horizon-testnet.stellar.org/effects?order=asc\\u0026cursor=3717508943056897\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3717508943056897\",\n" +
            "        \"paging_token\": \"3717508943056897\",\n" +
            "        \"source_account\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
            "        \"type\": \"create_account\",\n" +
            "        \"type_i\": 0,\n" +
            "        \"created_at\": \"2018-01-22T21:30:53Z\",\n" +
            "        \"transaction_hash\": \"dd9d10c80a344f4464df3ecaa63705a5ef4a0533ff2f2099d5ef371ab5e1c046\","+
            "        \"starting_balance\": \"10000.0\",\n" +
            "        \"funder\": \"GBS43BF24ENNS3KPACUZVKK2VYPOZVBQO2CISGZ777RYGOPYC2FT6S3K\",\n" +
            "        \"account\": \"GDFH4NIYMIIAKRVEJJZOIGWKXGQUF3XHJG6ZM6CEA64AMTVDN44LHOQE\"\n" +
            "      }\n"+
            "    ]\n" +
            "  }\n" +
            "}";

    Transaction buildTransaction() throws IOException {
        return buildTransaction(Network.PUBLIC);
    }

    Transaction buildTransaction(Network network) throws IOException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction.Builder builder = new Transaction.Builder(account, network)
                .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                .addMemo(Memo.text("Hello world!"))
                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE);

        assertEquals(1, builder.getOperationsCount());
        Transaction transaction = builder.build();
        assertEquals(2908908335136769L, transaction.getSequenceNumber());
        assertEquals(new Long(2908908335136769L), account.getSequenceNumber());
        transaction.sign(source);
        return transaction;
    }

    @Test
    public void testSubmitTransactionNetworkMisMatch() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(publicRootResponse));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(successResponse));
        mockWebServer.start();
        HttpUrl baseUrl = mockWebServer.url("");
        Server server = new Server(baseUrl.toString());

        try {
            server.submitTransaction(this.buildTransaction(Network.TESTNET));
            fail("expected NetworkMismatchException exception");
        } catch (NetworkMismatchException e) {
            // expect exception
        }
    }

    @Test
    public void testSubmitTransactionSuccess() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(publicRootResponse));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(successResponse));
        mockWebServer.start();
        HttpUrl baseUrl = mockWebServer.url("");
        Server server = new Server(baseUrl.toString());

        SubmitTransactionResponse response = server.submitTransaction(this.buildTransaction());
        assertTrue(response.isSuccess());
        assertEquals(response.getLedger(), new Long(826150L));
        assertEquals(response.getHash(), "2634d2cf5adcbd3487d1df042166eef53830115844fdde1588828667bf93ff42");
        assertEquals(response.getEnvelopeXdr().get(), "AAAAAKu3N77S+cHLEDfVD2eW/CqRiN9yvAKH+qkeLjHQs1u+AAAAZAAMkoMAAAADAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAbYQq8ek1GitmNBUloGnetfWxSpxlsgK48Xi66dIL3MoAAAAAC+vCAAAAAAAAAAAB0LNbvgAAAEDadQ25SNHWTg0L+2wr/KNWd8/EwSNFkX/ncGmBGA3zkNGx7lAow78q8SQmnn2IsdkD9MwICirhsOYDNbaqShwO");
        assertEquals(response.getResultXdr().get(), "AAAAAAAAAGQAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAA=");
        assertNull(response.getExtras());
    }

    @Test
    public void testSubmitTransactionFail() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(publicRootResponse));
        mockWebServer.enqueue(new MockResponse().setResponseCode(400).setBody(failureResponse));
        mockWebServer.start();
        HttpUrl baseUrl = mockWebServer.url("");
        Server server = new Server(baseUrl.toString());

        SubmitTransactionResponse response = server.submitTransaction(this.buildTransaction());
        assertFalse(response.isSuccess());
        assertNull(response.getLedger());
        assertNull(response.getHash());
        assertEquals(response.getExtras().getEnvelopeXdr(), "AAAAAK4Pg4OEkjGmSN0AN37K/dcKyKPT2DC90xvjjawKp136AAAAZAAKsZQAAAABAAAAAAAAAAEAAAAJSmF2YSBGVFchAAAAAAAAAQAAAAAAAAABAAAAAG9wfBI7rRYoBlX3qRa0KOnI75W5BaPU6NbyKmm2t71MAAAAAAAAAAABMS0AAAAAAAAAAAEKp136AAAAQOWEjL+Sm+WP2puE9dLIxWlOibIEOz8PsXyG77jOCVdHZfQvkgB49Mu5wqKCMWWIsDSLFekwUsLaunvmXrpyBwQ=");
        assertEquals(response.getEnvelopeXdr().get(), "AAAAAK4Pg4OEkjGmSN0AN37K/dcKyKPT2DC90xvjjawKp136AAAAZAAKsZQAAAABAAAAAAAAAAEAAAAJSmF2YSBGVFchAAAAAAAAAQAAAAAAAAABAAAAAG9wfBI7rRYoBlX3qRa0KOnI75W5BaPU6NbyKmm2t71MAAAAAAAAAAABMS0AAAAAAAAAAAEKp136AAAAQOWEjL+Sm+WP2puE9dLIxWlOibIEOz8PsXyG77jOCVdHZfQvkgB49Mu5wqKCMWWIsDSLFekwUsLaunvmXrpyBwQ=");
        assertEquals(response.getExtras().getResultXdr(), "AAAAAAAAAGT/////AAAAAQAAAAAAAAAB////+wAAAAA=");
        assertEquals(response.getResultXdr().get(), "AAAAAAAAAGT/////AAAAAQAAAAAAAAAB////+wAAAAA=");
        assertNotNull(response.getExtras());
        assertEquals("tx_failed", response.getExtras().getResultCodes().getTransactionResultCode());
        assertEquals("op_no_destination", response.getExtras().getResultCodes().getOperationsResultCodes().get(0));
    }

    @Test(expected = SubmitTransactionTimeoutResponseException.class)
    public void testSubmitTransactionTimeout() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(publicRootResponse));
        mockWebServer.enqueue(new MockResponse().setResponseCode(504).setBody(timeoutResponse).setBodyDelay(5, TimeUnit.SECONDS));
        mockWebServer.start();
        HttpUrl baseUrl = mockWebServer.url("");
        Server server = new Server(baseUrl.toString());

        // We're creating a new OkHttpClient to make this test faster
        OkHttpClient testSubmitHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
        server.setSubmitHttpClient(testSubmitHttpClient);

        server.submitTransaction(this.buildTransaction());
    }

    @Test(expected = SubmitTransactionTimeoutResponseException.class)
    public void testSubmitTransactionTimeoutWithoutResponse() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(publicRootResponse));
        mockWebServer.start();
        HttpUrl baseUrl = mockWebServer.url("");
        Server server = new Server(baseUrl.toString());

        // We're creating a new OkHttpClient to make this test faster
        OkHttpClient testSubmitHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
        server.setSubmitHttpClient(testSubmitHttpClient);

        server.submitTransaction(this.buildTransaction());
    }

    @Test
    public void testSubmitTransactionInternalError() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(publicRootResponse));
        mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody(internalServerErrorResponse));
        mockWebServer.start();
        HttpUrl baseUrl = mockWebServer.url("");
        Server server = new Server(baseUrl.toString());
        // We're creating a new OkHttpClient to make this test faster
        OkHttpClient testSubmitHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();;
        server.setSubmitHttpClient(testSubmitHttpClient);

        try {
            server.submitTransaction(this.buildTransaction());
            fail("submitTransaction didn't throw exception");
        } catch (SubmitTransactionUnknownResponseException e) {
            assertEquals(500, e.getCode());
        } catch (Exception e) {
            fail("submitTransaction thrown invalid exception");
        }
    }

    @Test
    public void testNextPage() throws IOException, URISyntaxException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(operationsPageResponse));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(operationsPageResponse));
        mockWebServer.start();
        HttpUrl baseUrl = mockWebServer.url("");
        Server server = new Server(baseUrl.toString());

        Page<OperationResponse> page = server.operations().execute();
        assertEquals(1, page.getRecords().size());
        assertEquals("dd9d10c80a344f4464df3ecaa63705a5ef4a0533ff2f2099d5ef371ab5e1c046", page.getRecords().get(0).getTransactionHash());
        Page<OperationResponse> nextPage = page.getNextPage(server.getHttpClient());
        assertEquals(1, page.getRecords().size());
    }
}
