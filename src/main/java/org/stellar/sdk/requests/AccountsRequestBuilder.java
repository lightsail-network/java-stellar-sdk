package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.exception.ConnectionErrorException;
import org.stellar.sdk.exception.RequestTimeoutException;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.http.IHttpClient;
import org.stellar.sdk.http.sse.ISseClient;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.Page;

/** Builds requests connected to accounts. */
public class AccountsRequestBuilder extends RequestBuilder {
  private static final String ASSET_PARAMETER_NAME = "asset";
  private static final String LIQUIDITY_POOL_PARAMETER_NAME = "liquidity_pool";
  private static final String SIGNER_PARAMETER_NAME = "signer";
  private static final String SPONSOR_PARAMETER_NAME = "sponsor";

  public AccountsRequestBuilder(IHttpClient httpClient, ISseClient sseClient, URI serverURI) {
    super(httpClient, sseClient, serverURI, "accounts");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link AccountResponse}. This method is helpful
   * for getting the links.
   *
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws RequestTimeoutException When Horizon returns a <code>Timeout</code> or connection
   *     timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws ConnectionErrorException When the request cannot be executed due to cancellation or
   *     connectivity problems, etc.
   */
  public AccountResponse account(URI uri) {
    TypeToken<AccountResponse> type = new TypeToken<AccountResponse>() {};
    return executeGetRequest(httpClient, uri, type);
  }

  /**
   * Requests <code>GET /accounts/{account}</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/accounts/single/">Account
   *     Details</a>
   * @param account Account to fetch
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws RequestTimeoutException When Horizon returns a <code>Timeout</code> or connection
   *     timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws ConnectionErrorException When the request cannot be executed due to cancellation or
   *     connectivity problems, etc.
   */
  public AccountResponse account(String account) {
    this.setSegments("accounts", account);
    return this.account(this.buildUri());
  }

  /**
   * Returns all accounts that contain a specific signer.
   *
   * @param signer Account ID
   * @return current {@link AccountsRequestBuilder} instance
   * @see <a href="https://developers.stellar.org/api/resources/accounts/list/">Accounts</a>
   */
  public AccountsRequestBuilder forSigner(String signer) {
    if (uriBuilder.hasQueryParameter(ASSET_PARAMETER_NAME)) {
      throw new IllegalArgumentException("cannot set both asset and signer");
    }
    if (uriBuilder.hasQueryParameter(LIQUIDITY_POOL_PARAMETER_NAME)) {
      throw new IllegalArgumentException("cannot set both liquidity_pool and signer");
    }
    if (uriBuilder.hasQueryParameter(SPONSOR_PARAMETER_NAME)) {
      throw new IllegalArgumentException("cannot set both sponsor and signer");
    }
    uriBuilder.setQueryParameter(SIGNER_PARAMETER_NAME, signer);
    return this;
  }

  /**
   * Returns all accounts who are trustees to a specific asset.
   *
   * @param asset An issued asset
   * @return current {@link AccountsRequestBuilder} instance
   * @see <a href="https://developers.stellar.org/api/resources/accounts/list/">Accounts</a>
   */
  public AccountsRequestBuilder forAsset(AssetTypeCreditAlphaNum asset) {
    if (uriBuilder.hasQueryParameter(LIQUIDITY_POOL_PARAMETER_NAME)) {
      throw new IllegalArgumentException("cannot set both liquidity_pool and asset");
    }
    if (uriBuilder.hasQueryParameter(SIGNER_PARAMETER_NAME)) {
      throw new IllegalArgumentException("cannot set both signer and asset");
    }
    if (uriBuilder.hasQueryParameter(SPONSOR_PARAMETER_NAME)) {
      throw new IllegalArgumentException("cannot set both sponsor and asset");
    }
    setAssetParameter(ASSET_PARAMETER_NAME, asset);
    return this;
  }

  /**
   * Returns all accounts who have trustlines to the specified liquidity pool.
   *
   * @param liquidityPoolId Liquidity Pool ID
   * @return current {@link AccountsRequestBuilder} instance
   * @see <a href="https://developers.stellar.org/api/resources/accounts/list/">Accounts</a>
   */
  public AccountsRequestBuilder forLiquidityPool(String liquidityPoolId) {
    if (uriBuilder.hasQueryParameter(ASSET_PARAMETER_NAME)) {
      throw new IllegalArgumentException("cannot set both asset and liquidity_pool");
    }
    if (uriBuilder.hasQueryParameter(SIGNER_PARAMETER_NAME)) {
      throw new IllegalArgumentException("cannot set both signer and liquidity_pool");
    }
    if (uriBuilder.hasQueryParameter(SPONSOR_PARAMETER_NAME)) {
      throw new IllegalArgumentException("cannot set both sponsor and liquidity_pool");
    }
    uriBuilder.setQueryParameter(LIQUIDITY_POOL_PARAMETER_NAME, liquidityPoolId);
    return this;
  }

  /**
   * Returns all accounts who are sponsored by a given account or have subentries which are
   * sponsored by a given account.
   *
   * @param sponsor Account ID
   * @return current {@link AccountsRequestBuilder} instance
   * @see <a href="https://developers.stellar.org/api/resources/accounts/list/">Accounts</a>
   */
  public AccountsRequestBuilder forSponsor(String sponsor) {
    if (uriBuilder.hasQueryParameter(ASSET_PARAMETER_NAME)) {
      throw new IllegalArgumentException("cannot set both asset and sponsor");
    }
    if (uriBuilder.hasQueryParameter(LIQUIDITY_POOL_PARAMETER_NAME)) {
      throw new IllegalArgumentException("cannot set both liquidity_pool and sponsor");
    }
    if (uriBuilder.hasQueryParameter(SIGNER_PARAMETER_NAME)) {
      throw new IllegalArgumentException("cannot set both signer and sponsor");
    }
    uriBuilder.setQueryParameter(SPONSOR_PARAMETER_NAME, sponsor);
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link AccountResponse}. This
   * method is helpful for getting the next set of results.
   *
   * @return {@link Page} of {@link AccountResponse}
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws RequestTimeoutException When Horizon returns a <code>Timeout</code> or connection
   *     timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws ConnectionErrorException When the request cannot be executed due to cancellation or
   *     connectivity problems, etc.
   */
  public static Page<AccountResponse> execute(IHttpClient httpClient, URI uri) {
    TypeToken<Page<AccountResponse>> type = new TypeToken<Page<AccountResponse>>() {};
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
   * @param listener {@link EventListener} implementation with {@link AccountResponse} type
   * @param reconnectTimeout Custom stream connection timeout in ms
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<AccountResponse> stream(
      final EventListener<AccountResponse> listener, long reconnectTimeout) {
    return SSEStream.create(sseClient, this, AccountResponse.class, listener, reconnectTimeout);
  }

  /**
   * An overloaded version of {@link #stream(EventListener, long)} with default reconnect timeout.
   */
  public SSEStream<AccountResponse> stream(final EventListener<AccountResponse> listener) {
    return stream(listener, SSEStream.DEFAULT_RECONNECT_TIMEOUT);
  }

  /**
   * Build and execute request. <strong>Warning!</strong> {@link AccountResponse}s in {@link Page}
   * will contain only <code>keypair</code> field.
   *
   * @return {@link Page} of {@link AccountResponse}
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws RequestTimeoutException When Horizon returns a <code>Timeout</code> or connection
   *     timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws ConnectionErrorException When the request cannot be executed due to cancellation or
   *     connectivity problems, etc.
   */
  public Page<AccountResponse> execute() {
    return execute(this.httpClient, this.buildUri());
  }

  @Override
  public AccountsRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public AccountsRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public AccountsRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
