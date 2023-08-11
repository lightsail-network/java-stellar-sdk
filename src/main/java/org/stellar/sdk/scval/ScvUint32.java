package org.stellar.sdk.scval;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_U32}. */
@Value
@EqualsAndHashCode(callSuper = false)
class ScvUint32 extends Scv {
  private static final SCValType TYPE = SCValType.SCV_U32;

  public static final long MAX_VALUE = XdrUnsignedInteger.MAX_VALUE;
  public static final long MIN_VALUE = XdrUnsignedInteger.MIN_VALUE;

  long value;

  public ScvUint32(long value) {
    if (value < MIN_VALUE || value > MAX_VALUE) {
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
        .u32(new Uint32(new XdrUnsignedInteger(value)))
        .build();
  }

  public static ScvUint32 fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return new ScvUint32(scVal.getU32().getUint32().getNumber());
  }
}
