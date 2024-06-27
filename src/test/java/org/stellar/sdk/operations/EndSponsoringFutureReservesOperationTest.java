package org.stellar.sdk.operations;

import static junit.framework.TestCase.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class EndSponsoringFutureReservesOperationTest {
  @Test
  public void testBuilder() {
    // from stellar_sdk.operation import *
    //
    // op = EndSponsoringFutureReserves(
    //    source="GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW",
    // )
    // print(op.to_xdr_object().to_xdr())
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    EndSponsoringFutureReservesOperation op =
        EndSponsoringFutureReservesOperation.builder().sourceAccount(source).build();
    assertEquals(source, op.getSourceAccount());
    String expectXdr = "AAAAAQAAAAAk4TTtavBWsGnEN3KxHw4Ohwi22ZJHWi8hlamN5pm0TgAAABE=";
    assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testFromXdr() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    EndSponsoringFutureReservesOperation op =
        EndSponsoringFutureReservesOperation.builder().sourceAccount(source).build();
    org.stellar.sdk.xdr.Operation xdrObject = op.toXdr();
    EndSponsoringFutureReservesOperation restoreOp =
        (EndSponsoringFutureReservesOperation) Operation.fromXdr(xdrObject);
    Assert.assertEquals(restoreOp, op);
  }
}
