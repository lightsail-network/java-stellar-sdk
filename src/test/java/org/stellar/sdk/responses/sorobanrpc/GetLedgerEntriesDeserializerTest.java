package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import org.junit.Test;
import org.stellar.sdk.responses.GsonSingleton;

public class GetLedgerEntriesDeserializerTest {

  @Test
  public void testDeserializeEntriesNotNull() throws IOException {
    SorobanRpcResponse<GetLedgerEntriesResponse> getLedgerEntriesResponse =
        GsonSingleton.getInstance()
            .fromJson(
                getJsonEntriesNotNull,
                new TypeToken<SorobanRpcResponse<GetLedgerEntriesResponse>>() {}.getType());
    assertEquals(getLedgerEntriesResponse.getResult().getLatestLedger().longValue(), 1457);
    assertEquals(getLedgerEntriesResponse.getResult().getEntries().size(), 1);
    assertEquals(
        getLedgerEntriesResponse.getResult().getEntries().get(0).getKey(),
        "AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JA==");
    assertEquals(
        getLedgerEntriesResponse.getResult().getEntries().get(0).getXdr(),
        "AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JAAAABdIcjmeAAAAfgAAAAgAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAADAAAAAAAAArcAAAAAZMIW+A==");
    assertEquals(
        getLedgerEntriesResponse
            .getResult()
            .getEntries()
            .get(0)
            .getLastModifiedLedger()
            .longValue(),
        695);
    assertEquals(
        getLedgerEntriesResponse.getResult().getEntries().get(0).getLiveUntilLedger().longValue(),
        996);
    assertEquals(
        getLedgerEntriesResponse.getResult().getEntries().get(0).parseXdr().toXdrBase64(),
        getLedgerEntriesResponse.getResult().getEntries().get(0).getXdr());
    assertEquals(
        getLedgerEntriesResponse.getResult().getEntries().get(0).parseKey().toXdrBase64(),
        getLedgerEntriesResponse.getResult().getEntries().get(0).getKey());
  }

  @Test
  public void testDeserializeEntriesNull() {
    SorobanRpcResponse<GetLedgerEntriesResponse> getLedgerEntriesResponse =
        GsonSingleton.getInstance()
            .fromJson(
                jsonEntriesNull,
                new TypeToken<SorobanRpcResponse<GetLedgerEntriesResponse>>() {}.getType());
    assertNull(getLedgerEntriesResponse.getResult().getEntries());
    assertEquals(getLedgerEntriesResponse.getResult().getLatestLedger().longValue(), 1009L);
  }

  String getJsonEntriesNotNull =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"44dc79a1b2734eb79d28fe8806345b39\",\n"
          + "    \"result\": {\n"
          + "        \"entries\": [\n"
          + "            {\n"
          + "                \"key\": \"AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JA==\",\n"
          + "                \"xdr\": \"AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JAAAABdIcjmeAAAAfgAAAAgAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAADAAAAAAAAArcAAAAAZMIW+A==\",\n"
          + "                \"lastModifiedLedgerSeq\": \"695\",\n"
          + "                \"liveUntilLedgerSeq\": \"996\"\n"
          + "            }\n"
          + "        ],\n"
          + "        \"latestLedger\": \"1457\"\n"
          + "    }\n"
          + "}";

  String jsonEntriesNull =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": 8675309,\n"
          + "    \"result\": {\n"
          + "        \"entries\": null,\n"
          + "        \"latestLedger\": \"1009\"\n"
          + "    }\n"
          + "}";
}
