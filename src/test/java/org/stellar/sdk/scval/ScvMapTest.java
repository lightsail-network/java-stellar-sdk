package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import org.junit.Test;
import org.stellar.sdk.xdr.SCMap;
import org.stellar.sdk.xdr.SCMapEntry;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvMapTest {
  @Test
  public void testScvMap() {
    LinkedHashMap<SCVal, SCVal> value = new LinkedHashMap<>();
    value.put(Scv.toSymbol("key1"), Scv.toString("value1"));
    value.put(Scv.toString("key2"), Scv.toInt32(123));

    SCVal expectedScVal =
        SCVal.builder()
            .discriminant(SCValType.SCV_MAP)
            .map(
                new SCMap(
                    new SCMapEntry[] {
                      SCMapEntry.builder().key(Scv.toString("key2")).val(Scv.toInt32(123)).build(),
                      SCMapEntry.builder()
                          .key(Scv.toSymbol("key1"))
                          .val(Scv.toString("value1"))
                          .build(),
                    }))
            .build();

    SCVal actualScVal = Scv.toMap(value);
    assertEquals(expectedScVal, actualScVal);
  }

  @Test
  public void testToMapSortsKeys() {
    LinkedHashMap<SCVal, SCVal> value = new LinkedHashMap<>();
    value.put(Scv.toUint32(3), Scv.toVoid());
    value.put(Scv.toUint32(1), Scv.toVoid());
    value.put(Scv.toUint32(2), Scv.toVoid());

    SCVal scVal = Scv.toMap(value);
    assertNotNull(scVal.getMap());
    SCMapEntry[] entries = scVal.getMap().getSCMap();
    assertEquals(Scv.toUint32(1), entries[0].getKey());
    assertEquals(Scv.toUint32(2), entries[1].getKey());
    assertEquals(Scv.toUint32(3), entries[2].getKey());
  }

  @Test
  public void testToMapStrictlyIncreasing() {
    LinkedHashMap<SCVal, SCVal> value = new LinkedHashMap<>();
    value.put(Scv.toSymbol("z"), Scv.toVoid());
    value.put(Scv.toSymbol("a"), Scv.toVoid());
    value.put(Scv.toUint32(100), Scv.toVoid());
    value.put(Scv.toBoolean(false), Scv.toVoid());
    value.put(Scv.toInt32(-1), Scv.toVoid());

    SCVal scVal = Scv.toMap(value);
    assertNotNull(scVal.getMap());
    SCMapEntry[] entries = scVal.getMap().getSCMap();
    for (int i = 0; i < entries.length - 1; i++) {
      assertTrue(
          "keys[" + i + "] not strictly less than keys[" + (i + 1) + "]",
          ScValComparator.compareScVal(entries[i].getKey(), entries[i + 1].getKey()) < 0);
    }
  }

  @Test
  public void testToMapMixedTypes() {
    LinkedHashMap<SCVal, SCVal> value = new LinkedHashMap<>();
    value.put(Scv.toSymbol("x"), Scv.toInt32(1));
    value.put(Scv.toUint32(42), Scv.toInt32(2));
    value.put(Scv.toBoolean(true), Scv.toInt32(3));

    SCVal scVal = Scv.toMap(value);
    assertNotNull(scVal.getMap());
    SCMapEntry[] entries = scVal.getMap().getSCMap();
    // SCV_BOOL(0) < SCV_U32(3) < SCV_SYMBOL(15)
    assertEquals(Scv.toBoolean(true), entries[0].getKey());
    assertEquals(Scv.toUint32(42), entries[1].getKey());
    assertEquals(Scv.toSymbol("x"), entries[2].getKey());
  }

  @Test
  public void testToMapSignedNegativeBoundaries() {
    LinkedHashMap<SCVal, SCVal> value = new LinkedHashMap<>();
    value.put(Scv.toInt32(0), Scv.toVoid());
    value.put(Scv.toInt32(Integer.MIN_VALUE), Scv.toVoid());
    value.put(Scv.toInt32(Integer.MAX_VALUE), Scv.toVoid());
    value.put(Scv.toInt32(-1), Scv.toVoid());

    SCVal scVal = Scv.toMap(value);
    assertNotNull(scVal.getMap());
    SCMapEntry[] entries = scVal.getMap().getSCMap();
    assertEquals(Scv.toInt32(Integer.MIN_VALUE), entries[0].getKey());
    assertEquals(Scv.toInt32(-1), entries[1].getKey());
    assertEquals(Scv.toInt32(0), entries[2].getKey());
    assertEquals(Scv.toInt32(Integer.MAX_VALUE), entries[3].getKey());
  }

  @Test
  public void testToMapAlreadySortedIdempotent() {
    LinkedHashMap<SCVal, SCVal> value = new LinkedHashMap<>();
    value.put(Scv.toUint32(1), Scv.toInt32(10));
    value.put(Scv.toUint32(2), Scv.toInt32(20));
    value.put(Scv.toUint32(3), Scv.toInt32(30));

    SCVal scVal = Scv.toMap(value);
    assertNotNull(scVal.getMap());
    SCMapEntry[] entries = scVal.getMap().getSCMap();
    assertEquals(Scv.toUint32(1), entries[0].getKey());
    assertEquals(Scv.toUint32(2), entries[1].getKey());
    assertEquals(Scv.toUint32(3), entries[2].getKey());
  }
}
