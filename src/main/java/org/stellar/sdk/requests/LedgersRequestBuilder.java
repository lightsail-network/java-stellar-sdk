package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.responses.LedgerResponse;
import org.stellar.sdk.responses.Page;

/** Builds requests connected to ledgers. */
public class LedgersRequestBuilder extends RequestBuilder {
  public LedgersRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "ledgers");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link LedgerResponse}. This method is helpful
   * for getting the links.
   *
   * @throws IOException
   */
  public LedgerResponse ledger(HttpUrl uri) throws IOException {
    TypeToken type = new TypeToken<LedgerResponse>() {};
    ResponseHandler<LedgerResponse> responseHandler = new ResponseHandler<LedgerResponse>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }

  /**
   * Requests <code>GET /ledgers/{ledgerSeq}</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/ledgers/single/">Ledger Details</a>
   * @param ledgerSeq Ledger to fetch
   * @throws IOException
   */
  public LedgerResponse ledger(long ledgerSeq) throws IOException {
    this.setSegments("ledgers", String.valueOf(ledgerSeq));
    return this.ledger(this.buildUri());
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link LedgerResponse}. This
   * method is helpful for getting the next set of results.
   *
   * @return {@link Page} of {@link LedgerResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public static Page<LedgerResponse> execute(OkHttpClient httpClient, HttpUrl uri)
      throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<Page<LedgerResponse>>() {};
    ResponseHandler<Page<LedgerResponse>> responseHandler =
        new ResponseHandler<Page<LedgerResponse>>(type);

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
   * @param listener {@link EventListener} implementation with {@link LedgerResponse} type
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<LedgerResponse> stream(final EventListener<LedgerResponse> listener) {
    return SSEStream.create(httpClient, this, LedgerResponse.class, listener);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link LedgerResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public Page<LedgerResponse> execute() throws IOException, TooManyRequestsException {
    return this.execute(this.httpClient, this.buildUri());
  }

  @Override
  public LedgersRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public LedgersRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public LedgersRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
