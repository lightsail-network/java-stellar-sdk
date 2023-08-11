package org.stellar.sdk.scval;

import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.xdr.Duration;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_DURATION}. */
@Value
@EqualsAndHashCode(callSuper = false)
class ScvDuration extends Scv {
  private static final SCValType TYPE = SCValType.SCV_DURATION;
  public static final BigInteger MAX_VALUE = XdrUnsignedHyperInteger.MAX_VALUE;
  public static final BigInteger MIN_VALUE = XdrUnsignedHyperInteger.MIN_VALUE;

  BigInteger value;

  public ScvDuration(BigInteger value) {
    if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
      throw new IllegalArgumentException("Invalid Duration value, must be between 0 and 2^64-1");
    }
    this.value = value;
  }

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder()
        .discriminant(TYPE)
        .duration(new Duration(new Uint64(new XdrUnsignedHyperInteger(value))))
        .build();
  }

  public static ScvDuration fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return new ScvDuration(scVal.getDuration().getDuration().getUint64().getNumber());
  }
}
