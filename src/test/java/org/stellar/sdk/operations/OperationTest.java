package org.stellar.sdk.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;
import org.junit.Test;

public class OperationTest {
  @Test
  public void testToXdrAmount() {
    assertEquals(0L, Operation.toXdrAmount(new BigDecimal("0.0000000")));
    assertEquals(1L, Operation.toXdrAmount(new BigDecimal("0.0000001")));
    assertEquals(10000000L, Operation.toXdrAmount(new BigDecimal("1")));
    assertEquals(1000000000L, Operation.toXdrAmount(new BigDecimal("100.0000000")));
    assertEquals(-1000000000L, Operation.toXdrAmount(new BigDecimal("-100.0000000")));
    assertEquals(1000000001L, Operation.toXdrAmount(new BigDecimal("100.0000001")));
    assertEquals(1230000001L, Operation.toXdrAmount(new BigDecimal("123.0000001")));
    assertEquals(
        9223372036854775807L, Operation.toXdrAmount(new BigDecimal("922337203685.4775807")));
    assertEquals(
        -9223372036854775808L, Operation.toXdrAmount(new BigDecimal("-922337203685.4775808")));
    assertEquals(922337203686L, Operation.toXdrAmount(new BigDecimal("92233.7203686")));
    assertEquals(-922337203686L, Operation.toXdrAmount(new BigDecimal("-92233.7203686")));
    assertEquals(11234567L, Operation.toXdrAmount(new BigDecimal("1.1234567")));
    assertEquals(729912843007381L, Operation.toXdrAmount(new BigDecimal("72991284.3007381")));
    assertEquals(729912843007381L, Operation.toXdrAmount(new BigDecimal("72991284.30073810")));
    assertEquals(
        1014016711446800155L, Operation.toXdrAmount(new BigDecimal("101401671144.6800155")));
  }

  @Test
  public void testToXdrAmountThrow() {
    assertThrows(
        ArithmeticException.class, () -> Operation.toXdrAmount(new BigDecimal("0.00000001")));
    assertThrows(
        ArithmeticException.class, () -> Operation.toXdrAmount(new BigDecimal("123.00000001")));
    assertThrows(
        ArithmeticException.class,
        () -> Operation.toXdrAmount(new BigDecimal("922337203685.4775808")));
    assertThrows(
        ArithmeticException.class, () -> Operation.toXdrAmount(new BigDecimal("922337203686")));
    assertThrows(
        ArithmeticException.class,
        () -> Operation.toXdrAmount(new BigDecimal("-922337203685.4775809")));
    assertThrows(
        ArithmeticException.class, () -> Operation.toXdrAmount(new BigDecimal("-922337203686")));
    assertThrows(
        ArithmeticException.class,
        () -> Operation.toXdrAmount(new BigDecimal("1000000000000.0000000")));
    assertThrows(
        ArithmeticException.class, () -> Operation.toXdrAmount(new BigDecimal("1000000000000")));
    assertThrows(
        ArithmeticException.class, () -> Operation.toXdrAmount(new BigDecimal("0.12345678")));
  }

  @Test
  public void testFromXdrAmount() {
    assertEquals(0, new BigDecimal("0").compareTo(Operation.fromXdrAmount(0L)));
    assertEquals(0, new BigDecimal("0.0000001").compareTo(Operation.fromXdrAmount(1L)));
    assertEquals(0, new BigDecimal("1").compareTo(Operation.fromXdrAmount(10000000L)));
    assertEquals(0, new BigDecimal("100.0000000").compareTo(Operation.fromXdrAmount(1000000000L)));
    assertEquals(
        0, new BigDecimal("-100.0000000").compareTo(Operation.fromXdrAmount(-1000000000L)));
    assertEquals(0, new BigDecimal("100.0000001").compareTo(Operation.fromXdrAmount(1000000001L)));
    assertEquals(0, new BigDecimal("123.0000001").compareTo(Operation.fromXdrAmount(1230000001L)));
    assertEquals(
        0,
        new BigDecimal("922337203685.4775807")
            .compareTo(Operation.fromXdrAmount(9223372036854775807L)));
    assertEquals(
        0,
        new BigDecimal("-922337203685.4775808")
            .compareTo(Operation.fromXdrAmount(-9223372036854775808L)));
    assertEquals(
        0, new BigDecimal("92233.7203686").compareTo(Operation.fromXdrAmount(922337203686L)));
    assertEquals(
        0, new BigDecimal("-92233.7203686").compareTo(Operation.fromXdrAmount(-922337203686L)));
    assertEquals(0, new BigDecimal("1.1234567").compareTo(Operation.fromXdrAmount(11234567L)));
    assertEquals(
        0, new BigDecimal("72991284.3007381").compareTo(Operation.fromXdrAmount(729912843007381L)));
    assertEquals(
        0,
        new BigDecimal("72991284.30073810").compareTo(Operation.fromXdrAmount(729912843007381L)));
    assertEquals(
        0,
        new BigDecimal("101401671144.6800155")
            .compareTo(Operation.fromXdrAmount(1014016711446800155L)));
  }

  @Test
  public void testFormatAmountScale_ScaleLessThan7() {
    BigDecimal value = new BigDecimal("1.23");
    BigDecimal expected = new BigDecimal("1.2300000");
    BigDecimal actual = Operation.formatAmountScale(value);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatAmountScale_ScaleEqualTo7() {
    BigDecimal value = new BigDecimal("2.3456789");
    BigDecimal expected = new BigDecimal("2.3456789");
    BigDecimal actual = Operation.formatAmountScale(value);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatAmountScale_ScaleGreaterThan7() {
    BigDecimal value = new BigDecimal("3.45678901");
    Operation.formatAmountScale(value);
  }

  @Test
  public void testFormatAmountScale_ScaleZero() {
    BigDecimal value = new BigDecimal("5");
    BigDecimal expected = new BigDecimal("5.0000000");
    BigDecimal actual = Operation.formatAmountScale(value);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatAmountScale_ScaleNegative() {
    BigDecimal value = new BigDecimal("100");
    BigDecimal expected = new BigDecimal("100.0000000");
    BigDecimal actual = Operation.formatAmountScale(value);
    assertEquals(expected, actual);
  }
}
