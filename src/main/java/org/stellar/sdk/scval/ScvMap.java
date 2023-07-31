package org.stellar.sdk.scval;

import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.stellar.sdk.xdr.SCMap;
import org.stellar.sdk.xdr.SCMapEntry;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

@Value
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScvMap extends Scv {
  private static final SCValType TYPE = SCValType.SCV_MAP;

  Map<Scv, Scv> value;

  @Override
  public SCVal toSCVal() {
    SCMapEntry[] scMapEntries = new SCMapEntry[value.size()];
    int i = 0;
    for (Map.Entry<Scv, Scv> entry : value.entrySet()) {
      scMapEntries[i++] =
          new SCMapEntry.Builder()
              .key(entry.getKey().toSCVal())
              .val(entry.getValue().toSCVal())
              .build();
    }
    return new SCVal.Builder().discriminant(TYPE).map(new SCMap(scMapEntries)).build();
  }

  @Override
  public SCValType getSCValType() {
    return TYPE;
  }

  public static ScvMap fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    Map<Scv, Scv> map = new HashMap<>();
    for (SCMapEntry entry : scVal.getMap().getSCMap()) {
      map.put(Scv.fromSCVal(entry.getKey()), Scv.fromSCVal(entry.getVal()));
    }
    return new ScvMap(map);
  }
}
