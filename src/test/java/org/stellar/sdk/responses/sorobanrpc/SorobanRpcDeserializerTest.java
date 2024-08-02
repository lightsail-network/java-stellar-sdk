package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class SorobanRpcDeserializerTest {
  @Test
  public void testDeserializeSuccess() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/soroban_rpc_success.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SorobanRpcResponse<GetHealthResponse> sorobanRpcResponse =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<SorobanRpcResponse<GetHealthResponse>>() {}.getType());
    assertEquals(sorobanRpcResponse.getJsonRpc(), "2.0");
    assertEquals(sorobanRpcResponse.getId(), "198cb1a8-9104-4446-a269-88bf000c2721");
    assertNotNull(sorobanRpcResponse.getResult());
    assertNull(sorobanRpcResponse.getError());
  }

  @Test
  public void testDeserializeError() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/soroban_rpc_error.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SorobanRpcResponse<GetHealthResponse> sorobanRpcResponse =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<SorobanRpcResponse<GetHealthResponse>>() {}.getType());
    assertEquals(sorobanRpcResponse.getJsonRpc(), "2.0");
    assertEquals(sorobanRpcResponse.getId(), "e860b82a-1738-4646-a999-b97ea3e117eb");
    assertNull(sorobanRpcResponse.getResult());
    assertNotNull(sorobanRpcResponse.getError());
    assertEquals(sorobanRpcResponse.getError().getCode().longValue(), -32602);
    assertEquals(sorobanRpcResponse.getError().getMessage(), "startLedger must be positive");
  }
}
