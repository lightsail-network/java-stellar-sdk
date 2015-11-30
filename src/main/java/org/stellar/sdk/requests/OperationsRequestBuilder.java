package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.stellar.base.Keypair;
import org.stellar.sdk.Page;
import org.stellar.sdk.operations.Operation;

import java.io.IOException;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

public class OperationsRequestBuilder extends RequestBuilder {
  public OperationsRequestBuilder(URI serverURI) {
    super(serverURI, "operations");
  }

  public Operation operation(long operationId) throws IOException {
    TypeToken type = new TypeToken<Operation>() {};
    ResponseHandler<Operation> responseHandler = new ResponseHandler<Operation>(type);
    this.addSegments("operation", String.valueOf(operationId));
    return (Operation) Request.Get(this.buildUri()).execute().handleResponse(responseHandler);
  }

  public OperationsRequestBuilder forAccount(Keypair account) {
    account = checkNotNull(account, "account cannot be null");
    this.addSegments("accounts", account.getAddress(), "operations");
    return this;
  }

  public OperationsRequestBuilder forLedger(long ledgerSeq) {
    this.addSegments("ledgers", String.valueOf(ledgerSeq), "operations");
    return this;
  }

  public OperationsRequestBuilder forTransaction(String transactionId) {
    transactionId = checkNotNull(transactionId, "transactionId cannot be null");
    this.addSegments("transactions", transactionId, "operations");
    return this;
  }

  public Page<Operation> execute() throws IOException {
    TypeToken type = new TypeToken<Page<Operation>>() {};
    ResponseHandler<Page<Operation>> responseHandler = new ResponseHandler<Page<Operation>>(type);
    return (Page<Operation>) Request.Get(this.buildUri()).execute().handleResponse(responseHandler);
  }

  @Override
  public OperationsRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public OperationsRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public OperationsRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
