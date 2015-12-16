package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.stellar.base.Keypair;
import org.stellar.sdk.Page;
import org.stellar.sdk.effects.Effect;

import java.io.IOException;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds requests connected to effects.
 */
public class EffectsRequestBuilder extends RequestBuilder {
  public EffectsRequestBuilder(URI serverURI) {
    super(serverURI, "effects");
  }

  /**
   * Builds request to <code>GET /accounts/{account}/effects</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/effects-for-account.html">Effects for Account</a>
   * @param account Account for which to get effects
   */
  public EffectsRequestBuilder forAccount(Keypair account) {
    account = checkNotNull(account, "account cannot be null");
    this.setSegments("accounts", account.getAddress(), "effects");
    return this;
  }

  /**
   * Builds request to <code>GET /ledgers/{ledgerSeq}/effects</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/effects-for-ledger.html">Effects for Ledger</a>
   * @param ledgerSeq Ledger for which to get effects
   */
  public EffectsRequestBuilder forLedger(long ledgerSeq) {
    this.setSegments("ledgers", String.valueOf(ledgerSeq), "effects");
    return this;
  }

  /**
   * Builds request to <code>GET /transactions/{transactionId}/effects</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/effects-for-transaction.html">Effect for Transaction</a>
   * @param transactionId Transaction ID for which to get effects
   */
  public EffectsRequestBuilder forTransaction(String transactionId) {
    transactionId = checkNotNull(transactionId, "transactionId cannot be null");
    this.setSegments("transactions", transactionId, "effects");
    return this;
  }

  /**
   * Builds request to <code>GET /operation/{operationId}/effects</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/effects-for-operation.html">Effect for Operation</a>
   * @param operationId Operation ID for which to get effects
   */
  public EffectsRequestBuilder forOperation(long operationId) {
    this.setSegments("operations", String.valueOf(operationId), "effects");
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link Effect}.
   * This method is helpful for getting the next set of results.
   * @return {@link Page} of {@link Effect}
   * @throws IOException
   */
  public static Page<Effect> execute(URI uri) throws IOException {
    TypeToken type = new TypeToken<Page<Effect>>() {};
    ResponseHandler<Page<Effect>> responseHandler = new ResponseHandler<Page<Effect>>(type);
    return (Page<Effect>) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  /**
   * Build and execute request.
   * @return {@link Page} of {@link Effect}
   * @throws IOException
   */
  public Page<Effect> execute() throws IOException {
    return this.execute(this.buildUri());
  }

  @Override
  public EffectsRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public EffectsRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public EffectsRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
