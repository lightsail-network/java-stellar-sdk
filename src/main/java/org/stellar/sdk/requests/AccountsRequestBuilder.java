package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.Page;

/** Builds requests connected to accounts. */
public class AccountsRequestBuilder extends RequestBuilder {
  private static final String ASSET_PARAMETER_NAME = "asset";
  private static final String LIQUIDITY_POOL_PARAMETER_NAME = "liquidity_pool";
  private static final String SIGNER_PARAMETER_NAME = "signer";
  private static final String SPONSOR_PARAMETER_NAME = "sponsor";

  public AccountsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "accounts");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link AccountResponse}. This method is helpful
   * for getting the links.
   *
   * @throws IOException
   */
  public AccountResponse account(HttpUrl uri) throws IOException {
    TypeToken type = new TypeToken<AccountResponse>() {};
    ResponseHandler<AccountResponse> responseHandler = new ResponseHandler<AccountResponse>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }

  /**
   * Requests <code>GET /accounts/{account}</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/accounts/single/">Account
   *     Details</a>
   * @param account Account to fetch
   * @throws IOException
   */
  public AccountResponse account(String account) throws IOException {
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
    if (uriBuilder.build().queryParameter(ASSET_PARAMETER_NAME) != null) {
      throw new RuntimeException("cannot set both asset and signer");
    }
    if (uriBuilder.build().queryParameter(LIQUIDITY_POOL_PARAMETER_NAME) != null) {
      throw new RuntimeException("cannot set both liquidity_pool and signer");
    }
    if (uriBuilder.build().queryParameter(SPONSOR_PARAMETER_NAME) != null) {
      throw new RuntimeException("cannot set both sponsor and signer");
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
    if (uriBuilder.build().queryParameter(LIQUIDITY_POOL_PARAMETER_NAME) != null) {
      throw new RuntimeException("cannot set both liquidity_pool and asset");
    }
    if (uriBuilder.build().queryParameter(SIGNER_PARAMETER_NAME) != null) {
      throw new RuntimeException("cannot set both signer and asset");
    }
    if (uriBuilder.build().queryParameter(SPONSOR_PARAMETER_NAME) != null) {
      throw new RuntimeException("cannot set both sponsor and asset");
    }
    setAssetParameter(ASSET_PARAMETER_NAME, asset);
    return this;
  }

  /**
   * Returns all accounts who have trustlines to the specified liquidity pool.
   *
   * @param Liquidity Pool ID
   * @return current {@link AccountsRequestBuilder} instance
   * @see <a href="https://developers.stellar.org/api/resources/accounts/list/">Accounts</a>
   */
  public AccountsRequestBuilder forLiquidityPool(LiquidityPoolID liquidityPoolID) {
    return this.forLiquidityPool(liquidityPoolID.toString());
  }

  /**
   * Returns all accounts who have trustlines to the specified liquidity pool.
   *
   * @param Liquidity Pool ID
   * @return current {@link AccountsRequestBuilder} instance
   * @see <a href="https://developers.stellar.org/api/resources/accounts/list/">Accounts</a>
   */
  public AccountsRequestBuilder forLiquidityPool(String liquidityPoolID) {
    if (uriBuilder.build().queryParameter(ASSET_PARAMETER_NAME) != null) {
      throw new RuntimeException("cannot set both asset and liquidity_pool");
    }
    if (uriBuilder.build().queryParameter(SIGNER_PARAMETER_NAME) != null) {
      throw new RuntimeException("cannot set both signer and liquidity_pool");
    }
    if (uriBuilder.build().queryParameter(SPONSOR_PARAMETER_NAME) != null) {
      throw new RuntimeException("cannot set both sponsor and liquidity_pool");
    }
    uriBuilder.setQueryParameter(LIQUIDITY_POOL_PARAMETER_NAME, liquidityPoolID);
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
    if (uriBuilder.build().queryParameter(ASSET_PARAMETER_NAME) != null) {
      throw new RuntimeException("cannot set both asset and sponsor");
    }
    if (uriBuilder.build().queryParameter(LIQUIDITY_POOL_PARAMETER_NAME) != null) {
      throw new RuntimeException("cannot set both liquidity_pool and sponsor");
    }
    if (uriBuilder.build().queryParameter(SIGNER_PARAMETER_NAME) != null) {
      throw new RuntimeException("cannot set both signer and sponsor");
    }
    uriBuilder.setQueryParameter(SPONSOR_PARAMETER_NAME, sponsor);
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link AccountResponse}. This
   * method is helpful for getting the next set of results.
   *
   * @return {@link Page} of {@link AccountResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public static Page<AccountResponse> execute(OkHttpClient httpClient, HttpUrl uri)
      throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<Page<AccountResponse>>() {};
    ResponseHandler<Page<AccountResponse>> responseHandler =
        new ResponseHandler<Page<AccountResponse>>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
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
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<AccountResponse> stream(final EventListener<AccountResponse> listener) {

    return SSEStream.create(httpClient, this, AccountResponse.class, listener);
  }

  /**
   * Build and execute request. <strong>Warning!</strong> {@link AccountResponse}s in {@link Page}
   * will contain only <code>keypair</code> field.
   *
   * @return {@link Page} of {@link AccountResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public Page<AccountResponse> execute() throws IOException, TooManyRequestsException {
    return this.execute(this.httpClient, this.buildUri());
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
