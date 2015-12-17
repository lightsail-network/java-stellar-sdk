package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.stellar.base.KeyPair;
import org.stellar.sdk.GsonSingleton;
import org.stellar.sdk.Page;
import org.stellar.sdk.Transaction;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds requests connected to transactions.
 */
public class TransactionsRequestBuilder extends RequestBuilder {
  public TransactionsRequestBuilder(URI serverURI) {
    super(serverURI, "transactions");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Transaction}.
   * This method is helpful for getting the links.
   * @throws IOException
   */
  public Transaction transaction(URI uri) throws IOException {
    TypeToken type = new TypeToken<Transaction>() {};
    ResponseHandler<Transaction> responseHandler = new ResponseHandler<Transaction>(type);
    return (Transaction) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  /**
   * Requests <code>GET /transactions/{transactionId}</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/transactions-single.html">Transaction Details</a>
   * @param transactionId Transaction to fetch
   * @throws IOException
   */
  public Transaction transaction(String transactionId) throws IOException {
    this.setSegments("transaction", transactionId);
    return this.transaction(this.buildUri());
  }

  /**
   * Builds request to <code>GET /accounts/{account}/transactions</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/transactions-for-account.html">Transactions for Account</a>
   * @param account Account for which to get transactions
   */
  public TransactionsRequestBuilder forAccount(KeyPair account) {
    account = checkNotNull(account, "account cannot be null");
    this.setSegments("accounts", account.getAddress(), "transactions");
    return this;
  }

  /**
   * Builds request to <code>GET /ledgers/{ledgerSeq}/transactions</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/transactions-for-ledger.html">Transactions for Ledger</a>
   * @param ledgerSeq Ledger for which to get transactions
   */
  public TransactionsRequestBuilder forLedger(long ledgerSeq) {
    this.setSegments("ledgers", String.valueOf(ledgerSeq), "transactions");
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link Transaction}.
   * This method is helpful for getting the next set of results.
   * @return {@link Page} of {@link Transaction}
   * @throws IOException
   */
  public static Page<Transaction> execute(URI uri) throws IOException {
    TypeToken type = new TypeToken<Page<Transaction>>() {};
    ResponseHandler<Page<Transaction>> responseHandler = new ResponseHandler<Page<Transaction>>(type);
    return (Page<Transaction>) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  /**
   * Allows to stream SSE events from horizon.
   * Certain endpoints in Horizon can be called in streaming mode using Server-Sent Events.
   * This mode will keep the connection to horizon open and horizon will continue to return
   * responses as ledgers close.
   * @see <a href="http://www.w3.org/TR/eventsource/" target="_blank">Server-Sent Events</a>
   * @see <a href="https://www.stellar.org/developers/horizon/learn/responses.html" target="_blank">Response Format documentation</a>
   * @param listener {@link EventListener} implementation with {@link Transaction} type
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public EventSource stream(final EventListener<Transaction> listener) {
    Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
    WebTarget target = client.target(this.buildUri());
    EventSource eventSource = new EventSource(target) {
      @Override
      public void onEvent(InboundEvent inboundEvent) {
        String data = inboundEvent.readData(String.class);
        if (data.equals("\"hello\"")) {
          return;
        }
        Transaction transaction = GsonSingleton.getInstance().fromJson(data, Transaction.class);
        listener.onEvent(transaction);
      }
    };
    return eventSource;
  }

  /**
   * Build and execute request.
   * @return {@link Page} of {@link Transaction}
   * @throws IOException
   */
  public Page<Transaction> execute() throws IOException {
    return this.execute(this.buildUri());
  }

  @Override
  public TransactionsRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public TransactionsRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public TransactionsRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
