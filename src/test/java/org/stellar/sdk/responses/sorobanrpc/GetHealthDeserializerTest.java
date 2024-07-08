package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;

import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class GetHealthDeserializerTest {

  @Test
  public void testDeserialize() {
    SorobanRpcResponse<GetHealthResponse> getHealthResponse =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<SorobanRpcResponse<GetHealthResponse>>() {}.getType());
    assertEquals(getHealthResponse.getResult().getStatus(), "healthy");
    assertEquals(getHealthResponse.getResult().getLatestLedger().longValue(), 50000L);
    assertEquals(getHealthResponse.getResult().getOldestLedger().longValue(), 1L);
    assertEquals(getHealthResponse.getResult().getLedgerRetentionWindow().longValue(), 10000L);
  }

  String json =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
          + "    \"result\": {\n"
          + "        \"status\": \"healthy\",\n"
          + "        \"latestLedger\": 50000,\n"
          + "        \"oldestLedger\": 1,\n"
          + "        \"ledgerRetentionWindow\": 10000\n"
          + "    }\n"
          + "}";
}
