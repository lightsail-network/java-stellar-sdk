package org.stellar.sdk.operations;

import static junit.framework.TestCase.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class BumpSequenceOperationTest {
  @Test
  public void testBuilder() {
    // from stellar_sdk.operation import *
    //
    // op = BumpSequence(
    //    bump_to=1234987466383,
    //    source="GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW",
    // )
    // print(op.to_xdr_object().to_xdr())
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    long bumpTo = 1234987466383L;
    BumpSequenceOperation op =
        BumpSequenceOperation.builder().bumpTo(bumpTo).sourceAccount(source).build();
    assertEquals(bumpTo, op.getBumpTo());
    assertEquals(source, op.getSourceAccount());
    String expectXdr = "AAAAAQAAAAAk4TTtavBWsGnEN3KxHw4Ohwi22ZJHWi8hlamN5pm0TgAAAAsAAAEfiv0+jw==";
    assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testFromXdr() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    long bumpTo = 1234987466383L;
    BumpSequenceOperation op =
        BumpSequenceOperation.builder().bumpTo(bumpTo).sourceAccount(source).build();
    org.stellar.sdk.xdr.Operation xdrObject = op.toXdr();
    BumpSequenceOperation restoreOp = (BumpSequenceOperation) Operation.fromXdr(xdrObject);
    Assert.assertEquals(restoreOp, op);
  }
}
