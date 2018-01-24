package org.stellar.sdk;

import okhttp3.*;
import org.stellar.sdk.requests.*;
import org.stellar.sdk.responses.GsonSingleton;
import org.stellar.sdk.responses.SubmitTransactionResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Main class used to connect to Horizon server.
 */
public class Server {
    private HttpUrl serverURI;
    private OkHttpClient httpClient;

    public Server(String uri) {
        serverURI = HttpUrl.parse(uri);
        httpClient = HttpClientSingleton.getInstance();
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
     * @param transaction transaction to submit to the network.
     * @return {@link SubmitTransactionResponse}
     * @throws IOException
     */
    public SubmitTransactionResponse submitTransaction(Transaction transaction) throws IOException {
        HttpUrl transactionsURI = serverURI.newBuilder().addPathSegment("transactions").build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("tx", transaction.toEnvelopeXdrBase64())
                .build();
        Request submitTransactionRequest = new Request.Builder().url(transactionsURI).post(requestBody).build();
        Response response = this.httpClient.newCall(submitTransactionRequest).execute();

        ResponseBody responseBody = response.body();

        if (responseBody == null) {
            return null;
        }

        SubmitTransactionResponse submitTransactionResponse = GsonSingleton.getInstance().fromJson(responseBody.string(), SubmitTransactionResponse.class);
        return submitTransactionResponse;
    }

    private static class HttpClientSingleton {
        private static OkHttpClient instance = null;

        private HttpClientSingleton() {
        }

        public static OkHttpClient getInstance() {
            if (instance == null) {
                synchronized (HttpClientSingleton.class) {
                    if(instance == null) {
                        instance = new OkHttpClient.Builder()
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(60, TimeUnit.SECONDS)
                                .retryOnConnectionFailure(false)
                                .build();
                    }
                }

            }
            return instance;
        }

    }
}
