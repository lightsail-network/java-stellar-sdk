package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class FeeStatsResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/fee_stats.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    FeeStatsResponse feeStatsResponse =
        GsonSingleton.getInstance().fromJson(json, FeeStatsResponse.class);

    assertEquals(35068310L, feeStatsResponse.getLastLedger().longValue());
    assertEquals(100L, feeStatsResponse.getLastLedgerBaseFee().longValue());
    assertEquals("0.16", feeStatsResponse.getLedgerCapacityUsage());

    assertEquals(500L, feeStatsResponse.getFeeCharged().getMax().longValue());
    assertEquals(100L, feeStatsResponse.getFeeCharged().getMin().longValue());
    assertEquals(100L, feeStatsResponse.getFeeCharged().getMode().longValue());
    assertEquals(100L, feeStatsResponse.getFeeCharged().getP10().longValue());
    assertEquals(100L, feeStatsResponse.getFeeCharged().getP20().longValue());
    assertEquals(100L, feeStatsResponse.getFeeCharged().getP30().longValue());
    assertEquals(100L, feeStatsResponse.getFeeCharged().getP40().longValue());
    assertEquals(100L, feeStatsResponse.getFeeCharged().getP50().longValue());
    assertEquals(100L, feeStatsResponse.getFeeCharged().getP60().longValue());
    assertEquals(100L, feeStatsResponse.getFeeCharged().getP70().longValue());
    assertEquals(100L, feeStatsResponse.getFeeCharged().getP80().longValue());
    assertEquals(100L, feeStatsResponse.getFeeCharged().getP90().longValue());
    assertEquals(100L, feeStatsResponse.getFeeCharged().getP95().longValue());
    assertEquals(100L, feeStatsResponse.getFeeCharged().getP99().longValue());

    assertEquals(15000L, feeStatsResponse.getMaxFee().getMax().longValue());
    assertEquals(100L, feeStatsResponse.getMaxFee().getMin().longValue());
    assertEquals(1000L, feeStatsResponse.getMaxFee().getMode().longValue());
    assertEquals(100L, feeStatsResponse.getMaxFee().getP10().longValue());
    assertEquals(100L, feeStatsResponse.getMaxFee().getP20().longValue());
    assertEquals(120L, feeStatsResponse.getMaxFee().getP30().longValue());
    assertEquals(300L, feeStatsResponse.getMaxFee().getP40().longValue());
    assertEquals(1000L, feeStatsResponse.getMaxFee().getP50().longValue());
    assertEquals(1000L, feeStatsResponse.getMaxFee().getP60().longValue());
    assertEquals(1000L, feeStatsResponse.getMaxFee().getP70().longValue());
    assertEquals(1000L, feeStatsResponse.getMaxFee().getP80().longValue());
    assertEquals(1000L, feeStatsResponse.getMaxFee().getP90().longValue());
    assertEquals(3000L, feeStatsResponse.getMaxFee().getP95().longValue());
    assertEquals(15000L, feeStatsResponse.getMaxFee().getP99().longValue());
  }
}
