package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.Util;
import org.stellar.sdk.responses.OrderBookResponse;

/** Builds requests connected to order book. */
public class OrderBookRequestBuilder extends RequestBuilder {
  public OrderBookRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "order_book");
  }

  public OrderBookRequestBuilder buyingAsset(Asset asset) {
    uriBuilder.setQueryParameter("buying_asset_type", asset.getType());
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.setQueryParameter("buying_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.setQueryParameter("buying_asset_issuer", creditAlphaNumAsset.getIssuer());
    }
    return this;
  }

  public OrderBookRequestBuilder sellingAsset(Asset asset) {
    uriBuilder.setQueryParameter("selling_asset_type", asset.getType());
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.setQueryParameter("selling_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.setQueryParameter("selling_asset_issuer", creditAlphaNumAsset.getIssuer());
    }
    return this;
  }

  public static OrderBookResponse execute(OkHttpClient httpClient, HttpUrl uri) {
    TypeToken<OrderBookResponse> type = new TypeToken<OrderBookResponse>() {};
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
   * @param listener {@link OrderBookResponse} implementation with {@link OrderBookResponse} type
   * @param reconnectTimeout Custom stream connection timeout in ms
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<OrderBookResponse> stream(
      final EventListener<OrderBookResponse> listener, long reconnectTimeout) {
    return SSEStream.create(httpClient, this, OrderBookResponse.class, listener, reconnectTimeout);
  }

  /**
   * An overloaded version of {@link #stream(EventListener, long)} with default reconnect timeout.
   */
  public SSEStream<OrderBookResponse> stream(final EventListener<OrderBookResponse> listener) {
    return stream(listener, SSEStream.DEFAULT_RECONNECT_TIMEOUT);
  }

  public OrderBookResponse execute() {
    return execute(this.httpClient, this.buildUri());
  }

  @Override
  public RequestBuilder cursor(String cursor) {
    throw new RuntimeException("Not implemented yet.");
  }

  @Override
  public RequestBuilder order(Order direction) {
    throw new RuntimeException("Not implemented yet.");
  }
}
