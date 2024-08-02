package org.stellar.sdk;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import org.junit.Test;

public class PredicateTest {
  @Test
  public void testUnconditional() throws IOException {
    Predicate.Unconditional unconditional = new Predicate.Unconditional();
    String expectedXdr = "AAAAAA==";
    assertEquals(unconditional.toXdr().toXdrBase64(), expectedXdr);
    assertEquals(Predicate.fromXdr(unconditional.toXdr()), unconditional);
  }

  @Test
  public void testPredicateBeforeRelativeTime() throws IOException {
    Predicate.RelBefore beforeRelativeTime = new Predicate.RelBefore(1000);
    assertEquals(1000, beforeRelativeTime.getSecondsSinceClose());
    String expectedXdr = "AAAABQAAAAAAAAPo";
    assertEquals(beforeRelativeTime.toXdr().toXdrBase64(), expectedXdr);
    assertEquals(Predicate.fromXdr(beforeRelativeTime.toXdr()), beforeRelativeTime);
  }

  @Test
  public void testPredicateBeforeAbsoluteTime() throws IOException {
    Predicate.AbsBefore beforeAbsoluteTime = new Predicate.AbsBefore(1601391266);
    assertEquals(Instant.ofEpochSecond(1601391266), beforeAbsoluteTime.getDate());
    assertEquals(BigInteger.valueOf(1601391266), beforeAbsoluteTime.getTimestampSeconds());
    String expectedXdr = "AAAABAAAAABfc0qi";
    assertEquals(beforeAbsoluteTime.toXdr().toXdrBase64(), expectedXdr);
    assertEquals(Predicate.fromXdr(beforeAbsoluteTime.toXdr()), beforeAbsoluteTime);
  }

  @Test
  public void testPredicateNot() throws IOException {
    Predicate.AbsBefore beforeAbsoluteTime = new Predicate.AbsBefore(1601391266);
    Predicate.Not not = new Predicate.Not(beforeAbsoluteTime);
    String expectedXdr = "AAAAAwAAAAEAAAAEAAAAAF9zSqI=";
    assertEquals(not.toXdr().toXdrBase64(), expectedXdr);
    assertEquals(Predicate.fromXdr(not.toXdr()), not);
  }

  @Test
  public void testPredicateAnd1() throws IOException {
    Predicate.AbsBefore beforeAbsoluteTime = new Predicate.AbsBefore(1601391266);
    Predicate.RelBefore beforeRelativeTime = new Predicate.RelBefore(1000);
    Predicate.And and = new Predicate.And(beforeAbsoluteTime, beforeRelativeTime);
    String expectedXdr = "AAAAAQAAAAIAAAAEAAAAAF9zSqIAAAAFAAAAAAAAA+g=";
    assertEquals(and.toXdr().toXdrBase64(), expectedXdr);
    assertEquals(Predicate.fromXdr(and.toXdr()), and);
  }

  @Test
  public void testPredicateAnd2() throws IOException {
    Predicate.AbsBefore beforeAbsoluteTime = new Predicate.AbsBefore(1601391266);
    Predicate.RelBefore beforeRelativeTime = new Predicate.RelBefore(1000);
    Predicate.And and = new Predicate.And(beforeRelativeTime, beforeAbsoluteTime);
    String expectedXdr = "AAAAAQAAAAIAAAAFAAAAAAAAA+gAAAAEAAAAAF9zSqI=";
    assertEquals(and.toXdr().toXdrBase64(), expectedXdr);
    assertEquals(Predicate.fromXdr(and.toXdr()), and);
  }

  @Test
  public void testPredicateOr1() throws IOException {
    Predicate.AbsBefore beforeAbsoluteTime = new Predicate.AbsBefore(1601391266);
    Predicate.RelBefore beforeRelativeTime = new Predicate.RelBefore(1000);
    Predicate.Or or = new Predicate.Or(beforeAbsoluteTime, beforeRelativeTime);
    String expectedXdr = "AAAAAgAAAAIAAAAEAAAAAF9zSqIAAAAFAAAAAAAAA+g=";
    assertEquals(or.toXdr().toXdrBase64(), expectedXdr);
    assertEquals(Predicate.fromXdr(or.toXdr()), or);
  }

  @Test
  public void testPredicateOr2() throws IOException {
    Predicate.AbsBefore beforeAbsoluteTime = new Predicate.AbsBefore(1601391266);
    Predicate.RelBefore beforeRelativeTime = new Predicate.RelBefore(1000);
    Predicate.Or or = new Predicate.Or(beforeRelativeTime, beforeAbsoluteTime);
    String expectedXdr = "AAAAAgAAAAIAAAAFAAAAAAAAA+gAAAAEAAAAAF9zSqI=";
    assertEquals(or.toXdr().toXdrBase64(), expectedXdr);
    assertEquals(Predicate.fromXdr(or.toXdr()), or);
  }

  @Test
  public void testPredicateMix() throws IOException {
    Predicate.AbsBefore beforeAbsoluteTime1 = new Predicate.AbsBefore(1600000000);
    Predicate.Unconditional unconditional = new Predicate.Unconditional();
    Predicate.And and = new Predicate.And(beforeAbsoluteTime1, unconditional);

    Predicate.RelBefore beforeRelativeTime = new Predicate.RelBefore(50000);
    Predicate.AbsBefore beforeAbsoluteTime2 = new Predicate.AbsBefore(1700000000);
    Predicate.Not not = new Predicate.Not(beforeAbsoluteTime2);
    Predicate.Or or = new Predicate.Or(beforeRelativeTime, not);

    Predicate.And and2 = new Predicate.And(and, or);
    String expectedXdr =
        "AAAAAQAAAAIAAAABAAAAAgAAAAQAAAAAX14QAAAAAAAAAAACAAAAAgAAAAUAAAAAAADDUAAAAAMAAAABAAAABAAAAABlU/EA";
    assertEquals(and2.toXdr().toXdrBase64(), expectedXdr);
    assertEquals(Predicate.fromXdr(and2.toXdr()), and2);
  }

  @Test
  public void testAbsBeforeEpochLargeThanInstantMax() {
    BigInteger epoch = BigInteger.valueOf(Instant.MAX.getEpochSecond()).add(BigInteger.ONE);
    Predicate.AbsBefore absBefore = new Predicate.AbsBefore(epoch);
    assertEquals(absBefore.getDate(), Instant.MAX);
    assertEquals(absBefore.getTimestampSeconds(), epoch);
  }

  @Test
  public void testAbsBeforeEpochLessThanInstantMax() {
    BigInteger epoch = BigInteger.valueOf(1691448835L);
    Predicate.AbsBefore absBefore = new Predicate.AbsBefore(epoch);
    assertEquals(absBefore.getDate().toString(), "2023-08-07T22:53:55Z");
    assertEquals(absBefore.getTimestampSeconds(), epoch);
  }
}
