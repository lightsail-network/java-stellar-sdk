package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class GetNetworkDeserializerTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/get_network.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SorobanRpcResponse<GetNetworkResponse> getLatestLedgerResponse =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<SorobanRpcResponse<GetNetworkResponse>>() {}.getType());
    assertEquals(
        getLatestLedgerResponse.getResult().getFriendbotUrl(), "http://localhost:8000/friendbot");
    assertEquals(
        getLatestLedgerResponse.getResult().getPassphrase(), "Standalone Network ; February 2017");
    assertEquals(getLatestLedgerResponse.getResult().getProtocolVersion().intValue(), 20);
  }
}
