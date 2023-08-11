package org.stellar.sdk.scval;

import java.util.Arrays;
import java.util.Collection;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.SCVec;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_VEC}. */
@Value
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
class ScvVec extends Scv {
  private static final SCValType TYPE = SCValType.SCV_VEC;

  @NonNull Collection<SCVal> value;

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder()
        .discriminant(TYPE)
        .vec(new SCVec(value.toArray(new SCVal[0])))
        .build();
  }

  public static ScvVec fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return new ScvVec(Arrays.asList(scVal.getVec().getSCVec()));
  }
}
