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
 * Builds requests connected to operations.
 */
public class OperationsRequestBuilder extends RequestBuilder {
  public OperationsRequestBuilder(URI serverURI) {
    super(serverURI, "operations");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Operation}.
   * This method is helpful for getting the links.
   * @throws IOException
   */
  public Operation operation(URI uri) throws IOException {
    TypeToken type = new TypeToken<Operation>() {};
    ResponseHandler<Operation> responseHandler = new ResponseHandler<Operation>(type);
    return (Operation) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  /**
   * Requests <code>GET /operations/{operationId}</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/operations-single.html">Operation Details</a>
   * @param operationId Operation to fetch
   * @throws IOException
   */
  public Operation operation(long operationId) throws IOException {
    this.setSegments("operation", String.valueOf(operationId));
    return this.operation(this.buildUri());
  }

  /**
   * Builds request to <code>GET /accounts/{account}/operations</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/operations-for-account.html">Operations for Account</a>
   * @param account Account for which to get operations
   */
  public OperationsRequestBuilder forAccount(Keypair account) {
    account = checkNotNull(account, "account cannot be null");
    this.setSegments("accounts", account.getAddress(), "operations");
    return this;
  }

  /**
   * Builds request to <code>GET /ledgers/{ledgerSeq}/operations</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/operations-for-ledger.html">Operations for Ledger</a>
   * @param ledgerSeq Ledger for which to get operations
   */
  public OperationsRequestBuilder forLedger(long ledgerSeq) {
    this.setSegments("ledgers", String.valueOf(ledgerSeq), "operations");
    return this;
  }

  /**
   * Builds request to <code>GET /transactions/{transactionId}/operations</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/operations-for-transaction.html">Operations for Transaction</a>
   * @param transactionId Transaction ID for which to get operations
   */
  public OperationsRequestBuilder forTransaction(String transactionId) {
    transactionId = checkNotNull(transactionId, "transactionId cannot be null");
    this.setSegments("transactions", transactionId, "operations");
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link Operation}.
   * This method is helpful for getting the next set of results.
   * @return {@link Page} of {@link Operation}
   * @throws IOException
   */
  public static Page<Operation> execute(URI uri) throws IOException {
    TypeToken type = new TypeToken<Page<Operation>>() {};
    ResponseHandler<Page<Operation>> responseHandler = new ResponseHandler<Page<Operation>>(type);
    return (Page<Operation>) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  /**
   * Build and execute request.
   * @return {@link Page} of {@link Operation}
   * @throws IOException
   */
  public Page<Operation> execute() throws IOException {
    return this.execute(this.buildUri());
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
