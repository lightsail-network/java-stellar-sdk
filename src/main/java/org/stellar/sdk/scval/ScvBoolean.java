package org.stellar.sdk.scval;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_BOOL}. */
@Value
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScvBoolean extends Scv {
  private static final SCValType TYPE = SCValType.SCV_BOOL;

  Boolean value;

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder().discriminant(TYPE).b(value).build();
  }

  @Override
  public SCValType getSCValType() {
    return TYPE;
  }

  public static ScvBoolean fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return new ScvBoolean(scVal.getB());
  }
}
