package org.stellar.sdk;

import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.stellar.sdk.requests.*;
import org.stellar.sdk.responses.GsonSingleton;
import org.stellar.sdk.responses.RootResponse;
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
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Returns {@link RootResponse}.
     */
    public RootResponse root() throws IOException {
        TypeToken type = new TypeToken<RootResponse>() {};
        ResponseHandler<RootResponse> responseHandler = new ResponseHandler<RootResponse>(type);

        Request request = new Request.Builder().get().url(serverURI).build();
        Response response = httpClient.newCall(request).execute();

        return responseHandler.handleResponse(response);
    }

    /**
     * Returns {@link AccountsRequestBuilder} instance.
     */
    public AccountsRequestBuilder accounts() {
        return new AccountsRequestBuilder(httpClient, serverURI);
    }

    /**
     * Returns {@link AssetsRequestBuilder} instance.
     */
    public AssetsRequestBuilder assets() {
        return new AssetsRequestBuilder(httpClient, serverURI);
    }

    /**
     * Returns {@link EffectsRequestBuilder} instance.
     */
    public EffectsRequestBuilder effects() {
        return new EffectsRequestBuilder(httpClient, serverURI);
    }

    /**
     * Returns {@link LedgersRequestBuilder} instance.
     */
    public LedgersRequestBuilder ledgers() {
        return new LedgersRequestBuilder(httpClient, serverURI);
    }

    /**
     * Returns {@link OffersRequestBuilder} instance.
     */
    public OffersRequestBuilder offers() {
        return new OffersRequestBuilder(httpClient, serverURI);
    }

    /**
     * Returns {@link OperationsRequestBuilder} instance.
     */
    public OperationsRequestBuilder operations() {
        return new OperationsRequestBuilder(httpClient, serverURI);
    }

    /**
     * Returns {@link OrderBookRequestBuilder} instance.
     */
    public OrderBookRequestBuilder orderBook() {
        return new OrderBookRequestBuilder(httpClient, serverURI);
    }

    /**
     * Returns {@link TradesRequestBuilder} instance.
     */
    public TradesRequestBuilder trades() {
        return new TradesRequestBuilder(httpClient, serverURI);
    }

    /**
     * Returns {@link TradeAggregationsRequestBuilder} instance.
     */
    public TradeAggregationsRequestBuilder tradeAggregations(Asset baseAsset, Asset counterAsset, long startTime, long endTime, long resolution) {
        return new TradeAggregationsRequestBuilder(httpClient, serverURI, baseAsset, counterAsset, startTime, endTime, resolution);
    }

    /**
     * Returns {@link PathsRequestBuilder} instance.
     */
    public PathsRequestBuilder paths() {
        return new PathsRequestBuilder(httpClient, serverURI);
    }

    /**
     * Returns {@link PaymentsRequestBuilder} instance.
     */
    public PaymentsRequestBuilder payments() {
        return new PaymentsRequestBuilder(httpClient, serverURI);
    }

    /**
     * Returns {@link TransactionsRequestBuilder} instance.
     */
    public TransactionsRequestBuilder transactions() {
        return new TransactionsRequestBuilder(httpClient, serverURI);
    }

    /**
     * Submits transaction to the network.
     * @param transaction transaction to submit to the network.
     * @return {@link SubmitTransactionResponse}
     * @throws IOException
     */
    public SubmitTransactionResponse submitTransaction(Transaction transaction) throws IOException {
        HttpUrl transactionsURI = serverURI.newBuilder().addPathSegment("transactions").build();
        RequestBody requestBody = new FormBody.Builder().add("tx", transaction.toEnvelopeXdrBase64()).build();
        Request submitTransactionRequest = new Request.Builder().url(transactionsURI).post(requestBody).build();

        Response response = null;
        try {
            response = this.httpClient.newCall(submitTransactionRequest).execute();
            SubmitTransactionResponse submitTransactionResponse = GsonSingleton.getInstance().fromJson(response.body().string(), SubmitTransactionResponse.class);
            return submitTransactionResponse;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
