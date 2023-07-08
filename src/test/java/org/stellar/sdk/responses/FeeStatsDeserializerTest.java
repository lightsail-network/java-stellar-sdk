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
    assertEquals(new Long(22606298), stats.getLastLedger());
    assertEquals(new Long(100), stats.getLastLedgerBaseFee());
    assertEquals(new Float(0.97), stats.getLedgerCapacityUsage());

    FeeDistribution maxFee = stats.getMaxFee();
    assertEquals(new Long(130), maxFee.getMin());
    assertEquals(new Long(8000), maxFee.getMax());
    assertEquals(new Long(250), maxFee.getMode());
    assertEquals(new Long(150), maxFee.getP10());
    assertEquals(new Long(200), maxFee.getP20());
    assertEquals(new Long(300), maxFee.getP30());
    assertEquals(new Long(400), maxFee.getP40());
    assertEquals(new Long(500), maxFee.getP50());
    assertEquals(new Long(1000), maxFee.getP60());
    assertEquals(new Long(2000), maxFee.getP70());
    assertEquals(new Long(3000), maxFee.getP80());
    assertEquals(new Long(4000), maxFee.getP90());
    assertEquals(new Long(5000), maxFee.getP95());
    assertEquals(new Long(8000), maxFee.getP99());

    FeeDistribution feeCharged = stats.getFeeCharged();
    assertEquals(new Long(100), feeCharged.getMin());
    assertEquals(new Long(102), feeCharged.getMax());
    assertEquals(new Long(101), feeCharged.getMode());
    assertEquals(new Long(103), feeCharged.getP10());
    assertEquals(new Long(104), feeCharged.getP20());
    assertEquals(new Long(105), feeCharged.getP30());
    assertEquals(new Long(106), feeCharged.getP40());
    assertEquals(new Long(107), feeCharged.getP50());
    assertEquals(new Long(108), feeCharged.getP60());
    assertEquals(new Long(109), feeCharged.getP70());
    assertEquals(new Long(110), feeCharged.getP80());
    assertEquals(new Long(111), feeCharged.getP90());
    assertEquals(new Long(112), feeCharged.getP95());
    assertEquals(new Long(113), feeCharged.getP99());
  }
}
