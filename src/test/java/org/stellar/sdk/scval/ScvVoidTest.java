package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvVoidTest {
  @Test
  public void testScvVoid() {
    SCVal expectedScVal = new SCVal.Builder().discriminant(SCValType.SCV_VOID).build();

    SCVal actualScVal = Scv.toVoid();
    assertEquals(expectedScVal, actualScVal);
    Scv.fromVoid(actualScVal);
  }
}
