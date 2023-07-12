package org.stellar.sdk;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeBoundsTest {
  @Test
  public void testSetTimeBoundsNegativeMinTime() {
    try {
      new TimeBounds(-1, 300);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("minTime cannot be negative", e.getMessage());
    }
  }

  @Test
  public void testSetTimeBoundsNegativeMaxTime() {
    try {
      new TimeBounds(1, -300);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("maxTime cannot be negative", e.getMessage());
    }
  }

  @Test
  public void testSetTimeBoundsMinTimeGreatThanMaxTime() {
    try {
      new TimeBounds(300, 1);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("minTime must be >= maxTime", e.getMessage());
    }
  }

  @Test
  public void TestSetTimeoutInfinite() {
    TimeBounds timebounds = new TimeBounds(300, 0);
    assertEquals(300, timebounds.getMinTime());
    assertEquals(0, timebounds.getMaxTime());
  }

  @Test
  public void TestSetTimeoutInfiniteBothZero() {
    TimeBounds timebounds = new TimeBounds(0, 0);
    assertEquals(0, timebounds.getMinTime());
    assertEquals(0, timebounds.getMaxTime());
  }

  @Test
  public void TestSetTimeout() {
    TimeBounds timebounds = new TimeBounds(1, 300);
    assertEquals(1, timebounds.getMinTime());
    assertEquals(300, timebounds.getMaxTime());
  }

  @Test
  public void TestSetTimeoutMinEqualMax() {
    TimeBounds timebounds = new TimeBounds(300, 300);
    assertEquals(300, timebounds.getMinTime());
    assertEquals(300, timebounds.getMaxTime());
  }

  @Test
  public void TestTimeoutWithTimeout() {
    long timeout = 300;
    TimeBounds timebounds = TimeBounds.expiresAfter(timeout);
    long now = System.currentTimeMillis() / 1000L;
    assertEquals(0, timebounds.getMinTime());
    assertTrue(
        timebounds.getMaxTime() - timeout <= now && timebounds.getMaxTime() - timeout >= now - 1);
  }
}
