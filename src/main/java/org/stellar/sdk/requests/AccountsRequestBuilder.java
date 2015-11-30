package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.stellar.base.Keypair;
import org.stellar.sdk.Account;
import org.stellar.sdk.Page;

import java.io.IOException;
import java.net.URI;

public class AccountsRequestBuilder extends RequestBuilder {
  public AccountsRequestBuilder(URI serverURI) {
    super(serverURI, "accounts");
  }

  public Account account(Keypair account) throws IOException {
    TypeToken type = new TypeToken<Account>() {};
    ResponseHandler<Account> responseHandler = new ResponseHandler<Account>(type);
    this.addSegments("accounts", account.getAddress());
    return (Account) Request.Get(this.buildUri()).execute().handleResponse(responseHandler);
  }

  public Page<Account> execute() throws IOException {
    TypeToken type = new TypeToken<Page<Account>>() {};
    ResponseHandler<Page<Account>> responseHandler = new ResponseHandler<Page<Account>>(type);
    return (Page<Account>) Request.Get(this.buildUri()).execute().handleResponse(responseHandler);
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
