package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.SCError;
import org.stellar.sdk.xdr.SCErrorCode;
import org.stellar.sdk.xdr.SCErrorType;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvErrorTest {
  @Test
  public void testScvError() {
    SCErrorType errorType = SCErrorType.SCE_CONTEXT;
    SCErrorCode errorCode = SCErrorCode.SCEC_UNEXPECTED_TYPE;

    ScvError scvError = new ScvError(errorType, errorCode);
    SCVal scVal = scvError.toSCVal();

    assertEquals(scvError.getSCValType(), SCValType.SCV_ERROR);
    assertEquals(scvError.getErrorCode(), errorCode);
    assertEquals(scvError.getErrorType(), errorType);

    assertEquals(ScvError.fromSCVal(scVal), scvError);
    assertEquals(Scv.fromSCVal(scVal), scvError);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_ERROR)
            .error(new SCError.Builder().code(errorCode).type(errorType).build())
            .build();
    assertEquals(expectedScVal, scVal);
  }
}
