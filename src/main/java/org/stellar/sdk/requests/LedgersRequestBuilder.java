package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.Util;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.responses.LedgerResponse;
import org.stellar.sdk.responses.Page;

/** Builds requests connected to ledgers. */
public class LedgersRequestBuilder extends RequestBuilder {
  public LedgersRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "ledgers");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link LedgerResponse}. This method is helpful
   * for getting the links.
   *
   * @return {@link LedgerResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public LedgerResponse ledger(HttpUrl uri) {
    TypeToken<LedgerResponse> type = new TypeToken<LedgerResponse>() {};
    return Util.executeGetRequest(httpClient, uri, type);
  }

  /**
   * Requests <code>GET /ledgers/{ledgerSeq}</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/ledgers/single/">Ledger Details</a>
   * @param ledgerSeq Ledger to fetch
   * @return {@link LedgerResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public LedgerResponse ledger(long ledgerSeq) {
    this.setSegments("ledgers", String.valueOf(ledgerSeq));
    return this.ledger(this.buildUri());
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link LedgerResponse}. This
   * method is helpful for getting the next set of results.
   *
   * @param httpClient {@link OkHttpClient} to use to send the request.
   * @param uri {@link HttpUrl} URI to send the request to.
   * @return {@link Page} of {@link LedgerResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public static Page<LedgerResponse> execute(OkHttpClient httpClient, HttpUrl uri) {
    TypeToken<Page<LedgerResponse>> type = new TypeToken<Page<LedgerResponse>>() {};
    return Util.executeGetRequest(httpClient, uri, type);
  }

  /**
   * Allows to stream SSE events from horizon. Certain endpoints in Horizon can be called in
   * streaming mode using Server-Sent Events. This mode will keep the connection to horizon open and
   * horizon will continue to return responses as ledgers close.
   *
   * @see <a href="http://www.w3.org/TR/eventsource/" target="_blank">Server-Sent Events</a>
   * @see <a href="https://developers.stellar.org/api/introduction/response-format/"
   *     target="_blank">Response Format documentation</a>
   * @param listener {@link EventListener} implementation with {@link LedgerResponse} type
   * @param reconnectTimeout Custom stream connection timeout in ms
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<LedgerResponse> stream(
      final EventListener<LedgerResponse> listener, long reconnectTimeout) {
    return SSEStream.create(httpClient, this, LedgerResponse.class, listener, reconnectTimeout);
  }

  /**
   * An overloaded version of {@link #stream(EventListener, long)} with default reconnect timeout.
   */
  public SSEStream<LedgerResponse> stream(final EventListener<LedgerResponse> listener) {
    return stream(listener, SSEStream.DEFAULT_RECONNECT_TIMEOUT);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link LedgerResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   */
  public Page<LedgerResponse> execute() {
    return execute(this.httpClient, this.buildUri());
  }

  @Override
  public LedgersRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public LedgersRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public LedgersRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
