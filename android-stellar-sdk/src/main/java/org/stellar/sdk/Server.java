package org.stellar.sdk;

import android.net.Uri;

import org.stellar.sdk.requests.AccountsRequestBuilder;
import org.stellar.sdk.requests.EffectsRequestBuilder;
import org.stellar.sdk.requests.LedgersRequestBuilder;
import org.stellar.sdk.requests.OffersRequestBuilder;
import org.stellar.sdk.requests.OperationsRequestBuilder;
import org.stellar.sdk.requests.OrderBookRequestBuilder;
import org.stellar.sdk.requests.PathsRequestBuilder;
import org.stellar.sdk.requests.PaymentsRequestBuilder;
import org.stellar.sdk.requests.TradesRequestBuilder;
import org.stellar.sdk.requests.TransactionsRequestBuilder;
import org.stellar.sdk.responses.GsonSingleton;
import org.stellar.sdk.responses.SubmitTransactionResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Main class used to connect to Horizon server.
 */
public class Server {
    private URI serverURI;

    private OkHttpClient httpClient;

    /**
     * Creates server with input uri
     * @param uri Horizon server uri
     */
    public Server(String uri) {
        createUri(uri);
        httpClient = new OkHttpClient();
    }

    /**
     * Creates server with input uri and timeout for transactions, i.e. {@link Server#submitTransaction(Transaction)}
     * <p>Increase timeout to prevent timeout exception for transaction with ledger close
     * time above default of 10 sec</p>
     * @param uri Horizon server uri
     * @param transactionsTimeout transactions timeout value
     * @param timeUnit transactions timeout unit
     */
    public Server(String uri, int transactionsTimeout, TimeUnit timeUnit) {
        createUri(uri);
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(transactionsTimeout, timeUnit)
                .writeTimeout(transactionsTimeout, timeUnit)
                .readTimeout(transactionsTimeout, timeUnit)
                .build();
    }

    private void createUri(String uri) {
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
     * Returns {@link TradesRequestBuilder} instance.
     */
    public TradesRequestBuilder trades() {
        return new TradesRequestBuilder(serverURI);
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
     *
     * @param transaction transaction to submit to the network.
     * @return {@link SubmitTransactionResponse}
     * @throws IOException
     */
    public SubmitTransactionResponse submitTransaction(Transaction transaction) throws IOException {
        Uri transactionsUri = Uri.parse(serverURI.toString())
                .buildUpon()
                .appendPath("transactions")
                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("tx", transaction.toEnvelopeXdrBase64())
                .build();
        Request request = new Request.Builder()
                .url(transactionsUri.toString())
                .post(formBody)
                .build();
        ResponseBody responseBody = httpClient.newCall(request)
                .execute()
                .body();

        if (responseBody != null) {
            String responseString = responseBody.string();
            return GsonSingleton.getInstance().fromJson(responseString, SubmitTransactionResponse.class);
        }
        return null;
    }

    /**
     * To support mocking a client
     *
     * @param httpClient
     */
    void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
