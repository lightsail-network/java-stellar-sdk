package org.stellar.sdk.scval;

import java.util.LinkedHashMap;
import java.util.Map;
import org.stellar.sdk.xdr.SCMap;
import org.stellar.sdk.xdr.SCMapEntry;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_MAP}. */
class ScvMap {
  private static final SCValType TYPE = SCValType.SCV_MAP;

  // we want to keep the order of the map entries
  // this ensures that the generated XDR is deterministic.
  static SCVal toSCVal(LinkedHashMap<SCVal, SCVal> value) {
    SCMapEntry[] scMapEntries = new SCMapEntry[value.size()];
    int i = 0;
    for (Map.Entry<SCVal, SCVal> entry : value.entrySet()) {
      scMapEntries[i++] =
          new SCMapEntry.Builder().key(entry.getKey()).val(entry.getValue()).build();
    }
    return new SCVal.Builder().discriminant(TYPE).map(new SCMap(scMapEntries)).build();
  }

  static LinkedHashMap<SCVal, SCVal> fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    LinkedHashMap<SCVal, SCVal> map = new LinkedHashMap<>();
    for (SCMapEntry entry : scVal.getMap().getSCMap()) {
      map.put(entry.getKey(), entry.getVal());
    }
    return map;
  }
}
