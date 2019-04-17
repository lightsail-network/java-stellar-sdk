package org.stellar.sdk.xdr;

import org.junit.Test;
import org.stellar.sdk.InflationOperation;

import static org.junit.Assert.assertTrue;

public class InflationDecodeTest {

    @Test
    public void testDecodeInflationOperation() throws Exception {
        org.stellar.sdk.Transaction tx = org.stellar.sdk.Transaction.fromEnvelopeXdr("AAAAAALC+FwxReetNDfMNvY5LOS1qSe7QqrfQPS28dnIV95NAAAAZAAAAAAAAATSAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAJAAAAAAAAAAA=");
        org.stellar.sdk.Operation[] ops = tx.getOperations();
        assertTrue(ops[0] instanceof InflationOperation);
    }
}
