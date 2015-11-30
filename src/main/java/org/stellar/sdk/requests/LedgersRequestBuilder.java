package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.stellar.sdk.Ledger;
import org.stellar.sdk.Page;

import java.io.IOException;
import java.net.URI;

public class LedgersRequestBuilder extends RequestBuilder {
  public LedgersRequestBuilder(URI serverURI) {
    super(serverURI, "ledgers");
  }

  public Ledger ledger(long ledgerSeq) throws IOException {
    TypeToken type = new TypeToken<Ledger>() {};
    ResponseHandler<Ledger> responseHandler = new ResponseHandler<Ledger>(type);
    this.addSegments("ledgers", String.valueOf(ledgerSeq));
    return (Ledger) Request.Get(this.buildUri()).execute().handleResponse(responseHandler);
  }

  public Page<Ledger> execute() throws IOException {
    TypeToken type = new TypeToken<Page<Ledger>>() {};
    ResponseHandler<Page<Ledger>> responseHandler = new ResponseHandler<Page<Ledger>>(type);
    return (Page<Ledger>) Request.Get(this.buildUri()).execute().handleResponse(responseHandler);
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
