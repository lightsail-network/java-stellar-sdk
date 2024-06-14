package org.stellar.sdk.operations;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

import org.junit.Assert;
import org.junit.Test;

public class ExtendFootprintTTLOperationTest {
  @Test
  public void testBuilderWithSource() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    ExtendFootprintTTLOperation op =
        ExtendFootprintTTLOperation.builder().extendTo(123L).sourceAccount(source).build();
    assertEquals(Long.valueOf(123), op.getExtendTo());
    assertEquals(source, op.getSourceAccount());
    String expectXdr = "AAAAAQAAAAAk4TTtavBWsGnEN3KxHw4Ohwi22ZJHWi8hlamN5pm0TgAAABkAAAAAAAAAew==";
    assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testBuilderWithoutSource() {
    ExtendFootprintTTLOperation op = ExtendFootprintTTLOperation.builder().extendTo(123L).build();
    assertEquals(Long.valueOf(123), op.getExtendTo());
    assertNull(op.getSourceAccount());
    String expectXdr = "AAAAAAAAABkAAAAAAAAAew==";
    assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testFromXdr() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    ExtendFootprintTTLOperation originOp =
        ExtendFootprintTTLOperation.builder().extendTo(123L).sourceAccount(source).build();
    org.stellar.sdk.xdr.Operation xdrObject = originOp.toXdr();
    Operation restartOp = Operation.fromXdr(xdrObject);
    Assert.assertEquals(restartOp, originOp);
  }

  @Test
  public void testEquals() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    ExtendFootprintTTLOperation operation1 =
        ExtendFootprintTTLOperation.builder().extendTo(123L).sourceAccount(source).build();
    ExtendFootprintTTLOperation operation2 =
        ExtendFootprintTTLOperation.builder().extendTo(123L).sourceAccount(source).build();
    assertEquals(operation1, operation2);
  }

  @Test
  public void testNotEquals() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    ExtendFootprintTTLOperation operation1 =
        ExtendFootprintTTLOperation.builder().extendTo(123L).sourceAccount(source).build();
    ExtendFootprintTTLOperation operation2 =
        ExtendFootprintTTLOperation.builder().extendTo(124L).sourceAccount(source).build();
    Assert.assertNotEquals(operation1, operation2);
  }

  @Test
  public void testNotEqualsSource() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    ExtendFootprintTTLOperation operation1 =
        ExtendFootprintTTLOperation.builder().extendTo(123L).sourceAccount(source).build();
    ExtendFootprintTTLOperation operation2 =
        ExtendFootprintTTLOperation.builder().extendTo(123L).build();
    Assert.assertNotEquals(operation1, operation2);
  }

  @Test
  public void testLedgersToExpireIsInvalidThrowsLessThanZero() {
    try {
      ExtendFootprintTTLOperation.builder().extendTo(-1L).build();
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("extendTo isn't a ledger quantity (uint32)", e.getMessage());
    }
  }

  @Test
  public void testLedgersToExpireIsInvalidThrowsGreatThanMaxUint32() {
    try {
      ExtendFootprintTTLOperation.builder().extendTo(4294967296L).build();
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("extendTo isn't a ledger quantity (uint32)", e.getMessage());
    }
  }

  @Test
  public void testLedgersToExpireIsNullThrows() {
    try {
      ExtendFootprintTTLOperation.builder().build();
      Assert.fail();
    } catch (NullPointerException e) {
      Assert.assertEquals("extendTo is marked non-null but is null", e.getMessage());
    }
  }
}
