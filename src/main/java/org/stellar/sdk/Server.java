package org.stellar.sdk;

import com.google.common.base.Optional;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import okhttp3.Response;
import org.stellar.sdk.requests.*;
import org.stellar.sdk.responses.*;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Main class used to connect to Horizon server.
 */
public class Server implements Closeable {
    private HttpUrl serverURI;
    private OkHttpClient httpClient;
    private Optional<Network> network;
    private ReentrantReadWriteLock networkLock;
    /**
     * submitHttpClient is used only for submitting transactions. The read timeout is longer.
     */
    private OkHttpClient submitHttpClient;

    /**
     * HORIZON_SUBMIT_TIMEOUT is a time in seconds after Horizon sends a timeout response
     * after internal txsub timeout.
     */
    private static final int HORIZON_SUBMIT_TIMEOUT = 60;

    public Server(String uri) {
        this(
            uri,
            new OkHttpClient.Builder()
                .addInterceptor(new ClientIdentificationInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build(),
            new OkHttpClient.Builder()
                .addInterceptor(new ClientIdentificationInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(HORIZON_SUBMIT_TIMEOUT + 5, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()
        );

    }

    public Server(
            String serverURI,
            OkHttpClient httpClient,
            OkHttpClient submitHttpClient
    ) {
        this.serverURI = HttpUrl.parse(serverURI);
        this.httpClient = httpClient;
        this.submitHttpClient = submitHttpClient;
        this.network = Optional.absent();
        this.networkLock = new ReentrantReadWriteLock();
    }


    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public OkHttpClient getSubmitHttpClient() {
        return submitHttpClient;
    }

    public void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setSubmitHttpClient(OkHttpClient submitHttpClient) {
        this.submitHttpClient = submitHttpClient;
    }

    /**
     * Returns {@link RootResponse}.
     */
    public RootResponse root() throws IOException {
        TypeToken type = new TypeToken<RootResponse>() {};
        ResponseHandler<RootResponse> responseHandler = new ResponseHandler<RootResponse>(type);

        Request request = new Request.Builder().get().url(serverURI).build();
        Response response = httpClient.newCall(request).execute();

        RootResponse parsedResponse = responseHandler.handleResponse(response);

        this.setNetwork(new Network(parsedResponse.getNetworkPassphrase()));
        return parsedResponse;
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
     * Returns {@link FeeStatsResponse} instance.
     */
    public FeeStatsRequestBuilder feeStats() {
        return new FeeStatsRequestBuilder(httpClient, serverURI);
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
    public TradeAggregationsRequestBuilder tradeAggregations(Asset baseAsset, Asset counterAsset, long startTime, long endTime, long resolution, long offset) {
        return new TradeAggregationsRequestBuilder(httpClient, serverURI, baseAsset, counterAsset, startTime, endTime, resolution, offset);
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

    private Optional<Network> getNetwork() {
        Lock readLock = this.networkLock.readLock();
        readLock.lock();
        try {
            return this.network;
        } finally {
            readLock.unlock();
        }
    }

    private void setNetwork(Network network) {
        Lock writeLock = this.networkLock.writeLock();
        writeLock.lock();
        try {
            this.network = Optional.of(network);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Submits transaction to the network.
     * @param transaction transaction to submit to the network.
     * @return {@link SubmitTransactionResponse}
     * @throws SubmitTransactionTimeoutResponseException When Horizon returns a <code>Timeout</code> or connection timeout occured.
     * @throws SubmitTransactionUnknownResponseException When unknown Horizon response is returned.
     * @throws IOException
     */
    public SubmitTransactionResponse submitTransaction(Transaction transaction) throws IOException {
        Optional<Network> network = getNetwork();
        if (!network.isPresent()) {
            this.root();
        }

        network = getNetwork();

        if (!network.get().equals(transaction.getNetwork())) {
            throw new NetworkMismatchException(network.get(), transaction.getNetwork());

        }
        HttpUrl transactionsURI = serverURI.newBuilder().addPathSegment("transactions").build();
        RequestBody requestBody = new FormBody.Builder().add("tx", transaction.toEnvelopeXdrBase64()).build();
        Request submitTransactionRequest = new Request.Builder().url(transactionsURI).post(requestBody).build();

        Response response = null;
        SubmitTransactionResponse submitTransactionResponse = null;
        try {
            response = this.submitHttpClient.newCall(submitTransactionRequest).execute();
            switch (response.code()) {
                case 200:
                case 400:
                    submitTransactionResponse = GsonSingleton.getInstance().fromJson(response.body().string(), SubmitTransactionResponse.class);
                    break;
                case 504:
                    throw new SubmitTransactionTimeoutResponseException();
                default:
                    throw new SubmitTransactionUnknownResponseException(response.code(), response.body().string());
            }
        } catch (SocketTimeoutException e) {
            throw new SubmitTransactionTimeoutResponseException();
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return submitTransactionResponse;
    }

    @Override
    public void close() {
        // workaround for https://github.com/square/okhttp/issues/3372
        // sometimes, the connection pool keeps running and this can prevent a clean shut down.
        this.httpClient.connectionPool().evictAll();
        this.submitHttpClient.connectionPool().evictAll();
    }
}
