package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.http.IHttpClient;
import org.stellar.sdk.http.sse.ISseClient;
import org.stellar.sdk.responses.LiquidityPoolResponse;
import org.stellar.sdk.responses.Page;

/** Builds requests connected to liquidity pools. */
public class LiquidityPoolsRequestBuilder extends RequestBuilder {
  private static final String RESERVES_PARAMETER_NAME = "reserves";
  private static final String ACCOUNT_PARAMETER_NAME = "account";

  public LiquidityPoolsRequestBuilder(IHttpClient httpClient, ISseClient sseClient, URI serverURI) {
    super(httpClient, sseClient, serverURI, "liquidity_pools");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link LiquidityPoolResponse}. This method is
   * helpful for getting the links.
   *
   * @return {@link LiquidityPoolResponse}
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
  public LiquidityPoolResponse liquidityPool(URI uri) {
    TypeToken<LiquidityPoolResponse> type = new TypeToken<LiquidityPoolResponse>() {};
    return executeGetRequest(httpClient, uri, type);
  }

  /**
   * Requests <code>GET /liquidity_pools/{liquidity_pool_id}</code>
   *
   * @param liquidityPoolId Liquidity Pool to fetch
   * @return {@link LiquidityPoolResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @see <a href="https://developers.stellar.org/api/resources/liquiditypools/single/">Liquidity
   *     Pool Details</a>
   */
  public LiquidityPoolResponse liquidityPool(String liquidityPoolId) {
    this.setSegments("liquidity_pools", liquidityPoolId);
    return this.liquidityPool(this.buildUri());
  }

  /**
   * Returns all liquidity pools that contain reserves in all specified assets.
   *
   * @param reserves Reserve assets to filter liquidity pools
   * @return current {@link LiquidityPoolsRequestBuilder} instance
   * @see <a
   *     href="https://developers.stellar.org/api/resources/liquiditypools/list/">LiquidityPools</a>
   */
  public LiquidityPoolsRequestBuilder forReserves(String... reserves) {
    uriBuilder.setQueryParameter(RESERVES_PARAMETER_NAME, String.join(",", reserves));
    return this;
  }

  /**
   * Returns all liquidity pools the specified account is participating in.
   *
   * @param account Account ID to filter liquidity pools
   * @return current {@link LiquidityPoolsRequestBuilder} instance
   * @see <a
   *     href="https://developers.stellar.org/api/resources/liquiditypools/list/">LiquidityPools</a>
   */
  public LiquidityPoolsRequestBuilder forAccount(String account) {
    uriBuilder.setQueryParameter(ACCOUNT_PARAMETER_NAME, account);
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link LiquidityPoolResponse}.
   * This method is helpful for getting the next set of results.
   *
   * @param httpClient {@link IHttpClient} to use to send the request.
   * @param uri {@link URI} URI to send the request to.
   * @return {@link Page} of {@link LiquidityPoolResponse}
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
  public static Page<LiquidityPoolResponse> execute(IHttpClient httpClient, URI uri) {
    TypeToken<Page<LiquidityPoolResponse>> type = new TypeToken<Page<LiquidityPoolResponse>>() {};
    return executeGetRequest(httpClient, uri, type);
  }

  /**
   * Allows to stream SSE events from horizon. Certain endpoints in Horizon can be called in
   * streaming mode using Server-Sent Events. This mode will keep the connection to horizon open and
   * horizon will continue to return responses as ledgers close.
   *
   * @param listener {@link EventListener} implementation with {@link LiquidityPoolResponse} type
   * @param reconnectTimeout Custom stream connection timeout in ms
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   * @see <a href="http://www.w3.org/TR/eventsource/" target="_blank">Server-Sent Events</a>
   * @see <a href="https://developers.stellar.org/api/introduction/response-format/"
   *     target="_blank">Response Format documentation</a>
   */
  public SSEStream<LiquidityPoolResponse> stream(
      final EventListener<LiquidityPoolResponse> listener, long reconnectTimeout) {
    return SSEStream.create(
        sseClient, this, LiquidityPoolResponse.class, listener, reconnectTimeout);
  }

  /**
   * An overloaded version of {@link #stream(EventListener, long)} with default reconnect timeout.
   */
  public SSEStream<LiquidityPoolResponse> stream(
      final EventListener<LiquidityPoolResponse> listener) {
    return stream(listener, SSEStream.DEFAULT_RECONNECT_TIMEOUT);
  }

  /**
   * Build and execute request. <strong>Warning!</strong> {@link LiquidityPoolResponse}s in {@link
   * Page} will contain only <code>keypair</code> field.
   *
   * @return {@link Page} of {@link LiquidityPoolResponse}
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
  public Page<LiquidityPoolResponse> execute() {
    return execute(this.httpClient, this.buildUri());
  }

  @Override
  public LiquidityPoolsRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public LiquidityPoolsRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public LiquidityPoolsRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
