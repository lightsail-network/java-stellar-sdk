package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;

import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class GetLatestLedgerDeserializerTest {
  @Test
  public void testDeserialize() {
    SorobanRpcResponse<GetLatestLedgerResponse> getLatestLedgerResponse =
        GsonSingleton.getInstance()
            .fromJson(
                json, new TypeToken<SorobanRpcResponse<GetLatestLedgerResponse>>() {}.getType());
    assertEquals(
        getLatestLedgerResponse.getResult().getId(),
        "e73d7654b72daa637f396669182c6072549736a9e3b6fcb8e685adb61f8c910a");
    assertEquals(getLatestLedgerResponse.getResult().getProtocolVersion().intValue(), 20);
    assertEquals(getLatestLedgerResponse.getResult().getSequence().intValue(), 24170);
  }

  String json =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
          + "    \"result\": {\n"
          + "        \"id\": \"e73d7654b72daa637f396669182c6072549736a9e3b6fcb8e685adb61f8c910a\",\n"
          + "        \"protocolVersion\": \"20\",\n"
          + "        \"sequence\": 24170\n"
          + "    }\n"
          + "}";
}
