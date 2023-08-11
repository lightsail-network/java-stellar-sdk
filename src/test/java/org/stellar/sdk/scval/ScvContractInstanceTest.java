package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.ContractExecutable;
import org.stellar.sdk.xdr.ContractExecutableType;
import org.stellar.sdk.xdr.SCContractInstance;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvContractInstanceTest {
  @Test
  public void testScvContractInstance() {
    SCContractInstance value =
        new SCContractInstance.Builder()
            .executable(
                new ContractExecutable.Builder()
                    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_TOKEN)
                    .build())
            .build();

    ScvContractInstance scvContractInstance = new ScvContractInstance(value);
    SCVal scVal = scvContractInstance.toSCVal();

    assertEquals(scvContractInstance.getValue(), value);
    assertEquals(ScvContractInstance.fromSCVal(scVal), scvContractInstance);

    SCVal expectedScVal =
        new SCVal.Builder().discriminant(SCValType.SCV_CONTRACT_INSTANCE).instance(value).build();
    assertEquals(expectedScVal, scVal);

    assertEquals(Scv.toContractInstance(value), scVal);
    assertEquals(Scv.fromContractInstance(scVal), value);
  }
}
