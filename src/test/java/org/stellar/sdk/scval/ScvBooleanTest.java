package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvBooleanTest {
  @Test
  public void testScvBoolean() {
    ScvBoolean scvBoolean = new ScvBoolean(true);
    SCVal scVal = scvBoolean.toSCVal();

    assertEquals(scvBoolean.getSCValType(), SCValType.SCV_BOOL);
    assertEquals(scvBoolean.getValue(), true);

    assertEquals(ScvBoolean.fromSCVal(scVal), scvBoolean);
    assertEquals(Scv.fromSCVal(scVal), scvBoolean);

    SCVal expectedScVal = new SCVal.Builder().discriminant(SCValType.SCV_BOOL).b(true).build();
    assertEquals(expectedScVal, scVal);
  }
}
