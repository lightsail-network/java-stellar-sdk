package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvLedgerKeyContractInstanceTest {
  @Test
  public void testScvLedgerKeyContractInstance() {
    ScvLedgerKeyContractInstance scvLedgerKeyContractInstance = new ScvLedgerKeyContractInstance();
    SCVal scVal = scvLedgerKeyContractInstance.toSCVal();

    assertEquals(ScvLedgerKeyContractInstance.fromSCVal(scVal), scvLedgerKeyContractInstance);

    SCVal expectedScVal =
        new SCVal.Builder().discriminant(SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE).build();
    assertEquals(expectedScVal, scVal);

    assertEquals(Scv.toLedgerKeyContractInstance(), scVal);
    Scv.fromLedgerKeyContractInstance(scVal);
  }
}
