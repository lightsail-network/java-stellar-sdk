package org.stellar.sdk.scval;

import org.stellar.sdk.xdr.SCBytes;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_BYTES}. */
class ScvBytes {
  private static final SCValType TYPE = SCValType.SCV_BYTES;

  static SCVal toSCVal(byte[] value) {
    return SCVal.builder().discriminant(TYPE).bytes(new SCBytes(value)).build();
  }

  static byte[] fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return scVal.getBytes().getSCBytes();
  }
}
