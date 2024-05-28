package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

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
                      SCMapEntry.builder()
                          .key(Scv.toSymbol("key1"))
                          .val(Scv.toString("value1"))
                          .build(),
                      SCMapEntry.builder().key(Scv.toString("key2")).val(Scv.toInt32(123)).build(),
                    }))
            .build();

    SCVal actualScVal = Scv.toMap(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromMap(actualScVal));
  }
}
