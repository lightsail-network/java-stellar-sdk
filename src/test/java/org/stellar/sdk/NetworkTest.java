package org.stellar.sdk;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NetworkTest {

    public void tearDown() {
        Network.useTestNetwork();
    }

    @Test
    public void testDefaultTestNetwork() {
        assertEquals("Test SDF Network ; September 2015", Network.current().getNetworkPassphrase());
    }

    @Test
    public void testSwitchToPublicNetwork() {
        Network.usePublicNetwork();
        assertEquals("Public Global Stellar Network ; September 2015", Network.current().getNetworkPassphrase());
        Network.useTestNetwork();
    }
}
