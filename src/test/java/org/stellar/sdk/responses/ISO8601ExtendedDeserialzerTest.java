package org.stellar.sdk.responses;

import junit.framework.TestCase;
import org.junit.Test;

public class ISO8601ExtendedDeserialzerTest extends TestCase {

    @Test
    public void testDeSerializesEpochs() {
        ISO8601ExtendedDeserialzer deser = new ISO8601ExtendedDeserialzer();
        // verify a large negative epoch that falls on non-leap year, it triggers the 100 and 400 year counters
        assertEquals(deser.unixEpochSeconds("-7025-12-23T00:00:00Z",0), -283824000000L);
        // verify a large negative epoch that falls on leap year and it triggers the 100 and 400 year counters
        assertEquals(deser.unixEpochSeconds("-7024-12-22T00:00:00Z",0), -283792464000L);
        // verify a small negative epoch that falls on non-leap year, triggers no 100 or 400 year counts
        assertEquals( deser.unixEpochSeconds("1969-12-31T23:59:50Z",0), -10L);
        // verify a small negative epoch that falls on leap year, triggers no 100 or 400 year counter
        assertEquals(deser.unixEpochSeconds("1968-12-31T23:59:50Z", 0),-31536010L);

        // verify a large epoch that falls on non-leap year, it triggers the 100 and 400 year counters
        assertEquals(deser.unixEpochSeconds("2869-05-27T00:00:00Z",0), 28382400000L);
        // verify a large epoch that falls on leap year, it triggers the 100 and 400 year counters
        assertEquals(deser.unixEpochSeconds("+39121901036-03-29T15:30:22Z",0), 1234567890982222222L);
        // verify a small epoch that falls on non-leap year and no 100 or 400 year counters
        assertEquals(deser.unixEpochSeconds("+2021-11-21T07:24:10Z",0), 1637479450L);
        // verify a small epoch that falls on leap year and no 100 or 400 year counters
        assertEquals(deser.unixEpochSeconds("+2024-06-21T23:59:50Z",0), 1719014390L);
    }
}
