package org.stellar.sdk.requests;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.Asset;
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
   * @throws IOException
   */
  public OfferResponse offer(HttpUrl uri) throws IOException {
    TypeToken type = new TypeToken<OfferResponse>() {};
    ResponseHandler<OfferResponse> responseHandler = new ResponseHandler<OfferResponse>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }

  /**
   * The offer details endpoint provides information on a single offer.
   *
   * @param offerId specifies which offer to load.
   * @return The offer details.
   * @throws IOException
   */
  public OfferResponse offer(long offerId) throws IOException {
    this.setSegments("offers", String.valueOf(offerId));
    return this.offer(this.buildUri());
  }

  /**
   * @param account Account for which to get offers
   * @see <a href="https://developers.stellar.org/api/resources/accounts/offers/">Offers for
   *     Account</a>
   * @deprecated Use {@link OffersRequestBuilder#forSeller} Builds request to <code>
   *     GET /accounts/{account}/offers</code>
   */
  @Deprecated
  public OffersRequestBuilder forAccount(String account) {
    account = checkNotNull(account, "account cannot be null");
    this.setSegments("accounts", account, "offers");
    return this;
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
   * @return {@link Page} of {@link OfferResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public static Page<OfferResponse> execute(OkHttpClient httpClient, HttpUrl uri)
      throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<Page<OfferResponse>>() {};
    ResponseHandler<Page<OfferResponse>> responseHandler =
        new ResponseHandler<Page<OfferResponse>>(type);

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
   * @param listener {@link EventListener} implementation with {@link OfferResponse} type
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<OfferResponse> stream(final EventListener<OfferResponse> listener) {
    return SSEStream.create(httpClient, this, OfferResponse.class, listener);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link OfferResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public Page<OfferResponse> execute() throws IOException, TooManyRequestsException {
    return this.execute(this.httpClient, this.buildUri());
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
