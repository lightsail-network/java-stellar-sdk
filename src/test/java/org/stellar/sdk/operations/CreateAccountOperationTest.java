package org.stellar.sdk.operations;

import static junit.framework.TestCase.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class CreateAccountOperationTest {
  @Test
  public void testBuilder() {
    // from stellar_sdk.operation import *
    //
    // op = CreateAccount(
    //    destination="GDOHZOZMMKIV7NJ566WB745WPLK46KFPSJSHITRBRB6QPT4DI5UJ7NO6",
    //    starting_balance="10.25",
    //    source="GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW",
    // )
    // print(op.to_xdr_object().to_xdr())
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    String startingBalance = "10.25";
    String destination = "GDOHZOZMMKIV7NJ566WB745WPLK46KFPSJSHITRBRB6QPT4DI5UJ7NO6";
    CreateAccountOperation op =
        CreateAccountOperation.builder()
            .destination(destination)
            .startingBalance(startingBalance)
            .sourceAccount(source)
            .build();
    assertEquals(destination, op.getDestination());
    assertEquals(source, op.getSourceAccount());
    String expectXdr =
        "AAAAAQAAAAAk4TTtavBWsGnEN3KxHw4Ohwi22ZJHWi8hlamN5pm0TgAAAAAAAAAA3Hy7LGKRX7U996wf87Z61c8or5JkdE4hiH0Hz4NHaJ8AAAAABhwGoA==";
    assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testFromXdr() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    String startingBalance = "10.25";
    String destination = "GDOHZOZMMKIV7NJ566WB745WPLK46KFPSJSHITRBRB6QPT4DI5UJ7NO6";
    CreateAccountOperation op =
        CreateAccountOperation.builder()
            .destination(destination)
            .startingBalance(startingBalance)
            .sourceAccount(source)
            .build();
    org.stellar.sdk.xdr.Operation xdrObject = op.toXdr();
    CreateAccountOperation restoreOp = (CreateAccountOperation) Operation.fromXdr(xdrObject);
    Assert.assertEquals(restoreOp, op);
  }
}
