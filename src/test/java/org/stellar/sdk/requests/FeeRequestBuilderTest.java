package org.stellar.sdk.requests;

import okhttp3.HttpUrl;
import org.junit.Test;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;

import static org.junit.Assert.assertEquals;

public class FeeRequestBuilderTest {
    @Test
    public void testBuilder() {
        Server server = new Server("https://horizon-testnet.stellar.org");
        HttpUrl uri = server.feeStats().buildUri();
        assertEquals("https://horizon-testnet.stellar.org/fee_stats", uri.toString());
    }
}
