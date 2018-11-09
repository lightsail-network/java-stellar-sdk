package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.Page;

import java.io.IOException;

/**
 * Builds requests connected to accounts.
 */
public class AccountsRequestBuilder extends RequestBuilder {
  public AccountsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "accounts");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link AccountResponse}.
   * This method is helpful for getting the links.
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
   * @see <a href="https://www.stellar.org/developers/horizon/reference/accounts-single.html">Account Details</a>
   * @param account Account to fetch
   * @throws IOException
   */
  public AccountResponse account(KeyPair account) throws IOException {
    this.setSegments("accounts", account.getAccountId());
    return this.account(this.buildUri());
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link AccountResponse}.
   * This method is helpful for getting the next set of results.
   * @return {@link Page} of {@link AccountResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public static Page<AccountResponse> execute(OkHttpClient httpClient, HttpUrl uri) throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<Page<AccountResponse>>() {};
    ResponseHandler<Page<AccountResponse>> responseHandler = new ResponseHandler<Page<AccountResponse>>(type);

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
   * @param listener {@link EventListener} implementation with {@link AccountResponse} type
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<AccountResponse> stream(final EventListener<AccountResponse> listener) {

    return SSEStream.create(httpClient,this,AccountResponse.class,listener);
  }

  /**
   * Build and execute request. <strong>Warning!</strong> {@link AccountResponse}s in {@link Page} will contain only <code>keypair</code> field.
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
