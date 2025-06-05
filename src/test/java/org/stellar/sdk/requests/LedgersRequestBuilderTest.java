package org.stellar.sdk.requests;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import org.junit.Test;
import org.stellar.sdk.Server;

public class LedgersRequestBuilderTest {
  @Test
  public void testLedgers() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    URI uri = server.ledgers().limit(200).order(RequestBuilder.Order.ASC).buildUri();
    assertEquals("https://horizon-testnet.stellar.org/ledgers?limit=200&order=asc", uri.toString());
    server.close();
  }
}
