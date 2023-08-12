package org.stellar.sdk.scval;

import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.TimePoint;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_TIMEPOINT}. */
class ScvTimePoint {
  private static final SCValType TYPE = SCValType.SCV_TIMEPOINT;

  private static final BigInteger MAX_VALUE = XdrUnsignedHyperInteger.MAX_VALUE;
  private static final BigInteger MIN_VALUE = XdrUnsignedHyperInteger.MIN_VALUE;

  static SCVal toSCVal(BigInteger value) {
    if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
      throw new IllegalArgumentException(
          String.format(
              "invalid value, expected between %s and %s, but got %s",
              MIN_VALUE, MAX_VALUE, value));
    }

    return new SCVal.Builder()
        .discriminant(TYPE)
        .timepoint(new TimePoint(new Uint64(new XdrUnsignedHyperInteger(value))))
        .build();
  }

  static BigInteger fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
    return scVal.getTimepoint().getTimePoint().getUint64().getNumber();
  }
}
