package org.stellar.sdk;

import junit.framework.TestCase;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.any;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClients.class, Request.class, Server.class})

public class HorizonPathTest extends TestCase {

    @Mock
    private HttpClient mockClient;
    @Mock
    private HttpResponse mockHttpResponse;
    @Mock
    private Request mockRequest;
    @Mock
    private Response mockResponse;
    @Mock
    private HttpPost mockHttpPost;

    @Mock
    private CloseableHttpClient closeableHttpClient;

    private Server server;

    private List<String> horizonPaths = null;
    
    private String uriEndpoint;

    @Before
    @Override
    public void setUp() throws URISyntaxException, IOException {

        Network.useTestNetwork();

        PowerMockito.mockStatic(HttpClients.class);

        MockitoAnnotations.initMocks(this);

        PowerMockito.when(HttpClients.createDefault()).thenReturn(closeableHttpClient);
        PowerMockito.when(mockClient.execute((HttpPost) any())).thenReturn(mockHttpResponse);
        PowerMockito.when(mockRequest.execute()).thenReturn(mockResponse);

        horizonPaths = new ArrayList<String>();
        //horizon path without folder
        horizonPaths.add("https://acme.com:1337");
        //horizon path with folder
        horizonPaths.add("https://acme.com:1337/folder");
        //horizon path with folder and subfolder
        horizonPaths.add("https://acme.com:1337/folder/subfolder");

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
        assertEquals(2908908335136769L, account.getSequenceNumber().longValue());
        transaction.sign(source);
        return transaction;
    }

    void prepareEndpoint(String horizonPath, String endpoint) throws Exception {
        this.uriEndpoint = horizonPath + endpoint;
        PowerMockito.mockStatic(Request.class);
        PowerMockito.when(Request.class, "Get", new URI(uriEndpoint)).thenReturn(mockRequest);
        this.server = new Server(horizonPath);
    }
    
    void verifyEndpointCalled() throws Exception {
        PowerMockito.verifyStatic(Request.class);
        Request.Get(new URI(this.uriEndpoint));       
    }

    @Test
    public void testServerAccounts() throws Exception {

        for (String horizonPath : horizonPaths) {

            this.prepareEndpoint(horizonPath, "/accounts");
            server.accounts().execute();
            this.verifyEndpointCalled();

        }
    }

    @Test
    public void testServerAccountsAccount() throws Exception {

        for (String horizonPath : horizonPaths) {

            KeyPair keyPair = KeyPair.random();
            this.prepareEndpoint(horizonPath, "/accounts/" + keyPair.getAccountId());
            server.accounts().account(keyPair);
            this.verifyEndpointCalled();
            
        }
    }

    @Test
    public void testServerTransactions() throws Exception {

        for (String horizonPath : horizonPaths) {

            this.prepareEndpoint(horizonPath, "/transactions");
            server.transactions().execute();
            this.verifyEndpointCalled();

        }
    }

    @Test
    public void testServerTransactionsTransaction() throws Exception {

        for (String horizonPath : horizonPaths) {

            this.prepareEndpoint(horizonPath, "/transactions/fooTransactionId");
            server.transactions().transaction("fooTransactionId");
            this.verifyEndpointCalled();

        }
    }

    @Test
    public void testServerTransactionsForAccount() throws Exception {

        for (String horizonPath : horizonPaths) {

            KeyPair keyPair = KeyPair.random();
            this.prepareEndpoint(horizonPath,  "/accounts/" + keyPair.getAccountId() + "/transactions");
            server.transactions().forAccount(keyPair).execute();
            this.verifyEndpointCalled();

        }
    }

    @Test
    public void testSubmitTransaction() throws Exception {

        for (String horizonPath : horizonPaths) {

            String transactionEndpoint = horizonPath + "/transactions";
            
            PowerMockito.whenNew(HttpPost.class).withArguments(new URI(transactionEndpoint)).thenReturn(mockHttpPost);

            server = new Server(horizonPath);
            server.setHttpClient(mockClient);
            server.submitTransaction(this.buildTransaction());
            PowerMockito.verifyNew(HttpPost.class).withArguments(new URI(transactionEndpoint));
            
        }
    }

}
