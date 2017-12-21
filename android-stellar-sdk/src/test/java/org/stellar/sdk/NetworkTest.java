package org.stellar.sdk;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NetworkTest {
    @After
    public void resetNetwork() {
        Network.use(null);
    }

    @Test
    public void testNoDefaultNetwork() {
        assertNull(Network.current());
    }

    @Test
    public void testSwitchToTestNetwork() {
        Network.useTestNetwork();
        assertEquals("Test SDF Network ; September 2015", Network.current().getNetworkPassphrase());
    }

    @Test
    public void testSwitchToPublicNetwork() {
        Network.usePublicNetwork();
        assertEquals("Public Global Stellar Network ; September 2015", Network.current().getNetworkPassphrase());
    }
}
