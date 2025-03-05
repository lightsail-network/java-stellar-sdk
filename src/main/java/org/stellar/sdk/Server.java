package org.stellar.sdk;

import com.google.gson.reflect.TypeToken;
import java.io.Closeable;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.stellar.sdk.exception.*;
import org.stellar.sdk.http.IHttpClient;
import org.stellar.sdk.http.Jdk11HttpClient;
import org.stellar.sdk.http.PostRequest;
import org.stellar.sdk.http.StringResponse;
import org.stellar.sdk.http.sse.ISseClient;
import org.stellar.sdk.operations.*;
import org.stellar.sdk.requests.*;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.FeeStatsResponse;
import org.stellar.sdk.responses.SubmitTransactionAsyncResponse;
import org.stellar.sdk.responses.TransactionResponse;
import org.stellar.sdk.xdr.CryptoKeyType;

/** Main class used to connect to Horizon server. */
public class Server implements Closeable {
  private final URI serverURI;
  @Getter @Setter private IHttpClient httpClient;

  /** submitHttpClient is used only for submitting transactions. The read timeout is longer. */
  @Getter @Setter private IHttpClient submitHttpClient;

  @Getter @Setter private ISseClient sseClient;

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
    this(uri, normalHttpClient(), submitHttpClient());
  }

  private static IHttpClient normalHttpClient() {
    return new Jdk11HttpClient.Builder()
        .withDefaultHeader("X-Client-Name", "java-stellar-sdk")
        .withDefaultHeader("X-Client-Version", Util.getSdkVersion())
        .withConnectTimeout(10, ChronoUnit.SECONDS)
        .withReadTimeout(30, ChronoUnit.SECONDS)
        .withRetryOnConnectionFailure(true)
        .build();
  }

  private static IHttpClient submitHttpClient() {
    return new Jdk11HttpClient.Builder()
        .withDefaultHeader("X-Client-Name", "java-stellar-sdk")
        .withDefaultHeader("X-Client-Version", Util.getSdkVersion())
        .withConnectTimeout(10, ChronoUnit.SECONDS)
        .withReadTimeout(HORIZON_SUBMIT_TIMEOUT + 5, ChronoUnit.SECONDS)
        .withRetryOnConnectionFailure(true)
        .build();
  }

  /**
   * Constructs a new Server object with custom HTTP clients.
   *
   * @param serverURI The URI of the Horizon server.
   * @param httpClient The IHttpClient to use for general requests.
   * @param submitHttpClient The IHttpClient to use for submitting transactions.
   */
  public Server(String serverURI, IHttpClient httpClient, IHttpClient submitHttpClient) {
    try {
      this.serverURI = new URI(serverURI);
    } catch (URISyntaxException e) {
      throw new RuntimeException("Invalid URI: " + serverURI);
    }

    this.httpClient = httpClient;
    this.submitHttpClient = submitHttpClient;
  }

  /**
   * Fetches an account's most current state in the ledger, then creates and returns an {@link
   * Account} object.
   *
   * @param address The address of the account to load, muxed accounts are supported.
   * @return {@link Account} object
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
   */
  public TransactionBuilderAccount loadAccount(String address) {
    MuxedAccount muxedAccount = new MuxedAccount(address);
    AccountResponse accountResponse = this.accounts().account(muxedAccount.getAccountId());
    return new Account(address, accountResponse.getSequenceNumber());
  }

  /**
   * @return {@link RootRequestBuilder} instance.
   */
  public RootRequestBuilder root() {
    return new RootRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link AccountsRequestBuilder} instance.
   */
  public AccountsRequestBuilder accounts() {
    return new AccountsRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link AssetsRequestBuilder} instance.
   */
  public AssetsRequestBuilder assets() {
    return new AssetsRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link ClaimableBalancesRequestBuilder} instance.
   */
  public ClaimableBalancesRequestBuilder claimableBalances() {
    return new ClaimableBalancesRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link EffectsRequestBuilder} instance.
   */
  public EffectsRequestBuilder effects() {
    return new EffectsRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link LedgersRequestBuilder} instance.
   */
  public LedgersRequestBuilder ledgers() {
    return new LedgersRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link OffersRequestBuilder} instance.
   */
  public OffersRequestBuilder offers() {
    return new OffersRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link OperationsRequestBuilder} instance.
   */
  public OperationsRequestBuilder operations() {
    return new OperationsRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link FeeStatsResponse} instance.
   */
  public FeeStatsRequestBuilder feeStats() {
    return new FeeStatsRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link OrderBookRequestBuilder} instance.
   */
  public OrderBookRequestBuilder orderBook() {
    return new OrderBookRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link TradesRequestBuilder} instance.
   */
  public TradesRequestBuilder trades() {
    return new TradesRequestBuilder(httpClient, sseClient, serverURI);
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
        httpClient,
        sseClient,
        serverURI,
        baseAsset,
        counterAsset,
        startTime,
        endTime,
        resolution,
        offset);
  }

  /**
   * @return {@link StrictReceivePathsRequestBuilder} instance.
   */
  public StrictReceivePathsRequestBuilder strictReceivePaths() {
    return new StrictReceivePathsRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link StrictSendPathsRequestBuilder} instance.
   */
  public StrictSendPathsRequestBuilder strictSendPaths() {
    return new StrictSendPathsRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link PaymentsRequestBuilder} instance.
   */
  public PaymentsRequestBuilder payments() {
    return new PaymentsRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link TransactionsRequestBuilder} instance.
   */
  public TransactionsRequestBuilder transactions() {
    return new TransactionsRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * @return {@link LiquidityPoolsRequestBuilder} instance.
   */
  public LiquidityPoolsRequestBuilder liquidityPools() {
    return new LiquidityPoolsRequestBuilder(httpClient, sseClient, serverURI);
  }

  /**
   * Submits a base64 encoded transaction envelope to the network
   *
   * @param transactionXdr base64 encoded transaction envelope to submit to the network
   * @return {@link TransactionResponse}
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
   */
  public TransactionResponse submitTransactionXdr(String transactionXdr) {
    final var transactionsURI = new UriBuilder(serverURI).addPathSegment("transactions").build();
    final var form = Map.of("tx", transactionXdr);
    final var post = PostRequest.formBody(transactionsURI, form);
    TypeToken<TransactionResponse> type = new TypeToken<TransactionResponse>() {};
    ResponseHandler<TransactionResponse> responseHandler = new ResponseHandler<>(type);
    StringResponse response;
    try {
      response = this.submitHttpClient.post(post);
    } catch (SocketTimeoutException e) {
      throw new RequestTimeoutException(e);
    } catch (IOException e) {
      if (e.getMessage().contains("request timed out")) {
        throw new RequestTimeoutException(e);
      } else {
        throw new ConnectionErrorException(e);
      }
    }
    return responseHandler.handleResponse(response);
  }

  /**
   * Submits a transaction to the network
   *
   * @param transaction transaction to submit to the network
   * @param skipMemoRequiredCheck set to true to skip memoRequiredCheck
   * @return {@link TransactionResponse}
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
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
   * @return {@link TransactionResponse}
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
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
   * @return {@link TransactionResponse}
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
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
   * @return {@link TransactionResponse}
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
   */
  public TransactionResponse submitTransaction(FeeBumpTransaction transaction) {
    return submitTransaction(transaction, false);
  }

  /**
   * Submits a base64 asynchronous transaction to the network. Unlike the synchronous version, which
   * blocks and waits for the transaction to be ingested in Horizon, this endpoint relays the
   * response from core directly back to the user.
   *
   * @param transactionXdr base64 encoded transaction envelope to submit to the network
   * @return {@link SubmitTransactionAsyncResponse}
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
   * @see <a
   *     href="https://developers.stellar.org/docs/data/horizon/api-reference/submit-async-transaction">Submit
   *     a Transaction Asynchronously</a>
   */
  public SubmitTransactionAsyncResponse submitTransactionXdrAsync(String transactionXdr) {
    final var transactionsURI =
        new UriBuilder(serverURI).addPathSegment("transactions_async").build();
    final var form = Map.of("tx", transactionXdr);
    final var post = PostRequest.formBody(transactionsURI, form);
    TypeToken<SubmitTransactionAsyncResponse> type =
        new TypeToken<SubmitTransactionAsyncResponse>() {};

    ResponseHandler<SubmitTransactionAsyncResponse> responseHandler = new ResponseHandler<>(type);
    StringResponse response;
    try {
      response = this.submitHttpClient.post(post);
    } catch (SocketTimeoutException e) {
      throw new RequestTimeoutException(e);
    } catch (IOException e) {
      throw new ConnectionErrorException(e);
    }
    return responseHandler.handleResponse(response, true);
  }

  /**
   * Submits a base64 asynchronous transaction to the network. Unlike the synchronous version, which
   * blocks and waits for the transaction to be ingested in Horizon, this endpoint relays the
   * response from core directly back to the user.
   *
   * @param transaction transaction to submit to the network
   * @param skipMemoRequiredCheck set to true to skip memoRequiredCheck
   * @return {@link TransactionResponse}
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
   * @see <a
   *     href="https://developers.stellar.org/docs/data/horizon/api-reference/submit-async-transaction">Submit
   *     a Transaction Asynchronously</a>
   */
  public SubmitTransactionAsyncResponse submitTransactionAsync(
      Transaction transaction, boolean skipMemoRequiredCheck) {
    if (!skipMemoRequiredCheck) {
      checkMemoRequired(transaction);
    }
    return this.submitTransactionXdrAsync(transaction.toEnvelopeXdrBase64());
  }

  /**
   * Submits a base64 asynchronous transaction to the network. Unlike the synchronous version, which
   * blocks and waits for the transaction to be ingested in Horizon, this endpoint relays the
   * response from core directly back to the user.
   *
   * @param transaction transaction to submit to the network
   * @param skipMemoRequiredCheck set to true to skip memoRequiredCheck
   * @return {@link SubmitTransactionAsyncResponse}
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
   * @see <a
   *     href="https://developers.stellar.org/docs/data/horizon/api-reference/submit-async-transaction">Submit
   *     a Transaction Asynchronously</a>
   */
  public SubmitTransactionAsyncResponse submitTransactionAsync(
      FeeBumpTransaction transaction, boolean skipMemoRequiredCheck) {
    if (!skipMemoRequiredCheck) {
      checkMemoRequired(transaction.getInnerTransaction());
    }
    return this.submitTransactionXdrAsync(transaction.toEnvelopeXdrBase64());
  }

  /**
   * Submits a base64 asynchronous transaction to the network. Unlike the synchronous version, which
   * blocks and waits for the transaction to be ingested in Horizon, this endpoint relays the
   * response from core directly back to the user.
   *
   * <p>This function will always check if the destination account requires a memo in the
   * transaction as defined in <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0029.md"
   * target="_blank">SEP-0029</a> If you want to skip this check, use {@link
   * Server#submitTransactionAsync(Transaction, boolean)}.
   *
   * @param transaction transaction to submit to the network.
   * @return {@link SubmitTransactionAsyncResponse}
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
   * @see <a
   *     href="https://developers.stellar.org/docs/data/horizon/api-reference/submit-async-transaction">Submit
   *     a Transaction Asynchronously</a>
   */
  public SubmitTransactionAsyncResponse submitTransactionAsync(Transaction transaction) {
    return submitTransactionAsync(transaction, false);
  }

  /**
   * Submits a base64 asynchronous transaction to the network. Unlike the synchronous version, which
   * blocks and waits for the transaction to be ingested in Horizon, this endpoint relays the
   * response from core directly back to the user.
   *
   * <p>This function will always check if the destination account requires a memo in the
   * transaction as defined in <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0029.md"
   * target="_blank">SEP-0029</a> If you want to skip this check, use {@link
   * Server#submitTransactionAsync(Transaction, boolean)}.
   *
   * @param transaction transaction to submit to the network.
   * @return {@link SubmitTransactionAsyncResponse}
   * @throws AccountRequiresMemoException when a transaction is trying to submit an operation to an
   *     account which requires a memo.
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
   * @see <a
   *     href="https://developers.stellar.org/docs/data/horizon/api-reference/submit-async-transaction">Submit
   *     a Transaction Asynchronously</a>
   */
  public SubmitTransactionAsyncResponse submitTransactionAsync(FeeBumpTransaction transaction) {
    return submitTransactionAsync(transaction, false);
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
    try {
      this.httpClient.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      this.submitHttpClient.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
