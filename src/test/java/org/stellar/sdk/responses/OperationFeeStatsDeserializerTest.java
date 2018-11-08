package org.stellar.sdk.responses;

import junit.framework.TestCase;
import org.junit.Test;

public class OperationFeeStatsDeserializerTest extends TestCase {
    @Test
    public void testDeserialize() {
        String json = "{\n" +
                "  \"last_ledger\": \"20882791\",\n" +
                "  \"last_ledger_base_fee\": \"100\",\n" +
                "  \"min_accepted_fee\": \"101\",\n" +
                "  \"mode_accepted_fee\": \"102\"\n" +
                "}";

        OperationFeeStatsResponse stats = GsonSingleton.getInstance().fromJson(json, OperationFeeStatsResponse.class);
        assertEquals(new Long(20882791), stats.getLastLedger());
        assertEquals(new Long(100), stats.getLastLedgerBaseFee());
        assertEquals(new Long(101), stats.getMin());
        assertEquals(new Long(102), stats.getMode());
    }
}
