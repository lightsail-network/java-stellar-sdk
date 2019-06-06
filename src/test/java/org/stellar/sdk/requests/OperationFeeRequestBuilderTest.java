package org.stellar.sdk.requests;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.Test;
import org.stellar.sdk.Server;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperationFeeRequestBuilderTest {
    @Test
    public void testBuilder() {
        Server server = new Server("https://horizon-testnet.stellar.org");
        HttpUrl uri = server.operationFeeStats().buildUri();
        assertEquals("https://horizon-testnet.stellar.org/operation_fee_stats", uri.toString());
    }
}
