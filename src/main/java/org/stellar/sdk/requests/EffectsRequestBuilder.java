package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.exception.ConnectionErrorException;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.effects.EffectResponse;

/** Builds requests connected to effects. */
public class EffectsRequestBuilder extends RequestBuilder {
  public EffectsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "effects");
  }

  /**
   * Builds request to <code>GET /accounts/{account}/effects</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/accounts/effects/">Effects for
   *     Account</a>
   * @param account Account for which to get effects
   */
  public EffectsRequestBuilder forAccount(@NonNull String account) {
    this.setSegments("accounts", account, "effects");
    return this;
  }

  /**
   * Builds request to <code>GET /ledgers/{ledgerSeq}/effects</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/ledgers/effects/">Effects for
   *     Ledger</a>
   * @param ledgerSeq Ledger for which to get effects
   */
  public EffectsRequestBuilder forLedger(long ledgerSeq) {
    this.setSegments("ledgers", String.valueOf(ledgerSeq), "effects");
    return this;
  }

  /**
   * Builds request to <code>GET /transactions/{transactionId}/effects</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/transactions/effects/">Effect for
   *     Transaction</a>
   * @param transactionId Transaction ID for which to get effects
   */
  public EffectsRequestBuilder forTransaction(@NonNull String transactionId) {
    this.setSegments("transactions", transactionId, "effects");
    return this;
  }

  /**
   * Builds request to <code>GET /liquidity_pools/{poolID}/effects</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/liquiditypools/effects/">Effects for
   *     Liquidity Pool</a>
   * @param liquidityPoolID Liquidity pool for which to get effects
   */
  public EffectsRequestBuilder forLiquidityPool(LiquidityPoolID liquidityPoolID) {
    return this.forLiquidityPool(String.valueOf(liquidityPoolID));
  }

  /**
   * Builds request to <code>GET /liquidity_pools/{poolID}/effects</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/liquiditypools/effects/">Effects for
   *     Liquidity Pool</a>
   * @param liquidityPoolID Liquidity pool for which to get effects
   */
  public EffectsRequestBuilder forLiquidityPool(String liquidityPoolID) {
    this.setSegments("liquidity_pools", liquidityPoolID, "effects");
    return this;
  }

  /**
   * Builds request to <code>GET /operation/{operationId}/effects</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/operations/effects/">Effect for
   *     Operation</a>
   * @param operationId Operation ID for which to get effects
   */
  public EffectsRequestBuilder forOperation(long operationId) {
    this.setSegments("operations", String.valueOf(operationId), "effects");
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link EffectResponse}. This
   * method is helpful for getting the next set of results.
   *
   * @param httpClient {@link OkHttpClient} to use to send the request.
   * @param uri {@link HttpUrl} URI to send the request to.
   * @return {@link Page} of {@link EffectResponse}
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
   * @throws ConnectionErrorException When the request cannot be executed due to cancellation or
   *     connectivity problems, etc.
   */
  public static Page<EffectResponse> execute(OkHttpClient httpClient, HttpUrl uri) {
    TypeToken<Page<EffectResponse>> type = new TypeToken<Page<EffectResponse>>() {};
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
   * @param listener {@link EventListener} implementation with {@link EffectResponse} type
   * @param reconnectTimeout Custom stream connection timeout in ms
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<EffectResponse> stream(
      final EventListener<EffectResponse> listener, long reconnectTimeout) {
    return SSEStream.create(httpClient, this, EffectResponse.class, listener, reconnectTimeout);
  }

  /**
   * An overloaded version of {@link #stream(EventListener, long)} with default reconnect timeout.
   */
  public SSEStream<EffectResponse> stream(final EventListener<EffectResponse> listener) {
    return stream(listener, SSEStream.DEFAULT_RECONNECT_TIMEOUT);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link EffectResponse}
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
   * @throws ConnectionErrorException When the request cannot be executed due to cancellation or
   *     connectivity problems, etc.
   */
  public Page<EffectResponse> execute() {
    return execute(this.httpClient, this.buildUri());
  }

  @Override
  public EffectsRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public EffectsRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public EffectsRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
