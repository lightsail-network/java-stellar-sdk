package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class LedgerBoundsTest {
  @Test
  public void testSetLedgerBoundsNegativeMinLedger() {
    try {
      new LedgerBounds(-1, 300);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("minLedger must be between 0 and 2^32-1", e.getMessage());
    }
  }

  @Test
  public void testSetLedgerBoundsNegativeMaxLedger() {
    try {
      new LedgerBounds(1, -300);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("maxLedger must be between 0 and 2^32-1", e.getMessage());
    }
  }

  @Test
  public void testSetLedgerBoundsMinLedgerGreaterThanUint32MaxValue() {
    try {
      new LedgerBounds(4294967296L, 300);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("minLedger must be between 0 and 2^32-1", e.getMessage());
    }
  }

  @Test
  public void testSetLedgerBoundsMaxLedgerGreaterThanUint32MaxValue() {
    try {
      new LedgerBounds(1, 4294967296L);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("maxLedger must be between 0 and 2^32-1", e.getMessage());
    }
  }

  @Test
  public void testSetLedgerBoundsMinLedgerGreaterThanMaxLedger() {
    try {
      new LedgerBounds(300, 1);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("minLedger can not be greater than maxLedger", e.getMessage());
    }
  }

  @Test
  public void testSetLedgerBoundsMinLedgerGreaterThanMaxLedgerButMaxLedgerIsZero() {
    LedgerBounds ledgerBounds = new LedgerBounds(300, 0);
    assertEquals(300, ledgerBounds.getMinLedger());
    assertEquals(0, ledgerBounds.getMaxLedger());
    assertEquals(LedgerBounds.fromXdr(ledgerBounds.toXdr()), ledgerBounds);
  }

  @Test
  public void testSetLedgerBounds() {
    LedgerBounds ledgerBounds = new LedgerBounds(300, 400);
    assertEquals(300, ledgerBounds.getMinLedger());
    assertEquals(400, ledgerBounds.getMaxLedger());
    assertEquals(LedgerBounds.fromXdr(ledgerBounds.toXdr()), ledgerBounds);
  }
}
