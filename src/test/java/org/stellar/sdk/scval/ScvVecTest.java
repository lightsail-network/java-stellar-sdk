package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvVecTest {
  @Test
  public void testScvVec() {
    List<SCVal> value = new ArrayList<>();
    value.add(Scv.toInt32(123));
    value.add(Scv.toString("value1"));

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_VEC)
            .vec(
                new org.stellar.sdk.xdr.SCVec(
                    new SCVal[] {Scv.toInt32(123), Scv.toString("value1")}))
            .build();

    SCVal actualScVal = Scv.toVec(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromVec(actualScVal));
  }
}
