package org.stellar.sdk.scval;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.SCVec;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_VEC}. */
class ScvVec {
  private static final SCValType TYPE = SCValType.SCV_VEC;

  static SCVal toSCVal(Collection<SCVal> value) {
    return SCVal.builder().discriminant(TYPE).vec(new SCVec(value.toArray(new SCVal[0]))).build();
  }

  static List<SCVal> fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return Arrays.asList(scVal.getVec().getSCVec());
  }
}
