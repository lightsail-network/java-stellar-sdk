package org.stellar.sdk.scval;

import org.stellar.sdk.xdr.SCError;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_ERROR}. */
class ScvError {
  private static final SCValType TYPE = SCValType.SCV_ERROR;

  static SCVal toSCVal(SCError value) {
    return new SCVal.Builder()
        .discriminant(TYPE)
        .error(new SCError.Builder().code(value.getCode()).type(value.getType()).build())
        .build();
  }

  static SCError fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return scVal.getError();
  }
}
