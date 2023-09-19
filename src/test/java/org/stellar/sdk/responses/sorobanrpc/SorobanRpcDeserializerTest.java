package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.stellar.sdk.responses.GsonSingleton;

public class SorobanRpcDeserializerTest {
  @Test
  public void testDeserializeSuccess() {
    SorobanRpcResponse<GetHealthResponse> sorobanRpcResponse =
        GsonSingleton.getInstance()
            .fromJson(
                jsonSuccess, new TypeToken<SorobanRpcResponse<GetHealthResponse>>() {}.getType());
    assertEquals(sorobanRpcResponse.getJsonRpc(), "2.0");
    assertEquals(sorobanRpcResponse.getId(), "198cb1a8-9104-4446-a269-88bf000c2721");
    assertNotNull(sorobanRpcResponse.getResult());
    assertNull(sorobanRpcResponse.getError());
  }

  @Test
  public void testDeserializeError() {
    SorobanRpcResponse<GetHealthResponse> sorobanRpcResponse =
        GsonSingleton.getInstance()
            .fromJson(
                jsonError, new TypeToken<SorobanRpcResponse<GetHealthResponse>>() {}.getType());
    assertEquals(sorobanRpcResponse.getJsonRpc(), "2.0");
    assertEquals(sorobanRpcResponse.getId(), "e860b82a-1738-4646-a999-b97ea3e117eb");
    assertNull(sorobanRpcResponse.getResult());
    assertNotNull(sorobanRpcResponse.getError());
    assertEquals(sorobanRpcResponse.getError().getCode().longValue(), -32602);
    assertEquals(sorobanRpcResponse.getError().getMessage(), "startLedger must be positive");
  }

  String jsonSuccess =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"198cb1a8-9104-4446-a269-88bf000c2721\",\n"
          + "    \"result\": {\n"
          + "        \"status\": \"healthy\"\n"
          + "    }\n"
          + "}";

  String jsonError =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"e860b82a-1738-4646-a999-b97ea3e117eb\",\n"
          + "    \"error\": {\n"
          + "        \"code\": -32602,\n"
          + "        \"message\": \"startLedger must be positive\"\n"
          + "    }\n"
          + "}";
}
