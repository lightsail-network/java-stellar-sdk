package org.stellar.sdk.requests;

import okhttp3.HttpUrl;
import org.junit.Test;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Server;

import static org.junit.Assert.assertEquals;

public class EffectsRequestBuilderTest {
  @Test
  public void testEffects() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.effects()
            .limit(200)
            .order(RequestBuilder.Order.DESC)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/effects?limit=200&order=desc", uri.toString());
  }

  @Test
  public void testForAccount() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.effects()
            .forAccount(KeyPair.fromAccountId("GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H"))
            .limit(200)
            .order(RequestBuilder.Order.DESC)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/accounts/GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H/effects?limit=200&order=desc", uri.toString());
  }

  @Test
  public void testForLedger() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.effects()
            .forLedger(200000000000L)
            .limit(50)
            .order(RequestBuilder.Order.ASC)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/ledgers/200000000000/effects?limit=50&order=asc", uri.toString());
  }

  @Test
  public void testForTransaction() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.effects()
            .forTransaction("991534d902063b7715cd74207bef4e7bd7aa2f108f62d3eba837ce6023b2d4f3")
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/transactions/991534d902063b7715cd74207bef4e7bd7aa2f108f62d3eba837ce6023b2d4f3/effects", uri.toString());
  }

  @Test
  public void testForOperation() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.effects()
            .forOperation(28798257847L)
            .cursor("85794837")
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/operations/28798257847/effects?cursor=85794837", uri.toString());
  }
}
