package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.util.HashSet;
import java.util.Set;
import lombok.NonNull;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.operations.OperationResponse;

/** Builds requests connected to operations. */
public class OperationsRequestBuilder extends RequestBuilder {
  protected Set<String> toJoin;

  public OperationsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "operations");
    toJoin = new HashSet<>();
  }

  /**
   * Requests specific <code>uri</code> and returns {@link OperationResponse}. This method is
   * helpful for getting the links.
   *
   * @return {@link OperationResponse}
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
  public OperationResponse operation(HttpUrl uri) {
    TypeToken<OperationResponse> type = new TypeToken<OperationResponse>() {};
    return executeGetRequest(httpClient, uri, type);
  }

  /**
   * Requests <code>GET /operations/{operationId}</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/operations/single/">Operation
   *     Details</a>
   * @param operationId Operation to fetch
   * @return {@link OperationResponse}
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
  public OperationResponse operation(long operationId) {
    this.setSegments("operations", String.valueOf(operationId));
    return this.operation(this.buildUri());
  }

  /**
   * Builds request to <code>GET /accounts/{account}/operations</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/accounts/operations/">Operations for
   *     Account</a>
   * @param account Account for which to get operations
   */
  public OperationsRequestBuilder forAccount(@NonNull String account) {
    this.setSegments("accounts", account, "operations");
    return this;
  }

  /**
   * Builds request to <code>GET /claimable_balances/{claimable_balance_id}/operations</code>
   *
   * @see <a
   *     href="https://developers.stellar.org/api/resources/claimablebalances/operations/">Operations
   *     for ClaimableBalance</a>
   * @param claimableBalance Claimable Balance for which to get operations
   */
  public OperationsRequestBuilder forClaimableBalance(@NonNull String claimableBalance) {
    this.setSegments("claimable_balances", claimableBalance, "operations");
    return this;
  }

  /**
   * Builds request to <code>GET /ledgers/{ledgerSeq}/operations</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/ledgers/operations/">Operations for
   *     Ledger</a>
   * @param ledgerSeq Ledger for which to get operations
   */
  public OperationsRequestBuilder forLedger(long ledgerSeq) {
    this.setSegments("ledgers", String.valueOf(ledgerSeq), "operations");
    return this;
  }

  /**
   * Builds request to <code>GET /transactions/{transactionId}/operations</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/ledgers/transactions/">Operations
   *     for Transaction</a>
   * @param transactionId Transaction ID for which to get operations
   */
  public OperationsRequestBuilder forTransaction(@NonNull String transactionId) {
    this.setSegments("transactions", transactionId, "operations");
    return this;
  }

  /**
   * Builds request to <code>GET /liquidity_pools/{poolID}/operations</code>
   *
   * @see <a
   *     href="https://developers.stellar.org/api/resources/liquiditypools/operations/">Operations
   *     for Liquidity Pool</a>
   * @param liquidityPoolID Liquidity pool for which to get operations
   */
  public OperationsRequestBuilder forLiquidityPool(String liquidityPoolID) {
    this.setSegments("liquidity_pools", liquidityPoolID, "operations");
    return this;
  }

  /**
   * Adds a parameter defining whether to include operations of failed transactions. By default only
   * operations of successful transactions are returned.
   *
   * @param value Set to <code>true</code> to include operations of failed transactions.
   */
  public OperationsRequestBuilder includeFailed(boolean value) {
    uriBuilder.setQueryParameter("include_failed", String.valueOf(value));
    return this;
  }

  /**
   * Adds a parameter defining whether to include transactions in the response. By default
   * transaction data is not included.
   *
   * @param include Set to <code>true</code> to include transaction data in the operations response.
   */
  public OperationsRequestBuilder includeTransactions(boolean include) {
    updateToJoin("transactions", include);
    return this;
  }

  protected void updateToJoin(String value, boolean include) {
    if (include) {
      toJoin.add(value);
    } else {
      toJoin.remove(value);
    }
    if (toJoin.isEmpty()) {
      uriBuilder.removeAllQueryParameters("join");
    } else {
      uriBuilder.setQueryParameter("join", String.join(",", toJoin));
    }
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link OperationResponse}. This
   * method is helpful for getting the next set of results.
   *
   * @param httpClient {@link OkHttpClient} to use to send the request.
   * @param uri {@link HttpUrl} URI to send the request to.
   * @return {@link Page} of {@link OperationResponse}
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
  public static Page<OperationResponse> execute(OkHttpClient httpClient, HttpUrl uri) {
    TypeToken<Page<OperationResponse>> type = new TypeToken<Page<OperationResponse>>() {};
    return executeGetRequest(httpClient, uri, type);
  }

  /**
   * Allows to stream SSE events from horizon. Certain endpoints in Horizon can be called in
   * streaming mode using Server-Sent Events. This mode will keep the connection to horizon open and
   * horizon will continue to return responses as ledgers close.
   *
   * @see <a href="http://www.w3.org/TR/eventsource/" target="_blank">Server-Sent Events</a>
   * @see <a href="https://developers.stellar.org/api/introduction/response-format/"
   *     target="_blank">Response Format documentation</a>
   * @param listener {@link OperationResponse} implementation with {@link OperationResponse} type
   * @param reconnectTimeout Custom stream connection timeout in ms
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<OperationResponse> stream(
      final EventListener<OperationResponse> listener, long reconnectTimeout) {
    return SSEStream.create(httpClient, this, OperationResponse.class, listener, reconnectTimeout);
  }

  /**
   * An overloaded version of {@link #stream(EventListener, long)} with default reconnect timeout.
   */
  public SSEStream<OperationResponse> stream(final EventListener<OperationResponse> listener) {
    return stream(listener, SSEStream.DEFAULT_RECONNECT_TIMEOUT);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link OperationResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   */
  public Page<OperationResponse> execute() {
    return execute(this.httpClient, this.buildUri());
  }

  @Override
  public OperationsRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public OperationsRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public OperationsRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
