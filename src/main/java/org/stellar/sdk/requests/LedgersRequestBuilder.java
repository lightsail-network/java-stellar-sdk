package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.stellar.sdk.Ledger;
import org.stellar.sdk.Page;

import java.io.IOException;
import java.net.URI;

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
