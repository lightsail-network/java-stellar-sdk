package org.stellar.sdk.scval;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.stellar.sdk.xdr.SCErrorCode;
import org.stellar.sdk.xdr.SCErrorType;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

@Value
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScvError extends Scv {
  private static final SCValType TYPE = SCValType.SCV_ERROR;

  SCErrorType errorType;
  SCErrorCode errorCode;

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder()
        .discriminant(TYPE)
        .error(new org.stellar.sdk.xdr.SCError.Builder().code(errorCode).type(errorType).build())
        .build();
  }

  @Override
  public SCValType getSCValType() {
    return TYPE;
  }

  public static ScvError fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return new ScvError(scVal.getError().getType(), scVal.getError().getCode());
  }
}
