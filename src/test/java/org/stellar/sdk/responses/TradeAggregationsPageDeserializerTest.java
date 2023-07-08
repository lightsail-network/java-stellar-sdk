package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Test;

public class TradeAggregationsPageDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    Page<TradeAggregationResponse> page =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<Page<TradeAggregationResponse>>() {}.getType());

    assertEquals(
        page.getLinks().getSelf().getHref(),
        "https://horizon.stellar.org/trade_aggregations?base_asset_type=native&start_time=1512689100000&counter_asset_issuer=GATEMHCCKCY67ZUCKTROYN24ZYT5GK4EQZ65JJLDHKHRUZI3EUEKMTCH&limit=200&end_time=1512775500000&counter_asset_type=credit_alphanum4&resolution=300000&order=asc&counter_asset_code=BTC");
    assertEquals(
        page.getLinks().getNext().getHref(),
        "https://horizon.stellar.org/trade_aggregations?base_asset_type=native&counter_asset_code=BTC&counter_asset_issuer=GATEMHCCKCY67ZUCKTROYN24ZYT5GK4EQZ65JJLDHKHRUZI3EUEKMTCH&counter_asset_type=credit_alphanum4&end_time=1512775500000&limit=200&order=asc&resolution=300000&start_time=1512765000000");

    assertEquals(page.getRecords().get(0).getTimestamp(), 1512731100000L);
    assertEquals(page.getRecords().get(0).getTradeCount(), 2);
    assertEquals(page.getRecords().get(0).getBaseVolume(), "341.8032786");
    assertEquals(page.getRecords().get(0).getCounterVolume(), "0.0041700");
    assertEquals(page.getRecords().get(0).getAvg(), "0.0000122");
    assertEquals(page.getRecords().get(0).getHigh(), "0.0000123");
    assertEquals(page.getRecords().get(0).getLow(), "0.0000124");
    assertEquals(page.getRecords().get(0).getOpen(), "0.0000125");
    assertEquals(page.getRecords().get(0).getClose(), "0.0000126");
  }

  String json =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/trade_aggregations?base_asset_type=native\\u0026start_time=1512689100000\\u0026counter_asset_issuer=GATEMHCCKCY67ZUCKTROYN24ZYT5GK4EQZ65JJLDHKHRUZI3EUEKMTCH\\u0026limit=200\\u0026end_time=1512775500000\\u0026counter_asset_type=credit_alphanum4\\u0026resolution=300000\\u0026order=asc\\u0026counter_asset_code=BTC\"\n"
          + "    },\n"
          + "    \"next\": {\n"
          + "      \"href\": \"https://horizon.stellar.org/trade_aggregations?base_asset_type=native\\u0026counter_asset_code=BTC\\u0026counter_asset_issuer=GATEMHCCKCY67ZUCKTROYN24ZYT5GK4EQZ65JJLDHKHRUZI3EUEKMTCH\\u0026counter_asset_type=credit_alphanum4\\u0026end_time=1512775500000\\u0026limit=200\\u0026order=asc\\u0026resolution=300000\\u0026start_time=1512765000000\"\n"
          + "    }\n"
          + "  },\n"
          + "  \"_embedded\": {\n"
          + "    \"records\": [\n"
          + "      {\n"
          + "        \"timestamp\": 1512731100000,\n"
          + "        \"trade_count\": 2,\n"
          + "        \"base_volume\": \"341.8032786\",\n"
          + "        \"counter_volume\": \"0.0041700\",\n"
          + "        \"avg\": \"0.0000122\",\n"
          + "        \"high\": \"0.0000123\",\n"
          + "        \"low\": \"0.0000124\",\n"
          + "        \"open\": \"0.0000125\",\n"
          + "        \"close\": \"0.0000126\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"timestamp\": 1512732300000,\n"
          + "        \"trade_count\": 1,\n"
          + "        \"base_volume\": \"233.6065573\",\n"
          + "        \"counter_volume\": \"0.0028500\",\n"
          + "        \"avg\": \"0.0000122\",\n"
          + "        \"high\": \"0.0000122\",\n"
          + "        \"low\": \"0.0000122\",\n"
          + "        \"open\": \"0.0000122\",\n"
          + "        \"close\": \"0.0000122\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"timestamp\": 1512764700000,\n"
          + "        \"trade_count\": 1,\n"
          + "        \"base_volume\": \"451.0000000\",\n"
          + "        \"counter_volume\": \"0.0027962\",\n"
          + "        \"avg\": \"0.0000062\",\n"
          + "        \"high\": \"0.0000062\",\n"
          + "        \"low\": \"0.0000062\",\n"
          + "        \"open\": \"0.0000062\",\n"
          + "        \"close\": \"0.0000062\"\n"
          + "      }\n"
          + "    ]\n"
          + "  }\n"
          + "}";
}
