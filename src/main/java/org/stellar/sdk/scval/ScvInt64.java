package org.stellar.sdk.scval;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_I64}. */
@Value
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScvInt64 extends Scv {
  private static final SCValType TYPE = SCValType.SCV_I64;

  public static final long MAX_VALUE = Long.MAX_VALUE;
  public static final long MIN_VALUE = Long.MIN_VALUE;

  long value;

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder().discriminant(TYPE).i64(new Int64(value)).build();
  }

  @Override
  public SCValType getSCValType() {
    return TYPE;
  }

  public static ScvInt64 fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
    return new ScvInt64(scVal.getI64().getInt64());
  }
}
