package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.LinkedHashMap;
import org.junit.Test;
import org.stellar.sdk.xdr.SCMap;
import org.stellar.sdk.xdr.SCMapEntry;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvMapTest {
  @Test
  public void testScvMap() throws IOException {
    LinkedHashMap<SCVal, SCVal> value = new LinkedHashMap<>();
    value.put(new ScvSymbol("key1").toSCVal(), new ScvString("value1").toSCVal());
    value.put(new ScvString("key2").toSCVal(), new ScvInt32(123).toSCVal());

    ScvMap scvMap = new ScvMap(value);
    SCVal scVal = scvMap.toSCVal();

    assertEquals(scvMap.getValue(), value);
    assertEquals(ScvMap.fromSCVal(scVal), scvMap);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_MAP)
            .map(
                new SCMap(
                    new SCMapEntry[] {
                      new SCMapEntry.Builder()
                          .key(new ScvSymbol("key1").toSCVal())
                          .val(new ScvString("value1").toSCVal())
                          .build(),
                      new SCMapEntry.Builder()
                          .key(new ScvString("key2").toSCVal())
                          .val(new ScvInt32(123).toSCVal())
                          .build(),
                    }))
            .build();
    assertEquals(expectedScVal.toXdrBase64(), scVal.toXdrBase64());

    assertEquals(Scv.toMap(value), scVal);
    assertEquals(Scv.fromMap(scVal), value);
  }
}
