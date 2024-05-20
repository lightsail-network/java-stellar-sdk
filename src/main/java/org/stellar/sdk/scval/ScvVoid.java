package org.stellar.sdk.scval;

import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_VOID}. */
class ScvVoid {
  private static final SCValType TYPE = SCValType.SCV_VOID;

  static SCVal toSCVal() {
    return SCVal.builder().discriminant(TYPE).build();
  }

  static void fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
  }
}
