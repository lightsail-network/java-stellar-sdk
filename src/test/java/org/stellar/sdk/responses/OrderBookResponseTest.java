package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.AssetTypeNative;

public class OrderBookResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/order_book.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    OrderBookResponse orderBookResponse =
        GsonSingleton.getInstance().fromJson(json, OrderBookResponse.class);

    assertEquals(new AssetTypeNative(), orderBookResponse.getBase());

    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "USDC", "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN"),
        orderBookResponse.getCounter());

    assertEquals(20, orderBookResponse.getBids().length);
    assertEquals("3.4648176", orderBookResponse.getBids()[0].getAmount());
    assertEquals("0.0834113", orderBookResponse.getBids()[0].getPrice());
    assertEquals(17321279L, orderBookResponse.getBids()[0].getPriceR().getNumerator());
    assertEquals(207661034L, orderBookResponse.getBids()[0].getPriceR().getDenominator());

    assertEquals("3999.9999999", orderBookResponse.getBids()[19].getAmount());
    assertEquals("0.0826542", orderBookResponse.getBids()[19].getPrice());
    assertEquals(71L, orderBookResponse.getBids()[19].getPriceR().getNumerator());
    assertEquals(859L, orderBookResponse.getBids()[19].getPriceR().getDenominator());

    assertEquals(20, orderBookResponse.getAsks().length);
    assertEquals("17.0025998", orderBookResponse.getAsks()[0].getAmount());
    assertEquals("0.0835708", orderBookResponse.getAsks()[0].getPrice());
    assertEquals(3900L, orderBookResponse.getAsks()[0].getPriceR().getNumerator());
    assertEquals(46667L, orderBookResponse.getAsks()[0].getPriceR().getDenominator());

    assertEquals("41.2999996", orderBookResponse.getAsks()[19].getAmount());
    assertEquals("0.0847458", orderBookResponse.getAsks()[19].getPrice());
    assertEquals(423729L, orderBookResponse.getAsks()[19].getPriceR().getNumerator());
    assertEquals(5000000L, orderBookResponse.getAsks()[19].getPriceR().getDenominator());
  }
}
