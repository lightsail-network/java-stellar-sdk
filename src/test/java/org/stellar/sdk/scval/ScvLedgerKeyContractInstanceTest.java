package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.ContractExecutable;
import org.stellar.sdk.xdr.ContractExecutableType;
import org.stellar.sdk.xdr.SCContractInstance;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvLedgerKeyContractInstanceTest {
  @Test
  public void testScvLedgerKeyContractInstance() {
    SCContractInstance value =
        new SCContractInstance.Builder()
            .executable(
                new ContractExecutable.Builder()
                    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_TOKEN)
                    .build())
            .build();

    ScvLedgerKeyContractInstance scvLedgerKeyContractInstance =
        new ScvLedgerKeyContractInstance(value);
    SCVal scVal = scvLedgerKeyContractInstance.toSCVal();

    assertEquals(
        scvLedgerKeyContractInstance.getSCValType(), SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE);
    assertEquals(scvLedgerKeyContractInstance.getValue(), value);

    assertEquals(ScvLedgerKeyContractInstance.fromSCVal(scVal), scvLedgerKeyContractInstance);
    assertEquals(Scv.fromSCVal(scVal), scvLedgerKeyContractInstance);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE)
            .instance(value)
            .build();
    assertEquals(expectedScVal, scVal);
  }
}
