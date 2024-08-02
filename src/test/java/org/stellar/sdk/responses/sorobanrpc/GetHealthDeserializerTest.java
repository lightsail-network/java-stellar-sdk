package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class GetHealthDeserializerTest {

  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/get_health.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));

    SorobanRpcResponse<GetHealthResponse> getHealthResponse =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<SorobanRpcResponse<GetHealthResponse>>() {}.getType());
    assertEquals(getHealthResponse.getResult().getStatus(), "healthy");
    assertEquals(getHealthResponse.getResult().getLatestLedger().longValue(), 50000L);
    assertEquals(getHealthResponse.getResult().getOldestLedger().longValue(), 1L);
    assertEquals(getHealthResponse.getResult().getLedgerRetentionWindow().longValue(), 10000L);
  }
}
