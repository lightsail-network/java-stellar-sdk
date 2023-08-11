package org.stellar.sdk.scval;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.stellar.sdk.xdr.SCError;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_ERROR}. */
@Value
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
class ScvError extends Scv {
  private static final SCValType TYPE = SCValType.SCV_ERROR;

  SCError value;

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder()
        .discriminant(TYPE)
        .error(new SCError.Builder().code(value.getCode()).type(value.getType()).build())
        .build();
  }

  public static ScvError fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return new ScvError(scVal.getError());
  }
}
