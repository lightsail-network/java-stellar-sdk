package org.stellar.sdk.xdr;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.stellar.sdk.Network;
import org.stellar.sdk.operations.InflationOperation;
import org.stellar.sdk.operations.Operation;

public class InflationDecodeTest {

  @Test
  public void testDecodeInflationOperation() throws Exception {
    org.stellar.sdk.Transaction tx =
        (org.stellar.sdk.Transaction)
            org.stellar.sdk.Transaction.fromEnvelopeXdr(
                "AAAAAALC+FwxReetNDfMNvY5LOS1qSe7QqrfQPS28dnIV95NAAAAZAAAAAAAAATSAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAJAAAAAAAAAAA=",
                Network.TESTNET);
    Operation[] ops = tx.getOperations();
    assertTrue(ops[0] instanceof InflationOperation);
  }
}
