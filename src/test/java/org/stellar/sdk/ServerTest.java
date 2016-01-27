package org.stellar.sdk;

import junit.framework.TestCase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.stellar.base.CreateAccountOperation;
import org.stellar.base.KeyPair;
import org.stellar.base.Memo;
import org.stellar.base.Transaction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ServerTest extends TestCase {
    @Mock
    private HttpClient mockClient;
    @Mock
    private HttpResponse mockResponse;
    @Mock
    private HttpEntity mockEntity;

    private final StatusLine httpOK = new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
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

    private final StatusLine httpBadRequest = new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_BAD_REQUEST, "Bad Request");
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

    public void setUp() throws URISyntaxException, IOException {
        MockitoAnnotations.initMocks(this);
        server = new Server("https://horizon.stellar.org");
        server.setHttpClient(mockClient);

        when(mockResponse.getEntity()).thenReturn(mockEntity);
        when(mockClient.execute((HttpPost) any())).thenReturn(mockResponse);
    }

    Transaction buildTransaction() throws IOException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source, 2908908335136768L);
        Transaction transaction = new Transaction.Builder(account)
                .addOperation(new CreateAccountOperation.Builder(destination, "2000").build())
                .addMemo(Memo.text("Hello world!"))
                .build();

        transaction.sign(source);
        return transaction;
    }

    @Test
    public void testSubmitTransactionSuccess() throws IOException {
        InputStream jsonResponse = new ByteArrayInputStream(successResponse.getBytes(StandardCharsets.UTF_8));
        when(mockResponse.getStatusLine()).thenReturn(httpOK);
        when(mockEntity.getContent()).thenReturn(jsonResponse);

        SubmitTransactionResponse response = server.submitTransaction(this.buildTransaction());
        assertTrue(response.isSuccess());
        assertEquals(response.getLedger(), new Long(826150L));
        assertEquals(response.getHash(), "2634d2cf5adcbd3487d1df042166eef53830115844fdde1588828667bf93ff42");
    }

    @Test
    public void testSubmitTransactionFail() throws IOException {
        InputStream jsonResponse = new ByteArrayInputStream(failureResponse.getBytes(StandardCharsets.UTF_8));
        when(mockResponse.getStatusLine()).thenReturn(httpBadRequest);
        when(mockEntity.getContent()).thenReturn(jsonResponse);

        SubmitTransactionResponse response = server.submitTransaction(this.buildTransaction());
        assertFalse(response.isSuccess());
        assertNull(response.getLedger());
        assertNull(response.getHash());
        assertEquals(response.getExtras().getEnvelopeXdr(), "AAAAAK4Pg4OEkjGmSN0AN37K/dcKyKPT2DC90xvjjawKp136AAAAZAAKsZQAAAABAAAAAAAAAAEAAAAJSmF2YSBGVFchAAAAAAAAAQAAAAAAAAABAAAAAG9wfBI7rRYoBlX3qRa0KOnI75W5BaPU6NbyKmm2t71MAAAAAAAAAAABMS0AAAAAAAAAAAEKp136AAAAQOWEjL+Sm+WP2puE9dLIxWlOibIEOz8PsXyG77jOCVdHZfQvkgB49Mu5wqKCMWWIsDSLFekwUsLaunvmXrpyBwQ=");
        assertEquals(response.getExtras().getResultXdr(), "AAAAAAAAAGT/////AAAAAQAAAAAAAAAB////+wAAAAA=");
    }
}
