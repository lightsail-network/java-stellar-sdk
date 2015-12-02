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

  public Ledger ledger(URI uri) throws IOException {
    TypeToken type = new TypeToken<Ledger>() {};
    ResponseHandler<Ledger> responseHandler = new ResponseHandler<Ledger>(type);
    return (Ledger) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  public Ledger ledger(long ledgerSeq) throws IOException {
    this.setSegments("ledgers", String.valueOf(ledgerSeq));
    return this.ledger(this.buildUri());
  }

  public static Page<Ledger> execute(URI uri) throws IOException {
    TypeToken type = new TypeToken<Page<Ledger>>() {};
    ResponseHandler<Page<Ledger>> responseHandler = new ResponseHandler<Page<Ledger>>(type);
    return (Page<Ledger>) Request.Get(uri).execute().handleResponse(responseHandler);
  }

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
