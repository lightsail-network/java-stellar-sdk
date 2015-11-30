package org.stellar.sdk.requests;

import org.junit.Test;
import org.stellar.base.Keypair;
import org.stellar.sdk.Server;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class OperationsRequestBuilderTest {
  @Test
  public void testOperations() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    URI uri = server.operations()
            .limit(200)
            .order(RequestBuilder.Order.DESC)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/operations?limit=200&order=desc", uri.toString());
  }

  @Test
  public void testForAccount() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    URI uri = server.operations()
            .forAccount(Keypair.fromAddress("GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H"))
            .limit(200)
            .order(RequestBuilder.Order.DESC)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/accounts/GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H/operations?limit=200&order=desc", uri.toString());
  }

  @Test
  public void testForLedger() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    URI uri = server.operations()
            .forLedger(200000000000L)
            .limit(50)
            .order(RequestBuilder.Order.ASC)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/ledgers/200000000000/operations?limit=50&order=asc", uri.toString());
  }

  @Test
  public void testForTransaction() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    URI uri = server.operations()
            .forTransaction("991534d902063b7715cd74207bef4e7bd7aa2f108f62d3eba837ce6023b2d4f3")
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/transactions/991534d902063b7715cd74207bef4e7bd7aa2f108f62d3eba837ce6023b2d4f3/operations", uri.toString());
  }
}
