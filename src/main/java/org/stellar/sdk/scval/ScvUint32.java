package org.stellar.sdk.scval;

import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_U32}. */
class ScvUint32 {
  private static final SCValType TYPE = SCValType.SCV_U32;

  private static final long MAX_VALUE = XdrUnsignedInteger.MAX_VALUE;
  private static final long MIN_VALUE = XdrUnsignedInteger.MIN_VALUE;

  static SCVal toSCVal(long value) {
    if (value < MIN_VALUE || value > MAX_VALUE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid value, expected between %s and %s, but got %s",
              MIN_VALUE, MAX_VALUE, value));
    }

    return new SCVal.Builder()
        .discriminant(TYPE)
        .u32(new Uint32(new XdrUnsignedInteger(value)))
        .build();
  }

  static Long fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return scVal.getU32().getUint32().getNumber();
  }
}
