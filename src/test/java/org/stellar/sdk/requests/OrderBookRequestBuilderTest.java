package org.stellar.sdk.requests;

import org.junit.Test;
import org.stellar.base.Asset;
import org.stellar.base.Keypair;
import org.stellar.sdk.Server;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class OrderBookRequestBuilderTest {
  @Test
  public void testOrderBook() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    URI uri = server.orderBook()
            .buyingAsset(Asset.createNonNativeAsset("EUR", Keypair.fromAddress("GAUPA4HERNBDPVO4IUA3MJXBCRRK5W54EVXTDK6IIUTGDQRB6D5W242W")))
            .sellingAsset(Asset.createNonNativeAsset("USD", Keypair.fromAddress("GDRRHSJMHXDTQBT4JTCILNGF5AS54FEMTXL7KOLMF6TFTHRK6SSUSUZZ")))
            .cursor("13537736921089")
            .limit(200)
            .order(RequestBuilder.Order.ASC)
            .buildUri();

    assertEquals("https://horizon-testnet.stellar.org/order_book?" +
            "buying_asset_type=credit_alphanum4&" +
            "buying_asset_code=EUR&" +
            "buying_asset_issuer=GAUPA4HERNBDPVO4IUA3MJXBCRRK5W54EVXTDK6IIUTGDQRB6D5W242W&" +
            "selling_asset_type=credit_alphanum4&" +
            "selling_asset_code=USD&" +
            "selling_asset_issuer=GDRRHSJMHXDTQBT4JTCILNGF5AS54FEMTXL7KOLMF6TFTHRK6SSUSUZZ&" +
            "cursor=13537736921089&" +
            "limit=200&" +
            "order=asc", uri.toString());
  }

  @Test
  public void testTrades() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    URI uri = server.orderBook()
            .trades()
            .buyingAsset(Asset.createNonNativeAsset("EUR", Keypair.fromAddress("GAUPA4HERNBDPVO4IUA3MJXBCRRK5W54EVXTDK6IIUTGDQRB6D5W242W")))
            .sellingAsset(Asset.createNonNativeAsset("USD", Keypair.fromAddress("GDRRHSJMHXDTQBT4JTCILNGF5AS54FEMTXL7KOLMF6TFTHRK6SSUSUZZ")))
            .cursor("13537736921089")
            .limit(200)
            .order(RequestBuilder.Order.ASC)
            .buildUri();

    assertEquals("https://horizon-testnet.stellar.org/order_book/trades?" +
            "buying_asset_type=credit_alphanum4&" +
            "buying_asset_code=EUR&" +
            "buying_asset_issuer=GAUPA4HERNBDPVO4IUA3MJXBCRRK5W54EVXTDK6IIUTGDQRB6D5W242W&" +
            "selling_asset_type=credit_alphanum4&" +
            "selling_asset_code=USD&" +
            "selling_asset_issuer=GDRRHSJMHXDTQBT4JTCILNGF5AS54FEMTXL7KOLMF6TFTHRK6SSUSUZZ&" +
            "cursor=13537736921089&" +
            "limit=200&" +
            "order=asc", uri.toString());
  }
}
