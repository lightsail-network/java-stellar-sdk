package org.stellar.sdk.requests;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URI;
import okhttp3.OkHttpClient;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.operations.OperationResponse;

/**
 * Builds requests connected to operations.
 */
public class OperationsRequestBuilder extends RequestBuilder {

  public OperationsRequestBuilder(OkHttpClient httpClient, URI serverURI) {
    super(httpClient, serverURI, "operations");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link OperationResponse}.
   * This method is helpful for getting the links.
   * @throws IOException
   */
  public OperationResponse operation(URI uri) throws IOException {
    TypeToken type = new TypeToken<OperationResponse>() {};
    ResponseHandler<OperationResponse> responseHandler = new ResponseHandler<OperationResponse>(httpClient, type);
    return responseHandler.handleGetRequest(uri);
  }

  /**
   * Requests <code>GET /operations/{operationId}</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/operations-single.html">Operation Details</a>
   * @param operationId Operation to fetch
   * @throws IOException
   */
  public OperationResponse operation(long operationId) throws IOException {
    this.setSegments("operation", String.valueOf(operationId));
    return this.operation(this.buildUri());
  }

  /**
   * Builds request to <code>GET /accounts/{account}/operations</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/operations-for-account.html">Operations for Account</a>
   * @param account Account for which to get operations
   */
  public OperationsRequestBuilder forAccount(KeyPair account) {
    account = checkNotNull(account, "account cannot be null");
    this.setSegments("accounts", account.getAccountId(), "operations");
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
   * Requests specific <code>uri</code> and returns {@link Page} of {@link OperationResponse}.
   * This method is helpful for getting the next set of results.
   * @return {@link Page} of {@link OperationResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public static Page<OperationResponse> execute(OkHttpClient httpClient, URI uri)
      throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<Page<OperationResponse>>() {};
    ResponseHandler<Page<OperationResponse>> responseHandler = new ResponseHandler<Page<OperationResponse>>(httpClient,
        type);
    return responseHandler.handleGetRequest(uri);
  }

  /**
   * Build and execute request.
   * @return {@link Page} of {@link OperationResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public Page<OperationResponse> execute() throws IOException, TooManyRequestsException {
    return this.execute(httpClient, this.buildUri());
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
