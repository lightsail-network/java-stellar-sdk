package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvContractInstanceTest {
  @Test
  public void testScvContractInstance() {
    ScvContractInstance scvContractInstance = new ScvContractInstance();
    SCVal scVal = scvContractInstance.toSCVal();

    assertEquals(scvContractInstance.getSCValType(), SCValType.SCV_CONTRACT_INSTANCE);

    assertEquals(ScvContractInstance.fromSCVal(scVal), scvContractInstance);
    assertEquals(Scv.fromSCVal(scVal), scvContractInstance);

    SCVal expectedScVal = new SCVal.Builder().discriminant(SCValType.SCV_CONTRACT_INSTANCE).build();
    assertEquals(expectedScVal, scVal);
  }
}
