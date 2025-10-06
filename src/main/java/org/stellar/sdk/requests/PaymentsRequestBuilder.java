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

/** Builds requests connected to payments. */
public class PaymentsRequestBuilder extends RequestBuilder {
  protected Set<String> toJoin;

  public PaymentsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "payments");
    toJoin = new HashSet<>();
  }

  /**
   * Builds request to <code>GET /accounts/{account}/payments</code>
   *
   * @see <a
   *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/get-payments-by-account-id">Payments
   *     for Account</a>
   * @param account Account for which to get payments
   */
  public PaymentsRequestBuilder forAccount(@NonNull String account) {
    this.setSegments("accounts", account, "payments");
    return this;
  }

  /**
   * Builds request to <code>GET /ledgers/{ledgerSeq}/payments</code>
   *
   * @see <a
   *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/retrieve-a-ledgers-payments">Payments
   *     for Ledger</a>
   * @param ledgerSeq Ledger for which to get payments
   */
  public PaymentsRequestBuilder forLedger(long ledgerSeq) {
    this.setSegments("ledgers", String.valueOf(ledgerSeq), "payments");
    return this;
  }

  /**
   * Builds request to <code>GET /transactions/{transactionId}/payments</code>
   *
   * @see <a
   *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/retrieve-a-transactions-payments">Payments
   *     for Transaction</a>
   * @param transactionId Transaction ID for which to get payments
   */
  public PaymentsRequestBuilder forTransaction(@NonNull String transactionId) {
    this.setSegments("transactions", transactionId, "payments");
    return this;
  }

  /**
   * Adds a parameter defining whether to include transactions in the response. By default
   * transaction data is not included.
   *
   * @param include Set to <code>true</code> to include transaction data in the operations response.
   */
  public PaymentsRequestBuilder includeTransactions(boolean include) {
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
   * @see <a
   *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/structure/response-format"
   *     target="_blank">Response Format documentation</a>
   * @param listener {@link EventListener} implementation with {@link OperationResponse} type
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
  public Page<OperationResponse> execute() {
    return execute(this.httpClient, this.buildUri());
  }

  @Override
  public PaymentsRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public PaymentsRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public PaymentsRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
