package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.responses.gson.GsonSingleton;
import org.stellar.sdk.xdr.LedgerEntry;

public class GetLedgerEntriesDeserializerTest {

  @Test
  public void testDeserializeEntriesNotNull() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/get_ledger_entries_not_null.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SorobanRpcResponse<GetLedgerEntriesResponse> getLedgerEntriesResponse =
        GsonSingleton.getInstance()
            .fromJson(
                json, new TypeToken<SorobanRpcResponse<GetLedgerEntriesResponse>>() {}.getType());
    assertEquals(getLedgerEntriesResponse.getResult().getLatestLedger().longValue(), 1457);
    assertEquals(getLedgerEntriesResponse.getResult().getEntries().size(), 1);
    assertEquals(
        getLedgerEntriesResponse.getResult().getEntries().get(0).getKey(),
        "AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JA==");
    assertEquals(
        getLedgerEntriesResponse.getResult().getEntries().get(0).getXdr(),
        "AAAAAAAAAADBPp7TMinJylnn+6dQXJACNc15LF+aJ2Py1BaR4P10JAAAABdIcjmeAAAAfgAAAAgAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAAAADAAAAAAAAArcAAAAAZMIW+A==");
    assertEquals(
        getLedgerEntriesResponse
            .getResult()
            .getEntries()
            .get(0)
            .getLastModifiedLedger()
            .longValue(),
        695);
    assertEquals(
        getLedgerEntriesResponse.getResult().getEntries().get(0).getLiveUntilLedger().longValue(),
        996);
    assertEquals(
        getLedgerEntriesResponse.getResult().getEntries().get(0).parseXdr().toXdrBase64(),
        getLedgerEntriesResponse.getResult().getEntries().get(0).getXdr());
    assertEquals(
        getLedgerEntriesResponse.getResult().getEntries().get(0).parseKey().toXdrBase64(),
        getLedgerEntriesResponse.getResult().getEntries().get(0).getKey());

    // The entry is sponsored, which only its extension says.
    LedgerEntry.LedgerEntryExt ext =
        getLedgerEntriesResponse.getResult().getEntries().get(0).parseExtXdr();
    assertEquals(ext.getDiscriminant().intValue(), 1);
    assertEquals(
        KeyPair.fromXdrPublicKey(
                ext.getV1().getSponsoringID().getSponsorshipDescriptor().getAccountID())
            .getAccountId(),
        "GCZJ46F2ENQAEEZVVY4RKTGVXWZNETKZBZ4LIW5CMUF7AHP6M7BEV464");
  }

  @Test
  public void testDeserializeEntriesNull() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/get_ledger_entries_null.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SorobanRpcResponse<GetLedgerEntriesResponse> getLedgerEntriesResponse =
        GsonSingleton.getInstance()
            .fromJson(
                json, new TypeToken<SorobanRpcResponse<GetLedgerEntriesResponse>>() {}.getType());
    assertNull(getLedgerEntriesResponse.getResult().getEntries());
    assertEquals(getLedgerEntriesResponse.getResult().getLatestLedger().longValue(), 1009L);
  }
}
