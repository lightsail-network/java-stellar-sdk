package org.stellar.sdk;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.stellar.sdk.requests.*;
import org.stellar.sdk.responses.GsonSingleton;
import org.stellar.sdk.responses.SubmitTransactionResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class used to connect to Horizon server.
 */
public class Server {
    private URI serverURI;
    private HttpClient httpClient = HttpClients.createDefault();

    public Server(String uri) {
        try {
            serverURI = new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns {@link AccountsRequestBuilder} instance.
     */
    public AccountsRequestBuilder accounts() {
        return new AccountsRequestBuilder(serverURI);
    }

    /**
     * Returns {@link EffectsRequestBuilder} instance.
     */
    public EffectsRequestBuilder effects() {
        return new EffectsRequestBuilder(serverURI);
    }

    /**
     * Returns {@link LedgersRequestBuilder} instance.
     */
    public LedgersRequestBuilder ledgers() {
        return new LedgersRequestBuilder(serverURI);
    }

    /**
     * Returns {@link OffersRequestBuilder} instance.
     */
    public OffersRequestBuilder offers() {
        return new OffersRequestBuilder(serverURI);
    }

    /**
     * Returns {@link OperationsRequestBuilder} instance.
     */
    public OperationsRequestBuilder operations() {
        return new OperationsRequestBuilder(serverURI);
    }

    /**
     * Returns {@link OrderBookRequestBuilder} instance.
     */
    public OrderBookRequestBuilder orderBook() {
        return new OrderBookRequestBuilder(serverURI);
    }

    /**
     * Returns {@link PathsRequestBuilder} instance.
     */
    public PathsRequestBuilder paths() {
        return new PathsRequestBuilder(serverURI);
    }

    /**
     * Returns {@link PaymentsRequestBuilder} instance.
     */
    public PaymentsRequestBuilder payments() {
        return new PaymentsRequestBuilder(serverURI);
    }

    /**
     * Returns {@link TransactionsRequestBuilder} instance.
     */
    public TransactionsRequestBuilder transactions() {
        return new TransactionsRequestBuilder(serverURI);
    }

    /**
     * Submits transaction to the network.
     * @param transaction transaction to submit to the network.
     * @return {@link SubmitTransactionResponse}
     * @throws IOException
     */
    public SubmitTransactionResponse submitTransaction(Transaction transaction) throws IOException {
        URI transactionsURI;
        try {
            transactionsURI = new URIBuilder(serverURI).setPath("/transactions").build();
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
        HttpPost submitTransactionRequest = new HttpPost(transactionsURI);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tx", transaction.toEnvelopeXdrBase64()));
        submitTransactionRequest.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse response = httpClient.execute(submitTransactionRequest);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            InputStream responseStream = entity.getContent();
            try {
                StringWriter writer = new StringWriter();
                IOUtils.copy(responseStream, writer, StandardCharsets.UTF_8);
                String responseString = writer.toString();
                SubmitTransactionResponse submitTransactionResponse = GsonSingleton.getInstance().fromJson(responseString, SubmitTransactionResponse.class);
                return submitTransactionResponse;
            } finally {
                responseStream.close();
            }
        }
        return null;
    }

    /**
     * To support mocking a client
     * @param httpClient
     */
    void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
