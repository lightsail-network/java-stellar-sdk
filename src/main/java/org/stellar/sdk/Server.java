package org.stellar.sdk;

import com.google.gson.reflect.TypeToken;
import java.io.Closeable;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
import okhttp3.Response;
import org.stellar.sdk.exception.AccountRequiresMemoException;
import org.stellar.sdk.exception.BadRequestException;
import org.stellar.sdk.exception.ConnectionErrorException;
import org.stellar.sdk.exception.RequestTimeoutException;
import org.stellar.sdk.exception.UnknownResponseException;
import org.stellar.sdk.operations.AccountMergeOperation;
import org.stellar.sdk.operations.Operation;
import org.stellar.sdk.operations.PathPaymentStrictReceiveOperation;
import org.stellar.sdk.operations.PathPaymentStrictSendOperation;
import org.stellar.sdk.operations.PaymentOperation;
import org.stellar.sdk.requests.*;
import org.stellar.sdk.responses.*;
import org.stellar.sdk.xdr.CryptoKeyType;

/** Main class used to connect to Horizon server. */
public class Server implements Closeable {
  private final HttpUrl serverURI;
  @Getter @Setter private OkHttpClient httpClient;

  /** submitHttpClient is used only for submitting transactions. The read timeout is longer. */
  @Getter @Setter private OkHttpClient submitHttpClient;

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

  /**
   * Constructs a new Server object with default HTTP clients.
   *
   * @param uri The URI of the Horizon server.
   */
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

  /**
   * Constructs a new Server object with custom HTTP clients.
   *
   * @param serverURI The URI of the Horizon server.
   * @param httpClient The OkHttpClient to use for general requests.
   * @param submitHttpClient The OkHttpClient to use for submitting transactions.
   */
  public Server(String serverURI, OkHttpClient httpClient, OkHttpClient submitHttpClient) {
    this.serverURI = HttpUrl.parse(serverURI);
    this.httpClient = httpClient;
    this.submitHttpClient = submitHttpClient;
  }

  /**
   * @return {@link RootRequestBuilder} instance.
   */
  public RootRequestBuilder root() {
    return new RootRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link AccountsRequestBuilder} instance.
   */
  public AccountsRequestBuilder accounts() {
    return new AccountsRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link AssetsRequestBuilder} instance.
   */
  public AssetsRequestBuilder assets() {
    return new AssetsRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link ClaimableBalancesRequestBuilder} instance.
   */
  public ClaimableBalancesRequestBuilder claimableBalances() {
    return new ClaimableBalancesRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link EffectsRequestBuilder} instance.
   */
  public EffectsRequestBuilder effects() {
    return new EffectsRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link LedgersRequestBuilder} instance.
   */
  public LedgersRequestBuilder ledgers() {
    return new LedgersRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link OffersRequestBuilder} instance.
   */
  public OffersRequestBuilder offers() {
    return new OffersRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link OperationsRequestBuilder} instance.
   */
  public OperationsRequestBuilder operations() {
    return new OperationsRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link FeeStatsResponse} instance.
   */
  public FeeStatsRequestBuilder feeStats() {
    return new FeeStatsRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link OrderBookRequestBuilder} instance.
   */
  public OrderBookRequestBuilder orderBook() {
    return new OrderBookRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link TradesRequestBuilder} instance.
   */
  public TradesRequestBuilder trades() {
    return new TradesRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link TradeAggregationsRequestBuilder} instance.
   */
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

  /**
   * @return {@link StrictReceivePathsRequestBuilder} instance.
   */
  public StrictReceivePathsRequestBuilder strictReceivePaths() {
    return new StrictReceivePathsRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link StrictSendPathsRequestBuilder} instance.
   */
  public StrictSendPathsRequestBuilder strictSendPaths() {
    return new StrictSendPathsRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link PaymentsRequestBuilder} instance.
   */
  public PaymentsRequestBuilder payments() {
    return new PaymentsRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link TransactionsRequestBuilder} instance.
   */
  public TransactionsRequestBuilder transactions() {
    return new TransactionsRequestBuilder(httpClient, serverURI);
  }

  /**
   * @return {@link LiquidityPoolsRequestBuilder} instance.
   */
  public LiquidityPoolsRequestBuilder liquidityPools() {
    return new LiquidityPoolsRequestBuilder(httpClient, serverURI);
  }

  /**
   * Submits a base64 encoded transaction envelope to the network
   *
   * @param transactionXdr base64 encoded transaction envelope to submit to the network
   * @return {@link SubmitTransactionResponse}
   * @throws RequestTimeoutException When Horizon returns a <code>Timeout</code> or connection
   *     timeout occured.
   * @throws UnknownResponseException When unknown Horizon response is returned.
   * @throws IOException
   */
  public TransactionResponse submitTransactionXdr(String transactionXdr) {
    HttpUrl transactionsURI = serverURI.newBuilder().addPathSegment("transactions").build();
    RequestBody requestBody = new FormBody.Builder().add("tx", transactionXdr).build();
    Request submitTransactionRequest =
        new Request.Builder().url(transactionsURI).post(requestBody).build();
    TypeToken<TransactionResponse> type = new TypeToken<TransactionResponse>() {};

    ResponseHandler<TransactionResponse> responseHandler = new ResponseHandler<>(type);
    Response response;
    try {
      response = this.submitHttpClient.newCall(submitTransactionRequest).execute();
    } catch (SocketTimeoutException e) {
      throw new RequestTimeoutException(e);
    } catch (IOException e) {
      throw new ConnectionErrorException(e);
    }
    return responseHandler.handleResponse(response);
  }

  /**
   * Submits a transaction to the network
   *
   * @param transaction transaction to submit to the network
   * @param skipMemoRequiredCheck set to true to skip memoRequiredCheck
   * @return {@link SubmitTransactionResponse}
   * @throws RequestTimeoutException When Horizon returns a <code>Timeout</code> or connection
   *     timeout occured.
   * @throws UnknownResponseException When unknown Horizon response is returned.
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws IOException
   */
  public TransactionResponse submitTransaction(
      Transaction transaction, boolean skipMemoRequiredCheck) {
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
   * @throws RequestTimeoutException When Horizon returns a <code>Timeout</code> or connection
   *     timeout occured.
   * @throws UnknownResponseException When unknown Horizon response is returned.
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws IOException
   */
  public TransactionResponse submitTransaction(
      FeeBumpTransaction transaction, boolean skipMemoRequiredCheck) {
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
   * @throws RequestTimeoutException When Horizon returns a <code>Timeout</code> or connection
   *     timeout occured.
   * @throws UnknownResponseException When unknown Horizon response is returned.
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws IOException
   */
  public TransactionResponse submitTransaction(Transaction transaction) {
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
   * @throws RequestTimeoutException When Horizon returns a <code>Timeout</code> or connection
   *     timeout occured.
   * @throws UnknownResponseException When unknown Horizon response is returned.
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws IOException
   */
  public TransactionResponse submitTransaction(FeeBumpTransaction transaction) {
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
  private void checkMemoRequired(Transaction transaction) {
    if (!transaction.getMemo().equals(Memo.none())) {
      return;
    }
    Set<String> destinations = new HashSet<>();
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
      } catch (BadRequestException e) {
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
