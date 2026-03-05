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
        "98908807544d6658094c94e00c1cff99e341b96ee1c59abbda39d5f4a3b20cdc");
    assertEquals(getLatestLedgerResponse.getResult().getProtocolVersion().intValue(), 25);
    assertEquals(getLatestLedgerResponse.getResult().getSequence().intValue(), 1283415);
    assertEquals(getLatestLedgerResponse.getResult().getCloseTime().longValue(), 1772416629L);
    assertEquals(
        getLatestLedgerResponse.getResult().parseHeaderXdr().toXdrBase64(),
        getLatestLedgerResponse.getResult().getHeaderXdr());
    assertEquals(
        getLatestLedgerResponse.getResult().parseMetadataXdr().toXdrBase64(),
        getLatestLedgerResponse.getResult().getMetadataXdr());
  }
}
