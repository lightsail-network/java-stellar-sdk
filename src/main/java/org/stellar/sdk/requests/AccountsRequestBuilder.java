package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.stellar.base.KeyPair;
import org.stellar.sdk.Account;
import org.stellar.sdk.GsonSingleton;
import org.stellar.sdk.Page;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Builds requests connected to accounts.
 */
public class AccountsRequestBuilder extends RequestBuilder {
  public AccountsRequestBuilder(URI serverURI) {
    super(serverURI, "accounts");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Account}.
   * This method is helpful for getting the links.
   * @throws IOException
   */
  public Account account(URI uri) throws IOException {
    TypeToken type = new TypeToken<Account>() {};
    ResponseHandler<Account> responseHandler = new ResponseHandler<Account>(type);
    return (Account) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  /**
   * Requests <code>GET /accounts/{account}</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/accounts-single.html">Account Details</a>
   * @param account Account to fetch
   * @throws IOException
   */
  public Account account(KeyPair account) throws IOException {
    this.setSegments("accounts", account.getAddress());
    return this.account(this.buildUri());
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link Account}.
   * This method is helpful for getting the next set of results.
   * @return {@link Page} of {@link Account}
   * @throws IOException
   */
  public static Page<Account> execute(URI uri) throws IOException {
    TypeToken type = new TypeToken<Page<Account>>() {};
    ResponseHandler<Page<Account>> responseHandler = new ResponseHandler<Page<Account>>(type);
    return (Page<Account>) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  /**
   * Allows to stream SSE events from horizon.
   * Certain endpoints in Horizon can be called in streaming mode using Server-Sent Events.
   * This mode will keep the connection to horizon open and horizon will continue to return
   * responses as ledgers close.
   * @see <a href="http://www.w3.org/TR/eventsource/" target="_blank">Server-Sent Events</a>
   * @see <a href="https://www.stellar.org/developers/horizon/learn/responses.html" target="_blank">Response Format documentation</a>
   * @param listener {@link EventListener} implementation with {@link Account} type
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public EventSource stream(final EventListener<Account> listener) {
    Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
    WebTarget target = client.target(this.buildUri());
    EventSource eventSource = new EventSource(target) {
      @Override
      public void onEvent(InboundEvent inboundEvent) {
        String data = inboundEvent.readData(String.class);
        if (data.equals("\"hello\"")) {
          return;
        }
        Account account = GsonSingleton.getInstance().fromJson(data, Account.class);
        listener.onEvent(account);
      }
    };
    return eventSource;
  }

  /**
   * Build and execute request. <strong>Warning!</strong> {@link Account}s in {@link Page} will contain only <code>keypair</code> field.
   * @return {@link Page} of {@link Account}
   * @throws IOException
   */
  public Page<Account> execute() throws IOException {
    return this.execute(this.buildUri());
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
