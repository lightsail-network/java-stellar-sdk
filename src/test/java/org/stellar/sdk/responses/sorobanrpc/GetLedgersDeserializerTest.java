package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class GetLedgersDeserializerTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/get_ledgers.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));

    SorobanRpcResponse<GetLedgersResponse> getLedgersResponseSorobanRpcResponse =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<SorobanRpcResponse<GetLedgersResponse>>() {}.getType());

    GetLedgersResponse getLedgersResponse = getLedgersResponseSorobanRpcResponse.getResult();
    assertEquals(getLedgersResponse.getLatestLedger().longValue(), 113L);
    assertEquals(getLedgersResponse.getLatestLedgerCloseTime().longValue(), 1731554518L);
    assertEquals(getLedgersResponse.getOldestLedger().longValue(), 8L);
    assertEquals(getLedgersResponse.getOldestLedgerCloseTime().longValue(), 1731554412L);
    assertEquals(getLedgersResponse.getCursor(), "11");
    assertEquals(getLedgersResponse.getLedgers().size(), 2);
    assertEquals(
        getLedgersResponse.getLedgers().get(0).getHash(),
        "59ccafc5641a44826608a882da10b08f585b2b614be91976ade3927d4422413d");
    assertEquals(getLedgersResponse.getLedgers().get(0).getSequence().longValue(), 10L);
    assertEquals(
        getLedgersResponse.getLedgers().get(0).getLedgerCloseTime().longValue(), 1731554414L);
    for (GetLedgersResponse.LedgerInfo ledgerInfo : getLedgersResponse.getLedgers()) {
      assertEquals(ledgerInfo.getHeaderXdr(), ledgerInfo.parseHeaderXdr().toXdrBase64());
      assertEquals(ledgerInfo.getMetadataXdr(), ledgerInfo.parseMetadataXdr().toXdrBase64());
    }
  }
}
