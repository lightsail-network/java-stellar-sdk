package org.stellar.sdk.requests;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.TransactionResponse;

/** Builds requests connected to transactions. */
public class TransactionsRequestBuilder extends RequestBuilder {
  public TransactionsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "transactions");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link TransactionResponse}. This method is
   * helpful for getting the links.
   *
   * @throws IOException
   */
  public TransactionResponse transaction(HttpUrl uri) throws IOException {
    TypeToken type = new TypeToken<TransactionResponse>() {};
    ResponseHandler<TransactionResponse> responseHandler =
        new ResponseHandler<TransactionResponse>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }

  /**
   * Requests <code>GET /transactions/{transactionId}</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/transactions/single/">Transaction
   *     Details</a>
   * @param transactionId Transaction to fetch
   * @throws IOException
   */
  public TransactionResponse transaction(String transactionId) throws IOException {
    this.setSegments("transactions", transactionId);
    return this.transaction(this.buildUri());
  }

  /**
   * Builds request to <code>GET /accounts/{account}/transactions</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/accounts/transactions/">Transactions
   *     for Account</a>
   * @param account Account for which to get transactions
   */
  public TransactionsRequestBuilder forAccount(String account) {
    account = checkNotNull(account, "account cannot be null");
    this.setSegments("accounts", account, "transactions");
    return this;
  }

  /**
   * Builds request to <code>GET /claimable_balances/{claimable_balance_id}/transactions</code>
   *
   * @see <a
   *     href="https://developers.stellar.org/api/resources/claimablebalances/transactions/">Transactions
   *     for ClaimableBalance</a>
   * @param claimableBalance Claimable Balance for which to get transactions
   */
  public TransactionsRequestBuilder forClaimableBalance(String claimableBalance) {
    claimableBalance = checkNotNull(claimableBalance, "claimableBalance cannot be null");
    this.setSegments("claimable_balances", claimableBalance, "transactions");
    return this;
  }

  /**
   * Builds request to <code>GET /ledgers/{ledgerSeq}/transactions</code>
   *
   * @see <a href="https://developers.stellar.org/api/resources/ledgers/transactions/">Transactions
   *     for Ledger</a>
   * @param ledgerSeq Ledger for which to get transactions
   */
  public TransactionsRequestBuilder forLedger(long ledgerSeq) {
    this.setSegments("ledgers", String.valueOf(ledgerSeq), "transactions");
    return this;
  }

  /**
   * Builds request to <code>GET /liquidity_pools/{ledgerSeq}/transactions</code>
   *
   * @see <a
   *     href="https://developers.stellar.org/api/resources/liquiditypools/transactions/">Transactions
   *     for Liquidity Pool</a>
   * @param liquidityPoolID Liquidity pool for which to get transactions
   */
  public TransactionsRequestBuilder forLiquidityPool(LiquidityPoolID liquidityPoolID) {
    return this.forLiquidityPool(String.valueOf(liquidityPoolID));
  }

  /**
   * Builds request to <code>GET /liquidity_pools/{ledgerSeq}/transactions</code>
   *
   * @see <a
   *     href="https://developers.stellar.org/api/resources/liquiditypools/transactions/">Transactions
   *     for Liquidity Pool</a>
   * @param liquidityPoolID Liquidity pool for which to get transactions
   */
  public TransactionsRequestBuilder forLiquidityPool(String liquidityPoolID) {
    this.setSegments("liquidity_pools", liquidityPoolID, "transactions");
    return this;
  }

  /**
   * Adds a parameter defining whether to include failed transactions. By default only successful
   * transactions are returned.
   *
   * @param value Set to <code>true</code> to include failed transactions.
   */
  public TransactionsRequestBuilder includeFailed(boolean value) {
    uriBuilder.setQueryParameter("include_failed", String.valueOf(value));
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link TransactionResponse}.
   * This method is helpful for getting the next set of results.
   *
   * @return {@link Page} of {@link TransactionResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public static Page<TransactionResponse> execute(OkHttpClient httpClient, HttpUrl uri)
      throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<Page<TransactionResponse>>() {};
    ResponseHandler<Page<TransactionResponse>> responseHandler =
        new ResponseHandler<Page<TransactionResponse>>(type);

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
   * @param listener {@link EventListener} implementation with {@link TransactionResponse} type
   * @return EventSource object, so you can <code>close()</code> connection when not needed anymore
   */
  public SSEStream<TransactionResponse> stream(final EventListener<TransactionResponse> listener) {
    return SSEStream.create(httpClient, this, TransactionResponse.class, listener);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link TransactionResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public Page<TransactionResponse> execute() throws IOException, TooManyRequestsException {
    return this.execute(this.httpClient, this.buildUri());
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
