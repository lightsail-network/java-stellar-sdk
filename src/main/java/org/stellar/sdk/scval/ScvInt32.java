package org.stellar.sdk.scval;

import org.stellar.sdk.xdr.Int32;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_I32}. */
class ScvInt32 {
  private static final SCValType TYPE = SCValType.SCV_I32;

  static SCVal toSCVal(int value) {
    return SCVal.builder().discriminant(TYPE).i32(new Int32(value)).build();
  }

  static int fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return scVal.getI32().getInt32();
  }
}
