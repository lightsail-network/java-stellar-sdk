package org.stellar.sdk.responses;

import junit.framework.TestCase;
import org.junit.Test;

public class FeeStatsDeserializerTest extends TestCase {
    @Test
    public void testDeserialize() {
        String json = "{\n" +
                "  \"last_ledger\": \"20882791\",\n" +
                "  \"last_ledger_base_fee\": \"100\",\n" +
                "  \"ledger_capacity_usage\": \"0.97\"," +
                "  \"min_accepted_fee\": \"101\",\n" +
                "  \"mode_accepted_fee\": \"102\",\n" +
                "  \"p10_accepted_fee\": \"103\",\n" +
                "  \"p20_accepted_fee\": \"104\",\n" +
                "  \"p30_accepted_fee\": \"105\",\n" +
                "  \"p40_accepted_fee\": \"106\",\n" +
                "  \"p50_accepted_fee\": \"107\",\n" +
                "  \"p60_accepted_fee\": \"108\",\n" +
                "  \"p70_accepted_fee\": \"109\",\n" +
                "  \"p80_accepted_fee\": \"110\",\n" +
                "  \"p90_accepted_fee\": \"111\",\n" +
                "  \"p95_accepted_fee\": \"112\",\n" +
                "  \"p99_accepted_fee\": \"113\"" +
                "}";

        FeeStatsResponse stats = GsonSingleton.getInstance().fromJson(json, FeeStatsResponse.class);
        assertEquals(new Long(20882791), stats.getLastLedger());
        assertEquals(new Long(100), stats.getLastLedgerBaseFee());
        assertEquals(new Float(0.97), stats.getLedgerCapacityUsage());
        assertEquals(new Long(101), stats.getMin());
        assertEquals(new Long(102), stats.getMode());
        assertEquals(new Long(103), stats.getP10());
        assertEquals(new Long(104), stats.getP20());
        assertEquals(new Long(105), stats.getP30());
        assertEquals(new Long(106), stats.getP40());
        assertEquals(new Long(107), stats.getP50());
        assertEquals(new Long(108), stats.getP60());
        assertEquals(new Long(109), stats.getP70());
        assertEquals(new Long(110), stats.getP80());
        assertEquals(new Long(111), stats.getP90());
        assertEquals(new Long(112), stats.getP95());
        assertEquals(new Long(113), stats.getP99());
    }
}
