package org.stellar.sdk.requests;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.Test;
import org.stellar.sdk.Server;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AccountsRequestBuilderTest {
  @Test
  public void testAccounts() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.accounts()
            .cursor("13537736921089")
            .limit(200)
            .order(RequestBuilder.Order.ASC)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/accounts?cursor=13537736921089&limit=200&order=asc", uri.toString());
  }
}
