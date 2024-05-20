package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvBooleanTest {
  @Test
  public void testScvBoolean() {
    boolean value = true;
    SCVal expectedScVal = SCVal.builder().discriminant(SCValType.SCV_BOOL).b(value).build();
    SCVal actualScVal = Scv.toBoolean(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromBoolean(actualScVal));
  }
}
