package org.stellar.sdk;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NetworkTest {
    @AfterEach
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
