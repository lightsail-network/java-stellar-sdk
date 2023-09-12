package org.stellar.sdk;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.time.Instant;
import org.junit.Test;

public class PredicateTest {
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
