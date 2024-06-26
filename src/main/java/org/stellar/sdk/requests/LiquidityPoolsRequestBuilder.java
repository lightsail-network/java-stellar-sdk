package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.Util;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.responses.LiquidityPoolResponse;
import org.stellar.sdk.responses.Page;

/** Builds requests connected to liquidity pools. */
public class LiquidityPoolsRequestBuilder extends RequestBuilder {
  private static final String RESERVES_PARAMETER_NAME = "reserves";
  private static final String ACCOUNT_PARAMETER_NAME = "account";

  public LiquidityPoolsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "liquidity_pools");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link LiquidityPoolResponse}. This method is
   * helpful for getting the links.
   *
   * @return {@link LiquidityPoolResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public LiquidityPoolResponse liquidityPool(HttpUrl uri) {
    TypeToken<LiquidityPoolResponse> type = new TypeToken<LiquidityPoolResponse>() {};
    return Util.executeGetRequest(httpClient, uri, type);
  }

  /**
   * Requests <code>GET /liquidity_pools/{liquidity_pool_id}</code>
   *
   * @param liquidityPoolID Liquidity Pool to fetch
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
  public LiquidityPoolResponse liquidityPool(String liquidityPoolID) {
    this.setSegments("liquidity_pools", liquidityPoolID);
    return this.liquidityPool(this.buildUri());
  }

  /**
   * Requests <code>GET /liquidity_pools/{liquidity_pool_id}</code>
   *
   * @param liquidityPoolID Liquidity Pool to fetch
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
  public LiquidityPoolResponse liquidityPool(LiquidityPoolID liquidityPoolID) {
    return this.liquidityPool(liquidityPoolID.toString());
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
   * @param httpClient {@link OkHttpClient} to use to send the request.
   * @param uri {@link HttpUrl} URI to send the request to.
   * @return {@link Page} of {@link LiquidityPoolResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public static Page<LiquidityPoolResponse> execute(OkHttpClient httpClient, HttpUrl uri) {
    TypeToken<Page<LiquidityPoolResponse>> type = new TypeToken<Page<LiquidityPoolResponse>>() {};
    return Util.executeGetRequest(httpClient, uri, type);
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
        httpClient, this, LiquidityPoolResponse.class, listener, reconnectTimeout);
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
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
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
