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
        SCContractInstance.builder()
            .executable(
                ContractExecutable.builder()
                    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_STELLAR_ASSET)
                    .build())
            .build();

    SCVal expectedScVal =
        SCVal.builder().discriminant(SCValType.SCV_CONTRACT_INSTANCE).instance(value).build();

    SCVal actualScVal = Scv.toContractInstance(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromContractInstance(actualScVal));
  }
}
