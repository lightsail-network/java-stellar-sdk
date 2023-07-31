package org.stellar.sdk.scval;

import lombok.EqualsAndHashCode;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

@EqualsAndHashCode(callSuper = false)
public class ScvVoid extends Scv {
  private static final SCValType TYPE = SCValType.SCV_VOID;

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder().discriminant(TYPE).build();
  }

  @Override
  public SCValType getSCValType() {
    return TYPE;
  }

  public static ScvVoid fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
    return new ScvVoid();
  }
}
