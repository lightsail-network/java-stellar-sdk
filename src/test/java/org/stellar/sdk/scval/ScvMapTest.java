package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvMapTest {
  @Test
  public void testScvMap() {
    Map<Scv, Scv> value = new HashMap<>();
    value.put(new ScvSymbol("key1"), new ScvString("value1"));
    value.put(new ScvString("key2"), new ScvInt32(123));

    Map<Scv, Scv> map = new HashMap<>();
    map.put(new ScvSymbol("mapKey1"), new ScvString("mapValue1"));
    map.put(new ScvString("mapKey2"), new ScvInt32(23434));

    value.put(
        new ScvString("key3"),
        new ScvVec(
            Arrays.asList(
                new ScvInt32(1),
                new ScvInt32(2),
                new ScvMap(map),
                new ScvVec(Arrays.asList(new ScvBoolean(true), new ScvBoolean(false))))));

    ScvMap scvMap = new ScvMap(value);
    SCVal scVal = scvMap.toSCVal();

    assertEquals(scvMap.getSCValType(), SCValType.SCV_MAP);
    assertEquals(scvMap.getValue(), value);

    assertEquals(ScvMap.fromSCVal(scVal), scvMap);
    assertEquals(Scv.fromSCVal(scVal), scvMap);

    assertEquals(
        ScvMap.fromSCVal(scVal).getValue().get(new ScvSymbol("key1")), new ScvString("value1"));
    assertEquals(ScvMap.fromSCVal(scVal).getValue().get(new ScvString("key2")), new ScvInt32(123));
    assertEquals(
        ScvMap.fromSCVal(scVal).getValue().get(new ScvString("key3")),
        new ScvVec(
            Arrays.asList(
                new ScvInt32(1),
                new ScvInt32(2),
                new ScvMap(map),
                new ScvVec(Arrays.asList(new ScvBoolean(true), new ScvBoolean(false))))));
  }
}
