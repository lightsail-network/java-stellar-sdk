package org.stellar.sdk.operations;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

import org.junit.Assert;
import org.junit.Test;

public class RestoreFootprintOperationTest {
  @Test
  public void testBuilderWithSource() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    RestoreFootprintOperation op =
        RestoreFootprintOperation.builder().sourceAccount(source).build();
    assertEquals(source, op.getSourceAccount());
    String expectXdr = "AAAAAQAAAAAk4TTtavBWsGnEN3KxHw4Ohwi22ZJHWi8hlamN5pm0TgAAABoAAAAA";
    assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testBuilderWithoutSource() {
    RestoreFootprintOperation op = RestoreFootprintOperation.builder().build();
    assertNull(op.getSourceAccount());
    String expectXdr = "AAAAAAAAABoAAAAA";
    assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testFromXdr() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    RestoreFootprintOperation originOp =
        RestoreFootprintOperation.builder().sourceAccount(source).build();
    org.stellar.sdk.xdr.Operation xdrObject = originOp.toXdr();
    Operation restartOp = Operation.fromXdr(xdrObject);
    Assert.assertEquals(restartOp, originOp);
  }

  @Test
  public void testEquals() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    RestoreFootprintOperation operation1 =
        RestoreFootprintOperation.builder().sourceAccount(source).build();
    RestoreFootprintOperation operation2 =
        RestoreFootprintOperation.builder().sourceAccount(source).build();
    assertEquals(operation1, operation2);
  }

  @Test
  public void testNotEquals() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    RestoreFootprintOperation operation1 =
        RestoreFootprintOperation.builder().sourceAccount(source).build();
    RestoreFootprintOperation operation2 = RestoreFootprintOperation.builder().build();
    Assert.assertNotEquals(operation1, operation2);
  }
}
