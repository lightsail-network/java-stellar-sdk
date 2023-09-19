package org.stellar.sdk;

import com.google.common.base.Optional;
import com.google.gson.reflect.TypeToken;
import java.io.Closeable;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import okhttp3.*;
import okhttp3.Response;
import org.stellar.sdk.requests.*;
import org.stellar.sdk.responses.*;
import org.stellar.sdk.xdr.CryptoKeyType;

/** Main class used to connect to Horizon server. */
public class Server implements Closeable {
  private HttpUrl serverURI;
  private OkHttpClient httpClient;
  private Optional<Network> network;
  private ReentrantReadWriteLock networkLock;

  /** submitHttpClient is used only for submitting transactions. The read timeout is longer. */
  private OkHttpClient submitHttpClient;

  /**
   * HORIZON_SUBMIT_TIMEOUT is a time in seconds after Horizon sends a timeout response after
   * internal txsub timeout.
   */
  private static final int HORIZON_SUBMIT_TIMEOUT = 60;

  /**
   * ACCOUNT_REQUIRES_MEMO_VALUE is the base64 encoding of "1". SEP 29 uses this value to define
   * transaction memo requirements for incoming payments.
   */
  private static final String ACCOUNT_REQUIRES_MEMO_VALUE = "MQ==";

  /** ACCOUNT_REQUIRES_MEMO_KEY is the data name described in SEP 29. */
  private static final String ACCOUNT_REQUIRES_MEMO_KEY = "config.memo_required";

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
            .build());
  }

  public Server(String serverURI, OkHttpClient httpClient, OkHttpClient submitHttpClient) {
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

  /** Returns {@link RootResponse}. */
  public RootResponse root() throws IOException {
    TypeToken type = new TypeToken<RootResponse>() {};
    ResponseHandler<RootResponse> responseHandler = new ResponseHandler<RootResponse>(type);

    Request request = new Request.Builder().get().url(serverURI).build();
    Response response = httpClient.newCall(request).execute();

    RootResponse parsedResponse = responseHandler.handleResponse(response);

    this.setNetwork(new Network(parsedResponse.getNetworkPassphrase()));
    return parsedResponse;
  }

  /** Returns {@link AccountsRequestBuilder} instance. */
  public AccountsRequestBuilder accounts() {
    return new AccountsRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link AssetsRequestBuilder} instance. */
  public AssetsRequestBuilder assets() {
    return new AssetsRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link ClaimableBalancesRequestBuilder} instance. */
  public ClaimableBalancesRequestBuilder claimableBalances() {
    return new ClaimableBalancesRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link EffectsRequestBuilder} instance. */
  public EffectsRequestBuilder effects() {
    return new EffectsRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link LedgersRequestBuilder} instance. */
  public LedgersRequestBuilder ledgers() {
    return new LedgersRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link OffersRequestBuilder} instance. */
  public OffersRequestBuilder offers() {
    return new OffersRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link OperationsRequestBuilder} instance. */
  public OperationsRequestBuilder operations() {
    return new OperationsRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link FeeStatsResponse} instance. */
  public FeeStatsRequestBuilder feeStats() {
    return new FeeStatsRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link OrderBookRequestBuilder} instance. */
  public OrderBookRequestBuilder orderBook() {
    return new OrderBookRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link TradesRequestBuilder} instance. */
  public TradesRequestBuilder trades() {
    return new TradesRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link TradeAggregationsRequestBuilder} instance. */
  public TradeAggregationsRequestBuilder tradeAggregations(
      Asset baseAsset,
      Asset counterAsset,
      long startTime,
      long endTime,
      long resolution,
      long offset) {
    return new TradeAggregationsRequestBuilder(
        httpClient, serverURI, baseAsset, counterAsset, startTime, endTime, resolution, offset);
  }

  /** Returns {@link StrictReceivePathsRequestBuilder} instance. */
  public StrictReceivePathsRequestBuilder strictReceivePaths() {
    return new StrictReceivePathsRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link StrictSendPathsRequestBuilder} instance. */
  public StrictSendPathsRequestBuilder strictSendPaths() {
    return new StrictSendPathsRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link PaymentsRequestBuilder} instance. */
  public PaymentsRequestBuilder payments() {
    return new PaymentsRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link TransactionsRequestBuilder} instance. */
  public TransactionsRequestBuilder transactions() {
    return new TransactionsRequestBuilder(httpClient, serverURI);
  }

  /** Returns {@link LiquidityPoolsRequestBuilder} instance. */
  public LiquidityPoolsRequestBuilder liquidityPools() {
    return new LiquidityPoolsRequestBuilder(httpClient, serverURI);
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

  private void checkTransactionNetwork(AbstractTransaction transaction) throws IOException {
    Optional<Network> network = getNetwork();
    if (!network.isPresent()) {
      this.root();
    }

    network = getNetwork();
    if (!network.get().equals(transaction.getNetwork())) {
      throw new NetworkMismatchException(network.get(), transaction.getNetwork());
    }
  }

  /**
   * Submits a base64 encoded transaction envelope to the network
   *
   * @param transactionXdr base64 encoded transaction envelope to submit to the network
   * @return {@link SubmitTransactionResponse}
   * @throws SubmitTransactionTimeoutResponseException When Horizon returns a <code>Timeout</code>
   *     or connection timeout occured.
   * @throws SubmitTransactionUnknownResponseException When unknown Horizon response is returned.
   * @throws IOException
   */
  public SubmitTransactionResponse submitTransactionXdr(String transactionXdr) throws IOException {
    HttpUrl transactionsURI = serverURI.newBuilder().addPathSegment("transactions").build();
    RequestBody requestBody = new FormBody.Builder().add("tx", transactionXdr).build();
    Request submitTransactionRequest =
        new Request.Builder().url(transactionsURI).post(requestBody).build();

    Response response = null;
    SubmitTransactionResponse submitTransactionResponse = null;
    try {
      response = this.submitHttpClient.newCall(submitTransactionRequest).execute();
      switch (response.code()) {
        case 200:
        case 400:
          submitTransactionResponse =
              GsonSingleton.getInstance()
                  .fromJson(response.body().string(), SubmitTransactionResponse.class);
          break;
        case 504:
          throw new SubmitTransactionTimeoutResponseException();
        default:
          throw new SubmitTransactionUnknownResponseException(
              response.code(), response.body().string());
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

  /**
   * Submits a transaction to the network
   *
   * @param transaction transaction to submit to the network
   * @param skipMemoRequiredCheck set to true to skip memoRequiredCheck
   * @return {@link SubmitTransactionResponse}
   * @throws SubmitTransactionTimeoutResponseException When Horizon returns a <code>Timeout</code>
   *     or connection timeout occured.
   * @throws SubmitTransactionUnknownResponseException When unknown Horizon response is returned.
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws IOException
   */
  public SubmitTransactionResponse submitTransaction(
      Transaction transaction, boolean skipMemoRequiredCheck)
      throws IOException, AccountRequiresMemoException {
    this.checkTransactionNetwork(transaction);

    if (!skipMemoRequiredCheck) {
      checkMemoRequired(transaction);
    }

    return this.submitTransactionXdr(transaction.toEnvelopeXdrBase64());
  }

  /**
   * Submits a fee bump transaction to the network
   *
   * @param transaction transaction to submit to the network
   * @param skipMemoRequiredCheck set to true to skip memoRequiredCheck
   * @return {@link SubmitTransactionResponse}
   * @throws SubmitTransactionTimeoutResponseException When Horizon returns a <code>Timeout</code>
   *     or connection timeout occured.
   * @throws SubmitTransactionUnknownResponseException When unknown Horizon response is returned.
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws IOException
   */
  public SubmitTransactionResponse submitTransaction(
      FeeBumpTransaction transaction, boolean skipMemoRequiredCheck)
      throws IOException, AccountRequiresMemoException {
    this.checkTransactionNetwork(transaction);

    if (!skipMemoRequiredCheck) {
      checkMemoRequired(transaction.getInnerTransaction());
    }

    return this.submitTransactionXdr(transaction.toEnvelopeXdrBase64());
  }

  /**
   * Submits a transaction to the network
   *
   * <p>This function will always check if the destination account requires a memo in the
   * transaction as defined in <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0029.md"
   * target="_blank">SEP-0029</a> If you want to skip this check, use {@link
   * Server#submitTransaction(Transaction, boolean)}.
   *
   * @param transaction transaction to submit to the network.
   * @return {@link SubmitTransactionResponse}
   * @throws SubmitTransactionTimeoutResponseException When Horizon returns a <code>Timeout</code>
   *     or connection timeout occured.
   * @throws SubmitTransactionUnknownResponseException When unknown Horizon response is returned.
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws IOException
   */
  public SubmitTransactionResponse submitTransaction(Transaction transaction)
      throws IOException, AccountRequiresMemoException {
    return submitTransaction(transaction, false);
  }

  /**
   * Submits a fee bump transaction to the network
   *
   * <p>This function will always check if the destination account requires a memo in the
   * transaction as defined in <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0029.md"
   * target="_blank">SEP-0029</a> If you want to skip this check, use {@link
   * Server#submitTransaction(Transaction, boolean)}.
   *
   * @param transaction transaction to submit to the network.
   * @return {@link SubmitTransactionResponse}
   * @throws SubmitTransactionTimeoutResponseException When Horizon returns a <code>Timeout</code>
   *     or connection timeout occured.
   * @throws SubmitTransactionUnknownResponseException When unknown Horizon response is returned.
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws IOException
   */
  public SubmitTransactionResponse submitTransaction(FeeBumpTransaction transaction)
      throws IOException, AccountRequiresMemoException {
    return submitTransaction(transaction, false);
  }

  private boolean hashMemoId(String muxedAccount) {
    return StrKey.encodeToXDRMuxedAccount(muxedAccount).getDiscriminant()
        == CryptoKeyType.KEY_TYPE_MUXED_ED25519;
  }

  /**
   * checkMemoRequired implements a memo required check as defined in <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0029.md"
   * target="_blank">SEP-0029</a>
   *
   * @param transaction transaction to submit to the network.
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws IOException
   */
  private void checkMemoRequired(Transaction transaction)
      throws IOException, AccountRequiresMemoException {
    if (!(transaction.getMemo() == null || transaction.getMemo().equals(Memo.none()))) {
      return;
    }
    Set<String> destinations = new HashSet<String>();
    Operation[] operations = transaction.getOperations();
    for (int i = 0; i < operations.length; i++) {
      String destination;
      Operation operation = operations[i];
      if (operation instanceof PaymentOperation) {
        destination = ((PaymentOperation) operation).getDestination();
      } else if (operation instanceof PathPaymentStrictReceiveOperation) {
        destination = ((PathPaymentStrictReceiveOperation) operation).getDestination();
      } else if (operation instanceof PathPaymentStrictSendOperation) {
        destination = ((PathPaymentStrictSendOperation) operation).getDestination();
      } else if (operation instanceof AccountMergeOperation) {
        destination = ((AccountMergeOperation) operation).getDestination();
      } else {
        continue;
      }
      if (destinations.contains(destination) || hashMemoId(destination)) {
        continue;
      }

      destinations.add(destination);
      AccountResponse.Data data;
      try {
        data = this.accounts().account(destination).getData();
      } catch (ErrorResponse e) {
        if (e.getCode() == 404) {
          continue;
        }
        throw e;
      }
      if (ACCOUNT_REQUIRES_MEMO_VALUE.equals(data.get(ACCOUNT_REQUIRES_MEMO_KEY))) {
        throw new AccountRequiresMemoException(
            "Destination account requires a memo in the transaction.", destination, i);
      }
    }
  }

  @Override
  public void close() {
    // workaround for https://github.com/square/okhttp/issues/3372
    // sometimes, the connection pool keeps running and this can prevent a clean shut down.
    this.httpClient.connectionPool().evictAll();
    this.submitHttpClient.connectionPool().evictAll();
  }
}
