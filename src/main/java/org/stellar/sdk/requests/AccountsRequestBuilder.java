package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.stellar.base.Keypair;
import org.stellar.sdk.Account;
import org.stellar.sdk.Page;

import java.io.IOException;
import java.net.URI;

/**
 * Builds requests connected to accounts.
 */
public class AccountsRequestBuilder extends RequestBuilder {
  public AccountsRequestBuilder(URI serverURI) {
    super(serverURI, "accounts");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Account}.
   * This method is helpful for getting the links.
   * @throws IOException
   */
  public Account account(URI uri) throws IOException {
    TypeToken type = new TypeToken<Account>() {};
    ResponseHandler<Account> responseHandler = new ResponseHandler<Account>(type);
    return (Account) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  /**
   * Requests <code>GET /accounts/{account}</code>
   * @see <a href="https://www.stellar.org/developers/horizon/reference/accounts-single.html">Account Details</a>
   * @param account Account to fetch
   * @throws IOException
   */
  public Account account(Keypair account) throws IOException {
    this.setSegments("accounts", account.getAddress());
    return this.account(this.buildUri());
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link Account}.
   * This method is helpful for getting the next set of results.
   * @return {@link Page} of {@link Account}
   * @throws IOException
   */
  public static Page<Account> execute(URI uri) throws IOException {
    TypeToken type = new TypeToken<Page<Account>>() {};
    ResponseHandler<Page<Account>> responseHandler = new ResponseHandler<Page<Account>>(type);
    return (Page<Account>) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  /**
   * Build and execute request. <strong>Warning!</strong> {@link Account}s in {@link Page} will contain only <code>keypair</code> field.
   * @return {@link Page} of {@link Account}
   * @throws IOException
   */
  public Page<Account> execute() throws IOException {
    return this.execute(this.buildUri());
  }

  @Override
  public AccountsRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public AccountsRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public AccountsRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
