package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.stellar.base.Keypair;
import org.stellar.sdk.Page;
import org.stellar.sdk.Transaction;

import java.io.IOException;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

public class TransactionsRequestBuilder extends RequestBuilder {
  public TransactionsRequestBuilder(URI serverURI) {
    super(serverURI, "transactions");
  }

  public Transaction transaction(String transactionId) throws IOException {
    TypeToken type = new TypeToken<Transaction>() {};
    ResponseHandler<Transaction> responseHandler = new ResponseHandler<Transaction>(type);
    this.addSegments("transaction", transactionId);
    return (Transaction) Request.Get(this.buildUri()).execute().handleResponse(responseHandler);
  }

  public TransactionsRequestBuilder forAccount(Keypair account) {
    account = checkNotNull(account, "account cannot be null");
    this.addSegments("accounts", account.getAddress(), "transactions");
    return this;
  }

  public TransactionsRequestBuilder forLedger(long ledgerSeq) {
    this.addSegments("ledgers", String.valueOf(ledgerSeq), "transactions");
    return this;
  }

  public Page<Transaction> execute() throws IOException {
    TypeToken type = new TypeToken<Page<Transaction>>() {};
    ResponseHandler<Page<Transaction>> responseHandler = new ResponseHandler<Page<Transaction>>(type);
    return (Page<Transaction>) Request.Get(this.buildUri()).execute().handleResponse(responseHandler);
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
