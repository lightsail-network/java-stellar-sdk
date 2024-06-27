package org.stellar.sdk.operations;

import static junit.framework.TestCase.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class AccountMergeOperationTest {
  @Test
  public void testBuilder() {
    // from stellar_sdk.operation import *
    //
    // op = AccountMerge(
    //    destination="GDOHZOZMMKIV7NJ566WB745WPLK46KFPSJSHITRBRB6QPT4DI5UJ7NO6",
    //    source="GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW",
    // )
    // print(op.to_xdr_object().to_xdr())
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    String destination = "GDOHZOZMMKIV7NJ566WB745WPLK46KFPSJSHITRBRB6QPT4DI5UJ7NO6";
    AccountMergeOperation op =
        AccountMergeOperation.builder().destination(destination).sourceAccount(source).build();
    assertEquals(destination, op.getDestination());
    assertEquals(source, op.getSourceAccount());
    String expectXdr =
        "AAAAAQAAAAAk4TTtavBWsGnEN3KxHw4Ohwi22ZJHWi8hlamN5pm0TgAAAAgAAAAA3Hy7LGKRX7U996wf87Z61c8or5JkdE4hiH0Hz4NHaJ8=";
    assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testFromXdr() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    String destination = "GDOHZOZMMKIV7NJ566WB745WPLK46KFPSJSHITRBRB6QPT4DI5UJ7NO6";
    AccountMergeOperation op =
        AccountMergeOperation.builder().destination(destination).sourceAccount(source).build();
    org.stellar.sdk.xdr.Operation xdrObject = op.toXdr();
    AccountMergeOperation restoreOp = (AccountMergeOperation) Operation.fromXdr(xdrObject);
    Assert.assertEquals(restoreOp, op);
  }
}
