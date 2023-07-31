package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvVecTest {
  @Test
  public void testScvVec() {
    List<Scv> value = new ArrayList<>();

    Map<Scv, Scv> map = new HashMap<>();
    map.put(new ScvSymbol("key1"), new ScvString("mapValue1"));
    map.put(new ScvString("key2"), new ScvInt32(23434));
    map.put(
        new ScvString("key3"),
        new ScvVec(
            Arrays.asList(
                new ScvInt32(1),
                new ScvInt32(2),
                new ScvVec(Arrays.asList(new ScvBoolean(true), new ScvBoolean(false))))));

    value.add(new ScvMap(map));
    value.add(new ScvInt32(123));
    value.add(new ScvString("value1"));

    ScvVec scvVec = new ScvVec(value);
    SCVal scVal = scvVec.toSCVal();

    assertEquals(scvVec.getSCValType(), SCValType.SCV_VEC);
    assertEquals(scvVec.getValue(), value);

    assertEquals(ScvVec.fromSCVal(scVal), scvVec);
    assertEquals(Scv.fromSCVal(scVal), scvVec);

    assertEquals(ScvVec.fromSCVal(scVal).getValue().get(0), new ScvMap(map));
    assertEquals(ScvVec.fromSCVal(scVal).getValue().get(1), new ScvInt32(123));
    assertEquals(ScvVec.fromSCVal(scVal).getValue().get(2), new ScvString("value1"));
  }
}
