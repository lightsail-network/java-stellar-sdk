package org.stellar.sdk.requests;

import okhttp3.HttpUrl;
import org.junit.Test;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;

import static org.junit.Assert.assertEquals;

public class TransactionsRequestBuilderTest {
  @Test
  public void testTransactions() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.transactions()
            .limit(200)
            .order(RequestBuilder.Order.DESC)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/transactions?limit=200&order=desc", uri.toString());
  }

  @Test
  public void testForAccount() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.transactions()
            .forAccount("GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H")
            .limit(200)
            .order(RequestBuilder.Order.DESC)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/accounts/GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H/transactions?limit=200&order=desc", uri.toString());
  }

  @Test
  public void testForLedger() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.transactions()
            .forLedger(200000000000L)
            .limit(50)
            .order(RequestBuilder.Order.ASC)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/ledgers/200000000000/transactions?limit=50&order=asc", uri.toString());
  }

  @Test
  public void testIncludeFailed() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.transactions()
            .forLedger(200000000000L)
            .includeFailed(true)
            .limit(50)
            .order(RequestBuilder.Order.ASC)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/ledgers/200000000000/transactions?include_failed=true&limit=50&order=asc", uri.toString());
  }
}
