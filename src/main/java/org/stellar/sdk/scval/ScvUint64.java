package org.stellar.sdk.scval;

import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_U64}. */
@Value
@EqualsAndHashCode(callSuper = false)
class ScvUint64 extends Scv {
  private static final SCValType TYPE = SCValType.SCV_U64;

  public static final BigInteger MAX_VALUE = XdrUnsignedHyperInteger.MAX_VALUE;
  public static final BigInteger MIN_VALUE = XdrUnsignedHyperInteger.MIN_VALUE;

  BigInteger value;

  public ScvUint64(BigInteger value) {
    if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
      throw new IllegalArgumentException(
          String.format(
              "invalid value, expected between %s and %s, but got %s",
              MIN_VALUE, MAX_VALUE, value));
    }
    this.value = value;
  }

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder()
        .discriminant(TYPE)
        .u64(new Uint64(new XdrUnsignedHyperInteger(value)))
        .build();
  }

  public static ScvUint64 fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
    return new ScvUint64(scVal.getU64().getUint64().getNumber());
  }
}
