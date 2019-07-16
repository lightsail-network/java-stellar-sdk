package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.responses.OfferResponse;
import org.stellar.sdk.responses.Page;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds requests connected to offers.
 */
public class OffersRequestBuilder extends RequestBuilder {
  public OffersRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "offers");
  }

  /**
   * Builds request to <code>GET /accounts/{account}/offers</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/offers-for-account.html">Offers for Account</a>
   * @param account Account for which to get offers
   */
  public OffersRequestBuilder forAccount(String account) {
    account = checkNotNull(account, "account cannot be null");
    this.setSegments("accounts", account, "offers");
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link OfferResponse}.
   * This method is helpful for getting the next set of results.
   * @return {@link Page} of {@link OfferResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public static Page<OfferResponse> execute(OkHttpClient httpClient, HttpUrl uri) throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<Page<OfferResponse>>() {};
    ResponseHandler<Page<OfferResponse>> responseHandler = new ResponseHandler<Page<OfferResponse>>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }


  /**
   * Allows to stream SSE events from horizon.
   * Certain endpoints in Horizon can be called in streaming mode using Server-Sent Events.
   * This mode will keep the connection to horizon open and horizon will continue to return
   * responses as ledgers close.
   * @see <a href="http://www.w3.org/TR/eventsource/" target="_blank">Server-Sent Events</a>
   * @see <a href="https://www.stellar.org/developers/horizon/learn/responses.html" target="_blank">Response Format documentation</a>
   * @param listener {@link EventListener} implementation with {@link OfferResponse} type
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */

  public SSEStream<OfferResponse> stream(final EventListener<OfferResponse> listener) {
      return SSEStream.create(httpClient, this, OfferResponse.class, listener);
  }

  /**
   * Build and execute request.
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
