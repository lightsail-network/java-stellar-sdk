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
    SCError scError = SCError.builder().code(errorCode).discriminant(errorType).build();

    SCVal expectedScVal = SCVal.builder().discriminant(SCValType.SCV_ERROR).error(scError).build();

    SCVal actualScVal = Scv.toError(scError);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(scError, Scv.fromError(actualScVal));
  }
}
