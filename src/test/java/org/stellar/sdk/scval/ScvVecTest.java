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
    value.add(new ScvInt32(123).toSCVal());
    value.add(new ScvString("value1").toSCVal());

    ScvVec scvVec = new ScvVec(value);
    SCVal scVal = scvVec.toSCVal();

    assertEquals(scvVec.getValue(), value);
    assertEquals(ScvVec.fromSCVal(scVal), scvVec);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_VEC)
            .vec(
                new org.stellar.sdk.xdr.SCVec(
                    new SCVal[] {new ScvInt32(123).toSCVal(), new ScvString("value1").toSCVal()}))
            .build();
    assertEquals(expectedScVal, scVal);

    assertEquals(Scv.toVec(value), scVal);
    assertEquals(Scv.fromVec(scVal), value);
  }
}
