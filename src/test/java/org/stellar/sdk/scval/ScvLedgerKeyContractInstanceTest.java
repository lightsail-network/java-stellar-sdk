package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvLedgerKeyContractInstanceTest {
  @Test
  public void testScvLedgerKeyContractInstance() {
    SCVal expectedScVal =
        SCVal.builder().discriminant(SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE).build();

    SCVal actualScVal = Scv.toLedgerKeyContractInstance();
    assertEquals(expectedScVal, actualScVal);
    Scv.fromLedgerKeyContractInstance(actualScVal);
  }
}
