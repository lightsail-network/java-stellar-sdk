package org.stellar.sdk.scval;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.stellar.sdk.xdr.SCBytes;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_BYTES}. */
@Value
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
class ScvBytes extends Scv {
  private static final SCValType TYPE = SCValType.SCV_BYTES;

  byte[] value;

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder().discriminant(TYPE).bytes(new SCBytes(value)).build();
  }

  public static ScvBytes fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return new ScvBytes(scVal.getBytes().getSCBytes());
  }
}
