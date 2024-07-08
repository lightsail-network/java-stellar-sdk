package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;

import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class GetNetworkDeserializerTest {
  @Test
  public void testDeserialize() {
    SorobanRpcResponse<GetNetworkResponse> getLatestLedgerResponse =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<SorobanRpcResponse<GetNetworkResponse>>() {}.getType());
    assertEquals(
        getLatestLedgerResponse.getResult().getFriendbotUrl(), "http://localhost:8000/friendbot");
    assertEquals(
        getLatestLedgerResponse.getResult().getPassphrase(), "Standalone Network ; February 2017");
    assertEquals(getLatestLedgerResponse.getResult().getProtocolVersion().intValue(), 20);
  }

  String json =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
          + "    \"result\": {\n"
          + "        \"friendbotUrl\": \"http://localhost:8000/friendbot\",\n"
          + "        \"passphrase\": \"Standalone Network ; February 2017\",\n"
          + "        \"protocolVersion\": \"20\"\n"
          + "    }\n"
          + "}";
}
