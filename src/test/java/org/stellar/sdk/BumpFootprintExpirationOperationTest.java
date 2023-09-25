package org.stellar.sdk;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

import org.junit.Assert;
import org.junit.Test;

public class BumpFootprintExpirationOperationTest {
  @Test
  public void testBuilderWithSource() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    BumpFootprintExpirationOperation op =
        BumpFootprintExpirationOperation.builder()
            .ledgersToExpire(123L)
            .sourceAccount(source)
            .build();
    assertEquals(Long.valueOf(123), op.getLedgersToExpire());
    assertEquals(source, op.getSourceAccount());
    String expectXdr = "AAAAAQAAAAAk4TTtavBWsGnEN3KxHw4Ohwi22ZJHWi8hlamN5pm0TgAAABkAAAAAAAAAew==";
    assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testBuilderWithoutSource() {
    BumpFootprintExpirationOperation op =
        BumpFootprintExpirationOperation.builder().ledgersToExpire(123L).build();
    assertEquals(Long.valueOf(123), op.getLedgersToExpire());
    assertNull(op.getSourceAccount());
    String expectXdr = "AAAAAAAAABkAAAAAAAAAew==";
    assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testFromXdr() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    BumpFootprintExpirationOperation originOp =
        BumpFootprintExpirationOperation.builder()
            .ledgersToExpire(123L)
            .sourceAccount(source)
            .build();
    org.stellar.sdk.xdr.Operation xdrObject = originOp.toXdr();
    Operation restartOp = Operation.fromXdr(xdrObject);
    Assert.assertEquals(restartOp, originOp);
  }

  @Test
  public void testEquals() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    BumpFootprintExpirationOperation operation1 =
        BumpFootprintExpirationOperation.builder()
            .ledgersToExpire(123L)
            .sourceAccount(source)
            .build();
    BumpFootprintExpirationOperation operation2 =
        BumpFootprintExpirationOperation.builder()
            .ledgersToExpire(123L)
            .sourceAccount(source)
            .build();
    assertEquals(operation1, operation2);
  }

  @Test
  public void testNotEquals() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    BumpFootprintExpirationOperation operation1 =
        BumpFootprintExpirationOperation.builder()
            .ledgersToExpire(123L)
            .sourceAccount(source)
            .build();
    BumpFootprintExpirationOperation operation2 =
        BumpFootprintExpirationOperation.builder()
            .ledgersToExpire(124L)
            .sourceAccount(source)
            .build();
    Assert.assertNotEquals(operation1, operation2);
  }

  @Test
  public void testNotEqualsSource() {
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    BumpFootprintExpirationOperation operation1 =
        BumpFootprintExpirationOperation.builder()
            .ledgersToExpire(123L)
            .sourceAccount(source)
            .build();
    BumpFootprintExpirationOperation operation2 =
        BumpFootprintExpirationOperation.builder().ledgersToExpire(123L).build();
    Assert.assertNotEquals(operation1, operation2);
  }

  @Test
  public void testLedgersToExpireIsInvalidThrowsLessThanZero() {
    try {
      BumpFootprintExpirationOperation.builder().ledgersToExpire(-1L).build();
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("ledgersToExpire isn't a ledger quantity (uint32)", e.getMessage());
    }
  }

  @Test
  public void testLedgersToExpireIsInvalidThrowsGreatThanMaxUint32() {
    try {
      BumpFootprintExpirationOperation.builder().ledgersToExpire(4294967296L).build();
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("ledgersToExpire isn't a ledger quantity (uint32)", e.getMessage());
    }
  }

  @Test
  public void testLedgersToExpireIsNullThrows() {
    try {
      BumpFootprintExpirationOperation.builder().build();
      Assert.fail();
    } catch (NullPointerException e) {
      Assert.assertEquals("ledgersToExpire is marked non-null but is null", e.getMessage());
    }
  }
}
