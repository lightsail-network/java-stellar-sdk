package org.stellar.sdk.xdr;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.InflationOperation;
import org.stellar.sdk.Network;

public class InflationDecodeTest {

  @Test
  public void testDecodeInflationOperation() throws Exception {
    org.stellar.sdk.Transaction tx =
        (org.stellar.sdk.Transaction)
            org.stellar.sdk.Transaction.fromEnvelopeXdr(
                AccountConverter.enableMuxed(),
                "AAAAAALC+FwxReetNDfMNvY5LOS1qSe7QqrfQPS28dnIV95NAAAAZAAAAAAAAATSAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAJAAAAAAAAAAA=",
                Network.TESTNET);
    org.stellar.sdk.Operation[] ops = tx.getOperations();
    assertTrue(ops[0] instanceof InflationOperation);
  }
}
