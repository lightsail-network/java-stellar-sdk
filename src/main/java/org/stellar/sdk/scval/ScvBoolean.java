package org.stellar.sdk.scval;

import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_BOOL}. */
class ScvBoolean {
  private static final SCValType TYPE = SCValType.SCV_BOOL;

  static SCVal toSCVal(Boolean value) {
    return new SCVal.Builder().discriminant(TYPE).b(value).build();
  }

  static boolean fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return scVal.getB();
  }
}
