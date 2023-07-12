package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.responses.LiquidityPoolResponse;
import org.stellar.sdk.responses.Page;

/** Builds requests connected to liquidity pools. */
public class LiquidityPoolsRequestBuilder extends RequestBuilder {
  private static final String RESERVES_PARAMETER_NAME = "reserves";

  public LiquidityPoolsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "liquidity_pools");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link LiquidityPoolResponse}. This method is
   * helpful for getting the links.
   *
   * @throws IOException
   */
  public LiquidityPoolResponse liquidityPool(HttpUrl uri) throws IOException {
    TypeToken type = new TypeToken<LiquidityPoolResponse>() {};
    ResponseHandler<LiquidityPoolResponse> responseHandler =
        new ResponseHandler<LiquidityPoolResponse>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }

  /**
   * Requests <code>GET /liquidity_pools/{liquidity_pool_id}</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/liquiditypools/single/">Liquidity
   *     Pool Details</a>
   * @param liquidityPoolID Liquidity Pool to fetch
   * @throws IOException
   */
  public LiquidityPoolResponse liquidityPool(String liquidityPoolID) throws IOException {
    this.setSegments("liquidity_pools", liquidityPoolID);
    return this.liquidityPool(this.buildUri());
  }

  /**
   * Requests <code>GET /liquidity_pools/{liquidity_pool_id}</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/liquiditypools/single/">Liquidity
   *     Pool Details</a>
   * @param liquidityPoolID Liquidity Pool to fetch
   * @throws IOException
   */
  public LiquidityPoolResponse liquidityPool(LiquidityPoolID liquidityPoolID) throws IOException {
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
   * Requests specific <code>uri</code> and returns {@link Page} of {@link LiquidityPoolResponse}.
   * This method is helpful for getting the next set of results.
   *
   * @return {@link Page} of {@link LiquidityPoolResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public static Page<LiquidityPoolResponse> execute(OkHttpClient httpClient, HttpUrl uri)
      throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<Page<LiquidityPoolResponse>>() {};
    ResponseHandler<Page<LiquidityPoolResponse>> responseHandler =
        new ResponseHandler<Page<LiquidityPoolResponse>>(type);

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
   * @param listener {@link EventListener} implementation with {@link LiquidityPoolResponse} type
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<LiquidityPoolResponse> stream(
      final EventListener<LiquidityPoolResponse> listener) {

    return SSEStream.create(httpClient, this, LiquidityPoolResponse.class, listener);
  }

  /**
   * Build and execute request. <strong>Warning!</strong> {@link LiquidityPoolResponse}s in {@link
   * Page} will contain only <code>keypair</code> field.
   *
   * @return {@link Page} of {@link LiquidityPoolResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public Page<LiquidityPoolResponse> execute() throws IOException, TooManyRequestsException {
    return this.execute(this.httpClient, this.buildUri());
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
