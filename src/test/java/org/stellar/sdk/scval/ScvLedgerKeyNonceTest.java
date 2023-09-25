package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.SCNonceKey;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvLedgerKeyNonceTest {
  @Test
  public void testScvLedgerKeyNonce() {
    long value = 123456L;

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_LEDGER_KEY_NONCE)
            .nonce_key(new SCNonceKey.Builder().nonce(new Int64(value)).build())
            .build();

    SCVal actualScVal = Scv.toLedgerKeyNonce(value);
    assertEquals(expectedScVal, actualScVal);
  }
}
