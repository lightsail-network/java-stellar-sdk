package org.stellar.sdk.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class OperationTest {
  @Test
  public void testToXdrAmount() {
    assertEquals(0L, Operation.toXdrAmount("0"));
    assertEquals(1L, Operation.toXdrAmount("0.0000001"));
    assertEquals(10000000L, Operation.toXdrAmount("1"));
    assertEquals(11234567L, Operation.toXdrAmount("1.1234567"));
    assertEquals(729912843007381L, Operation.toXdrAmount("72991284.3007381"));
    assertEquals(729912843007381L, Operation.toXdrAmount("72991284.30073810"));
    assertEquals(1014016711446800155L, Operation.toXdrAmount("101401671144.6800155"));
    assertEquals(9223372036854775807L, Operation.toXdrAmount("922337203685.4775807"));

    try {
      Operation.toXdrAmount("0.00000001");
      fail();
    } catch (ArithmeticException ignored) {
    } catch (Exception e) {
      fail();
    }

    try {
      Operation.toXdrAmount("72991284.30073811");
      fail();
    } catch (ArithmeticException ignored) {
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testFromXdrAmount() {
    assertEquals("0", Operation.fromXdrAmount(0L));
    assertEquals("0.0000001", Operation.fromXdrAmount(1L));
    assertEquals("1", Operation.fromXdrAmount(10000000L));
    assertEquals("1.1234567", Operation.fromXdrAmount(11234567L));
    assertEquals("72991284.3007381", Operation.fromXdrAmount(729912843007381L));
    assertEquals("101401671144.6800155", Operation.fromXdrAmount(1014016711446800155L));
    assertEquals("922337203685.4775807", Operation.fromXdrAmount(9223372036854775807L));
  }
}
