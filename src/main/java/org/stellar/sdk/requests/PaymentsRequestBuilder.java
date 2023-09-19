package org.stellar.sdk.requests;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.Set;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.operations.OperationResponse;

/** Builds requests connected to payments. */
public class PaymentsRequestBuilder extends RequestBuilder {
  protected Set<String> toJoin;

  public PaymentsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "payments");
    toJoin = Sets.newHashSet();
  }

  /**
   * Builds request to <code>GET /accounts/{account}/payments</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/accounts/payments/">Payments for
   *     Account</a>
   * @param account Account for which to get payments
   */
  public PaymentsRequestBuilder forAccount(String account) {
    account = checkNotNull(account, "account cannot be null");
    this.setSegments("accounts", account, "payments");
    return this;
  }

  /**
   * Builds request to <code>GET /ledgers/{ledgerSeq}/payments</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/ledgers/payments/">Payments for
   *     Ledger</a>
   * @param ledgerSeq Ledger for which to get payments
   */
  public PaymentsRequestBuilder forLedger(long ledgerSeq) {
    this.setSegments("ledgers", String.valueOf(ledgerSeq), "payments");
    return this;
  }

  /**
   * Builds request to <code>GET /transactions/{transactionId}/payments</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/transactions/payments/">Payments for
   *     Transaction</a>
   * @param transactionId Transaction ID for which to get payments
   */
  public PaymentsRequestBuilder forTransaction(String transactionId) {
    transactionId = checkNotNull(transactionId, "transactionId cannot be null");
    this.setSegments("transactions", transactionId, "payments");
    return this;
  }

  /**
   * Adds a parameter defining whether to include transactions in the response. By default
   * transaction data is not included.
   *
   * @param include Set to <code>true</code> to include transaction data in the operations response.
   */
  public PaymentsRequestBuilder includeTransactions(boolean include) {
    updateToJoin("transactions", include);
    return this;
  }

  protected void updateToJoin(String value, boolean include) {
    if (include) {
      toJoin.add(value);
    } else {
      toJoin.remove(value);
    }
    if (toJoin.isEmpty()) {
      uriBuilder.removeAllQueryParameters("join");
    } else {
      uriBuilder.setQueryParameter("join", Joiner.on(",").join(toJoin));
    }
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link OperationResponse}. This
   * method is helpful for getting the next set of results.
   *
   * @return {@link Page} of {@link OperationResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public static Page<OperationResponse> execute(OkHttpClient httpClient, HttpUrl uri)
      throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<Page<OperationResponse>>() {};
    ResponseHandler<Page<OperationResponse>> responseHandler =
        new ResponseHandler<Page<OperationResponse>>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }

  /**
   * Allows to stream SSE events from horizon. Certain endpoints in Horizon can be called in
   * streaming mode using Server-Sent Events. This mode will keep the connection to horizon open and
   * horizon will continue to return responses as ledgers close.
   *
   * @see <a href="http://www.w3.org/TR/eventsource/" target="_blank">Server-Sent Events</a>
   * @see <a href="https://developers.stellar.org/api/introduction/response-format/"
   *     target="_blank">Response Format documentation</a>
   * @param listener {@link EventListener} implementation with {@link OperationResponse} type
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<OperationResponse> stream(final EventListener<OperationResponse> listener) {
    return SSEStream.create(httpClient, this, OperationResponse.class, listener);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link OperationResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public Page<OperationResponse> execute() throws IOException, TooManyRequestsException {
    return this.execute(this.httpClient, this.buildUri());
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
