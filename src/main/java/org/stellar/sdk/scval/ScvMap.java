package org.stellar.sdk.scval;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.stellar.sdk.xdr.SCMap;
import org.stellar.sdk.xdr.SCMapEntry;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_MAP}. */
class ScvMap {
  private static final SCValType TYPE = SCValType.SCV_MAP;

  // Entries are sorted by key following Soroban runtime ordering rules,
  // as the network requires ScMap keys to be in ascending order.
  static SCVal toSCVal(Map<SCVal, SCVal> value) {
    SCMapEntry[] scMapEntries = new SCMapEntry[value.size()];
    int i = 0;
    for (Map.Entry<SCVal, SCVal> entry : value.entrySet()) {
      scMapEntries[i++] = SCMapEntry.builder().key(entry.getKey()).val(entry.getValue()).build();
    }
    Arrays.sort(scMapEntries, (a, b) -> ScValComparator.compareScVal(a.getKey(), b.getKey()));
    return SCVal.builder().discriminant(TYPE).map(new SCMap(scMapEntries)).build();
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
