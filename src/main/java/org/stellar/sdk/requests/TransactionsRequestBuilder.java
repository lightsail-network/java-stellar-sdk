package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.stellar.base.Keypair;
import org.stellar.sdk.Page;
import org.stellar.sdk.Transaction;

import java.io.IOException;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds requests connected to transactions.
 */
public class TransactionsRequestBuilder extends RequestBuilder {
  public TransactionsRequestBuilder(URI serverURI) {
    super(serverURI, "transactions");
  }

  public Transaction transaction(URI uri) throws IOException {
    TypeToken type = new TypeToken<Transaction>() {};
    ResponseHandler<Transaction> responseHandler = new ResponseHandler<Transaction>(type);
    return (Transaction) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  public Transaction transaction(String transactionId) throws IOException {
    this.setSegments("transaction", transactionId);
    return this.transaction(this.buildUri());
  }

  public TransactionsRequestBuilder forAccount(Keypair account) {
    account = checkNotNull(account, "account cannot be null");
    this.setSegments("accounts", account.getAddress(), "transactions");
    return this;
  }

  public TransactionsRequestBuilder forLedger(long ledgerSeq) {
    this.setSegments("ledgers", String.valueOf(ledgerSeq), "transactions");
    return this;
  }

  public static Page<Transaction> execute(URI uri) throws IOException {
    TypeToken type = new TypeToken<Page<Transaction>>() {};
    ResponseHandler<Page<Transaction>> responseHandler = new ResponseHandler<Page<Transaction>>(type);
    return (Page<Transaction>) Request.Get(uri).execute().handleResponse(responseHandler);
  }

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
