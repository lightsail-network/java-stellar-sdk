package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.stellar.base.Keypair;
import org.stellar.sdk.Page;
import org.stellar.sdk.operations.Operation;

import java.io.IOException;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds requests connected to payments.
 */
public class PaymentsRequestBuilder extends RequestBuilder {
  public PaymentsRequestBuilder(URI serverURI) {
    super(serverURI, "payments");
  }

  public PaymentsRequestBuilder forAccount(Keypair account) {
    account = checkNotNull(account, "account cannot be null");
    this.setSegments("accounts", account.getAddress(), "payments");
    return this;
  }

  public PaymentsRequestBuilder forLedger(long ledgerSeq) {
    this.setSegments("ledgers", String.valueOf(ledgerSeq), "payments");
    return this;
  }

  public PaymentsRequestBuilder forTransaction(String transactionId) {
    transactionId = checkNotNull(transactionId, "transactionId cannot be null");
    this.setSegments("transactions", transactionId, "payments");
    return this;
  }

  public Page<Operation> execute() throws IOException {
    TypeToken type = new TypeToken<Page<Operation>>() {};
    ResponseHandler<Page<Operation>> responseHandler = new ResponseHandler<Page<Operation>>(type);
    return (Page<Operation>) Request.Get(this.buildUri()).execute().handleResponse(responseHandler);
  }

  @Override
  public PaymentsRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public PaymentsRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public PaymentsRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
