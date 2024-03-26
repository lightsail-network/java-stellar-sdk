package org.stellar.sdk.responses;

import junit.framework.TestCase;
import org.junit.Test;

public class FeeStatsDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    String json =
        "{\n"
            + "  \"last_ledger\": \"22606298\",\n"
            + "  \"last_ledger_base_fee\": \"100\",\n"
            + "  \"ledger_capacity_usage\": \"0.97\",\n"
            + "  \"max_fee\": {\n"
            + "    \"min\": \"130\",\n"
            + "    \"max\": \"8000\",\n"
            + "    \"mode\": \"250\",\n"
            + "    \"p10\": \"150\",\n"
            + "    \"p20\": \"200\",\n"
            + "    \"p30\": \"300\",\n"
            + "    \"p40\": \"400\",\n"
            + "    \"p50\": \"500\",\n"
            + "    \"p60\": \"1000\",\n"
            + "    \"p70\": \"2000\",\n"
            + "    \"p80\": \"3000\",\n"
            + "    \"p90\": \"4000\",\n"
            + "    \"p95\": \"5000\",\n"
            + "    \"p99\": \"8000\"\n"
            + "  },\n"
            + "  \"fee_charged\": {\n"
            + "    \"min\": \"100\",\n"
            + "    \"max\": \"102\",\n"
            + "    \"mode\": \"101\",\n"
            + "    \"p10\": \"103\",\n"
            + "    \"p20\": \"104\",\n"
            + "    \"p30\": \"105\",\n"
            + "    \"p40\": \"106\",\n"
            + "    \"p50\": \"107\",\n"
            + "    \"p60\": \"108\",\n"
            + "    \"p70\": \"109\",\n"
            + "    \"p80\": \"110\",\n"
            + "    \"p90\": \"111\",\n"
            + "    \"p95\": \"112\",\n"
            + "    \"p99\": \"113\"\n"
            + "  }\n"
            + "}";

    FeeStatsResponse stats = GsonSingleton.getInstance().fromJson(json, FeeStatsResponse.class);
    assertEquals(22606298, stats.getLastLedger().longValue());
    assertEquals(100, stats.getLastLedgerBaseFee().longValue());
    assertEquals(0.97F, stats.getLedgerCapacityUsage());

    FeeDistribution maxFee = stats.getMaxFee();
    assertEquals(130, maxFee.getMin().longValue());
    assertEquals(8000, maxFee.getMax().longValue());
    assertEquals(250, maxFee.getMode().longValue());
    assertEquals(150, maxFee.getP10().longValue());
    assertEquals(200, maxFee.getP20().longValue());
    assertEquals(300, maxFee.getP30().longValue());
    assertEquals(400, maxFee.getP40().longValue());
    assertEquals(500, maxFee.getP50().longValue());
    assertEquals(1000, maxFee.getP60().longValue());
    assertEquals(2000, maxFee.getP70().longValue());
    assertEquals(3000, maxFee.getP80().longValue());
    assertEquals(4000, maxFee.getP90().longValue());
    assertEquals(5000, maxFee.getP95().longValue());
    assertEquals(8000, maxFee.getP99().longValue());

    FeeDistribution feeCharged = stats.getFeeCharged();
    assertEquals(100, feeCharged.getMin().longValue());
    assertEquals(102, feeCharged.getMax().longValue());
    assertEquals(101, feeCharged.getMode().longValue());
    assertEquals(103, feeCharged.getP10().longValue());
    assertEquals(104, feeCharged.getP20().longValue());
    assertEquals(105, feeCharged.getP30().longValue());
    assertEquals(106, feeCharged.getP40().longValue());
    assertEquals(107, feeCharged.getP50().longValue());
    assertEquals(108, feeCharged.getP60().longValue());
    assertEquals(109, feeCharged.getP70().longValue());
    assertEquals(110, feeCharged.getP80().longValue());
    assertEquals(111, feeCharged.getP90().longValue());
    assertEquals(112, feeCharged.getP95().longValue());
    assertEquals(113, feeCharged.getP99().longValue());
  }
}
