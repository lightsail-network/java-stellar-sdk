package org.stellar.sdk;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.stellar.sdk.responses.SubmitTransactionResponse;

import java.io.IOException;
import java.net.URISyntaxException;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 23)
public class ServerTest extends TestCase {

  private OkHttpClient mockClient;
  private MockWebServer mockWebServer;

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
  private Server server;

  @Before
  public void setUp() throws URISyntaxException, IOException {
    Network.useTestNetwork();

    MockitoAnnotations.initMocks(this);
    mockWebServer = new MockWebServer();
    mockWebServer.start();
    mockClient = new OkHttpClient();
    server = new Server(mockWebServer.url("/").url().toString());
    server.setHttpClient(mockClient);
  }

  @After
  public void resetNetwork() {
    Network.use(null);
  }

  Transaction buildTransaction() throws IOException {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source, 2908908335136768L);
    Transaction.Builder builder = new Transaction.Builder(account)
        .addOperation(new CreateAccountOperation.Builder(destination, "2000").build())
        .addMemo(Memo.text("Hello world!"));

    assertEquals(1, builder.getOperationsCount());
    Transaction transaction = builder.build();
    assertEquals(2908908335136769L, transaction.getSequenceNumber());
    assertEquals(new Long(2908908335136769L), account.getSequenceNumber());
    transaction.sign(source);
    return transaction;
  }

  @Test
  public void testSubmitTransactionSuccess() throws IOException {
    mockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setBody(successResponse)
    );

    SubmitTransactionResponse response = server.submitTransaction(this.buildTransaction());
    assertTrue(response.isSuccess());
    assertEquals(response.getLedger(), new Long(826150L));
    assertEquals(response.getHash(), "2634d2cf5adcbd3487d1df042166eef53830115844fdde1588828667bf93ff42");
    assertNull(response.getExtras());
  }

  @Test
  public void testSubmitTransactionFail() throws IOException {
    mockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(400)
            .setBody(failureResponse)
    );

    SubmitTransactionResponse response = server.submitTransaction(this.buildTransaction());
    assertFalse(response.isSuccess());
    assertNull(response.getLedger());
    assertNull(response.getHash());
    assertEquals(response.getExtras().getEnvelopeXdr(), "AAAAAK4Pg4OEkjGmSN0AN37K/dcKyKPT2DC90xvjjawKp136AAAAZAAKsZQAAAABAAAAAAAAAAEAAAAJSmF2YSBGVFchAAAAAAAAAQAAAAAAAAABAAAAAG9wfBI7rRYoBlX3qRa0KOnI75W5BaPU6NbyKmm2t71MAAAAAAAAAAABMS0AAAAAAAAAAAEKp136AAAAQOWEjL+Sm+WP2puE9dLIxWlOibIEOz8PsXyG77jOCVdHZfQvkgB49Mu5wqKCMWWIsDSLFekwUsLaunvmXrpyBwQ=");
    assertEquals(response.getExtras().getResultXdr(), "AAAAAAAAAGT/////AAAAAQAAAAAAAAAB////+wAAAAA=");
    assertNotNull(response.getExtras());
    assertEquals("tx_failed", response.getExtras().getResultCodes().getTransactionResultCode());
    assertEquals("op_no_destination", response.getExtras().getResultCodes().getOperationsResultCodes().get(0));
  }
}
