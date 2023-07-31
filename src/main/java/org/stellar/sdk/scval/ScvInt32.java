package org.stellar.sdk.scval;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.stellar.sdk.xdr.Int32;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

@Value
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScvInt32 extends Scv {
  private static final SCValType TYPE = SCValType.SCV_I32;
  public static final int MAX_VALUE = Integer.MAX_VALUE;
  public static final int MIN_VALUE = Integer.MIN_VALUE;

  int value;

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder().discriminant(TYPE).i32(new Int32(value)).build();
  }

  @Override
  public SCValType getSCValType() {
    return TYPE;
  }

  public static ScvInt32 fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return new ScvInt32(scVal.getI32().getInt32());
  }
}
