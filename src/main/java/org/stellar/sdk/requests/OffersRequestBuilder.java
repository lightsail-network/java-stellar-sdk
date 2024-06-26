package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Util;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.responses.OfferResponse;
import org.stellar.sdk.responses.Page;

/** Builds requests connected to offers. */
public class OffersRequestBuilder extends RequestBuilder {
  public OffersRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "offers");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link OfferResponse}. This method is helpful
   * for getting the links.
   *
   * @return {@link OfferResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public OfferResponse offer(HttpUrl uri) {
    TypeToken<OfferResponse> type = new TypeToken<OfferResponse>() {};
    return Util.executeGetRequest(httpClient, uri, type);
  }

  /**
   * The offer details endpoint provides information on a single offer.
   *
   * @param offerId specifies which offer to load.
   * @return {@link OfferResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public OfferResponse offer(long offerId) {
    this.setSegments("offers", String.valueOf(offerId));
    return this.offer(this.buildUri());
  }

  /**
   * Returns all offers sponsored by a given account.
   *
   * @param sponsor Account ID of the sponsor.
   * @return current {@link OffersRequestBuilder} instance
   * @see <a href="https://developers.stellar.org/api/resources/offers/">Offers</a>
   */
  public OffersRequestBuilder forSponsor(String sponsor) {
    uriBuilder.setQueryParameter("sponsor", sponsor);
    return this;
  }

  /**
   * Returns all offers where the given account is the seller.
   *
   * @param seller Account ID of the offer creator.
   * @return current {@link OffersRequestBuilder} instance
   * @see <a href="https://developers.stellar.org/api/resources/offers/">Offers</a>
   */
  public OffersRequestBuilder forSeller(String seller) {
    uriBuilder.setQueryParameter("seller", seller);
    return this;
  }

  /**
   * Returns all offers buying an asset.
   *
   * @param asset The Asset being bought.
   * @return current {@link OffersRequestBuilder} instance
   * @see <a href="https://developers.stellar.org/api/resources/offers/">Offers</a>
   */
  public OffersRequestBuilder forBuyingAsset(Asset asset) {
    setAssetParameter("buying", asset);
    return this;
  }

  /**
   * Returns all offers selling an asset.
   *
   * @param asset The Asset being sold.
   * @return current {@link OffersRequestBuilder} instance
   * @see <a href="https://developers.stellar.org/api/resources/offers/">Offers</a>
   */
  public OffersRequestBuilder forSellingAsset(Asset asset) {
    setAssetParameter("selling", asset);
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link OfferResponse}. This
   * method is helpful for getting the next set of results.
   *
   * @param httpClient {@link OkHttpClient} to use to send the request.
   * @param uri {@link HttpUrl} URI to send the request to.
   * @return {@link Page} of {@link OfferResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public static Page<OfferResponse> execute(OkHttpClient httpClient, HttpUrl uri) {
    TypeToken<Page<OfferResponse>> type = new TypeToken<Page<OfferResponse>>() {};
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
   * @param listener {@link EventListener} implementation with {@link OfferResponse} type
   * @param reconnectTimeout Custom stream connection timeout in ms
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<OfferResponse> stream(
      final EventListener<OfferResponse> listener, long reconnectTimeout) {
    return SSEStream.create(httpClient, this, OfferResponse.class, listener, reconnectTimeout);
  }

  /**
   * An overloaded version of {@link #stream(EventListener, long)} with default reconnect timeout.
   */
  public SSEStream<OfferResponse> stream(final EventListener<OfferResponse> listener) {
    return stream(listener, SSEStream.DEFAULT_RECONNECT_TIMEOUT);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link OfferResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   */
  public Page<OfferResponse> execute() {
    return execute(this.httpClient, this.buildUri());
  }

  @Override
  public OffersRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public OffersRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public OffersRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
