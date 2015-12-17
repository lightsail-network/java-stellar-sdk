package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.stellar.sdk.GsonSingleton;
import org.stellar.sdk.Ledger;
import org.stellar.sdk.Page;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Builds requests connected to ledgers.
 */
public class LedgersRequestBuilder extends RequestBuilder {
  public LedgersRequestBuilder(URI serverURI) {
    super(serverURI, "ledgers");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Ledger}.
   * This method is helpful for getting the links.
   * @throws IOException
   */
  public Ledger ledger(URI uri) throws IOException {
    TypeToken type = new TypeToken<Ledger>() {};
    ResponseHandler<Ledger> responseHandler = new ResponseHandler<Ledger>(type);
    return (Ledger) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  /**
   * Requests <code>GET /ledgers/{ledgerSeq}</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/ledgers-single.html">Ledger Details</a>
   * @param ledgerSeq Ledger to fetch
   * @throws IOException
   */
  public Ledger ledger(long ledgerSeq) throws IOException {
    this.setSegments("ledgers", String.valueOf(ledgerSeq));
    return this.ledger(this.buildUri());
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link Ledger}.
   * This method is helpful for getting the next set of results.
   * @return {@link Page} of {@link Ledger}
   * @throws IOException
   */
  public static Page<Ledger> execute(URI uri) throws IOException {
    TypeToken type = new TypeToken<Page<Ledger>>() {};
    ResponseHandler<Page<Ledger>> responseHandler = new ResponseHandler<Page<Ledger>>(type);
    return (Page<Ledger>) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  /**
   * Allows to stream SSE events from horizon.
   * Certain endpoints in Horizon can be called in streaming mode using Server-Sent Events.
   * This mode will keep the connection to horizon open and horizon will continue to return
   * responses as ledgers close.
   * @see <a href="http://www.w3.org/TR/eventsource/" target="_blank">Server-Sent Events</a>
   * @see <a href="https://www.stellar.org/developers/horizon/learn/responses.html" target="_blank">Response Format documentation</a>
   * @param listener {@link EventListener} implementation with {@link Ledger} type
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public EventSource stream(final EventListener<Ledger> listener) {
    Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
    WebTarget target = client.target(this.buildUri());
    EventSource eventSource = new EventSource(target) {
      @Override
      public void onEvent(InboundEvent inboundEvent) {
        String data = inboundEvent.readData(String.class);
        if (data.equals("\"hello\"")) {
          return;
        }
        Ledger ledger = GsonSingleton.getInstance().fromJson(data, Ledger.class);
        listener.onEvent(ledger);
      }
    };
    return eventSource;
  }

  /**
   * Build and execute request.
   * @return {@link Page} of {@link Ledger}
   * @throws IOException
   */
  public Page<Ledger> execute() throws IOException {
    return this.execute(this.buildUri());
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
