package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.TradeResponse;

/** Builds requests connected to trades. */
public class TradesRequestBuilder extends RequestBuilder {
  private static final String TRADE_TYPE_PARAMETER_NAME = "trade_type";

  public TradesRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "trades");
  }

  public TradesRequestBuilder baseAsset(Asset asset) {
    uriBuilder.setQueryParameter("base_asset_type", asset.getType());
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.setQueryParameter("base_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.setQueryParameter("base_asset_issuer", creditAlphaNumAsset.getIssuer());
    }
    return this;
  }

  public TradesRequestBuilder counterAsset(Asset asset) {
    uriBuilder.setQueryParameter("counter_asset_type", asset.getType());
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.setQueryParameter("counter_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.setQueryParameter("counter_asset_issuer", creditAlphaNumAsset.getIssuer());
    }
    return this;
  }

  /**
   * Builds request to <code>GET /accounts/{account}/trades</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/accounts/trades/">Trades for
   *     Account</a>
   * @param account Account for which to get trades
   */
  public TradesRequestBuilder forAccount(@NonNull String account) {
    this.setSegments("accounts", account, "trades");
    return this;
  }

  /**
   * Builds request to <code>GET /liquidity_pools/{poolID}/trades</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/liquiditypools/trades/">Trades for
   *     Liquidity Pool</a>
   * @param liquidityPoolID Liquidity pool for which to get trades
   */
  public TradesRequestBuilder forLiquidityPool(LiquidityPoolID liquidityPoolID) {
    return this.forLiquidityPool(String.valueOf(liquidityPoolID));
  }

  /**
   * Builds request to <code>GET /liquidity_pools/{poolID}/trades</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/liquiditypools/trades/">Trades for
   *     Liquidity Pool</a>
   * @param liquidityPoolID Liquidity pool for which to get trades
   */
  public TradesRequestBuilder forLiquidityPool(String liquidityPoolID) {
    this.setSegments("liquidity_pools", String.valueOf(liquidityPoolID), "trades");
    return this;
  }

  /**
   * Returns all trades that of a specific type.
   *
   * @param tradeType type
   * @return current {@link TradesRequestBuilder} instance
   * @see <a href="https://developers.stellar.org/api/resources/trades/list/">List All Trades</a>
   */
  public TradesRequestBuilder forTradeType(@NonNull String tradeType) {
    uriBuilder.setQueryParameter(TRADE_TYPE_PARAMETER_NAME, tradeType);
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link TradeResponse}.
   *
   * @param httpClient {@link OkHttpClient} to use to send the request.
   * @param uri {@link HttpUrl} URI to send the request to.
   * @return {@link Page} of {@link TradeResponse}
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
  public static Page<TradeResponse> execute(OkHttpClient httpClient, HttpUrl uri) {
    TypeToken<Page<TradeResponse>> type = new TypeToken<Page<TradeResponse>>() {};
    return executeGetRequest(httpClient, uri, type);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link TradeResponse}
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
  public Page<TradeResponse> execute() {
    return execute(this.httpClient, this.buildUri());
  }

  public TradesRequestBuilder offerId(Long offerId) {
    if (offerId == null) {
      uriBuilder.removeAllQueryParameters("offer_id");
      return this;
    }
    uriBuilder.setQueryParameter("offer_id", offerId.toString());
    return this;
  }

  @Override
  public TradesRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public TradesRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  /**
   * Allows to stream SSE events from horizon. Certain endpoints in Horizon can be called in
   * streaming mode using Server-Sent Events. This mode will keep the connection to horizon open and
   * horizon will continue to return responses as ledgers close.
   *
   * @see <a href="http://www.w3.org/TR/eventsource/" target="_blank">Server-Sent Events</a>
   * @see <a href="https://developers.stellar.org/api/introduction/response-format/"
   *     target="_blank">Response Format documentation</a>
   * @param listener {@link EventListener} implementation with {@link TradeResponse} type
   * @param reconnectTimeout Custom stream connection timeout in ms
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<TradeResponse> stream(
      final EventListener<TradeResponse> listener, long reconnectTimeout) {
    return SSEStream.create(httpClient, this, TradeResponse.class, listener, reconnectTimeout);
  }

  /**
   * An overloaded version of {@link #stream(EventListener, long)} with default reconnect timeout.
   */
  public SSEStream<TradeResponse> stream(final EventListener<TradeResponse> listener) {
    return stream(listener, SSEStream.DEFAULT_RECONNECT_TIMEOUT);
  }
}
