package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class GetLatestLedgerDeserializerTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/get_latest_ledger.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));

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
}
