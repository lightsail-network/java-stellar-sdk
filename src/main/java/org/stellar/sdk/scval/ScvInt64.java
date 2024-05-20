package org.stellar.sdk.scval;

import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_I64}. */
class ScvInt64 {
  private static final SCValType TYPE = SCValType.SCV_I64;

  static SCVal toSCVal(long value) {
    return SCVal.builder().discriminant(TYPE).i64(new Int64(value)).build();
  }

  static long fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
    return scVal.getI64().getInt64();
  }
}
