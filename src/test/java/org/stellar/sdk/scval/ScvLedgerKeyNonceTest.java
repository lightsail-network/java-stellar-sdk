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
    ScvLedgerKeyNonce scvLedgerKeyNonce = new ScvLedgerKeyNonce(value);
    SCVal scVal = scvLedgerKeyNonce.toSCVal();

    assertEquals(scvLedgerKeyNonce.getValue(), value);
    assertEquals(ScvLedgerKeyNonce.fromSCVal(scVal), scvLedgerKeyNonce);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_LEDGER_KEY_NONCE)
            .nonce_key(new SCNonceKey.Builder().nonce(new Int64(value)).build())
            .build();
    assertEquals(expectedScVal, scVal);

    assertEquals(Scv.toLedgerKeyNonce(value), scVal);
    assertEquals(Scv.fromLedgerKeyNonce(scVal), value);
  }
}
