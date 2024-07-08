package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class TradeAggregationResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/trade_aggregation.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TradeAggregationResponse tradeAggregationResponse =
        GsonSingleton.getInstance().fromJson(json, TradeAggregationResponse.class);
    assertEquals(1612483200000L, tradeAggregationResponse.getTimestamp().longValue());
    assertEquals(1403, tradeAggregationResponse.getTradeCount().intValue());
    assertEquals("318596.2535734", tradeAggregationResponse.getBaseVolume());
    assertEquals("109698.4067875", tradeAggregationResponse.getCounterVolume());
    assertEquals("0.3443179", tradeAggregationResponse.getAvg());

    assertEquals("0.3617180", tradeAggregationResponse.getHigh());
    assertEquals(356909700, tradeAggregationResponse.getHighR().getNumerator());
    assertEquals(986707000, tradeAggregationResponse.getHighR().getDenominator());

    assertEquals("0.3257225", tradeAggregationResponse.getLow());
    assertEquals(130289, tradeAggregationResponse.getLowR().getNumerator());
    assertEquals(400000, tradeAggregationResponse.getLowR().getDenominator());

    assertEquals("0.3268569", tradeAggregationResponse.getOpen());
    assertEquals(548699500, tradeAggregationResponse.getOpenR().getNumerator());
    assertEquals(1678715000, tradeAggregationResponse.getOpenR().getDenominator());

    assertEquals("0.3517116", tradeAggregationResponse.getClose());
    assertEquals(695836822, tradeAggregationResponse.getCloseR().getNumerator());
    assertEquals(1978430375, tradeAggregationResponse.getCloseR().getDenominator());
  }
}
