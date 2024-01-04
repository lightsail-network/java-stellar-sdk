package org.stellar.sdk;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.stellar.sdk.xdr.ClaimPredicate;
import org.stellar.sdk.xdr.ClaimPredicateType;
import org.stellar.sdk.xdr.Duration;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.TimePoint;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

/** Base class for predicates. */
public abstract class Predicate {

  private static List<Predicate> convertXDRPredicates(ClaimPredicate[] predicates) {
    List<Predicate> list = new ArrayList<>();
    for (ClaimPredicate p : predicates) {
      list.add(fromXdr(p));
    }
    return list;
  }

  /**
   * Generates Predicate object from a given XDR object
   *
   * @param xdr XDR object
   */
  public static Predicate fromXdr(org.stellar.sdk.xdr.ClaimPredicate xdr) {
    switch (xdr.getDiscriminant()) {
      case CLAIM_PREDICATE_UNCONDITIONAL:
        return new Unconditional();
      case CLAIM_PREDICATE_AND:
        return new And(convertXDRPredicates(xdr.getAndPredicates()));
      case CLAIM_PREDICATE_OR:
        return new Or(convertXDRPredicates(xdr.getOrPredicates()));
      case CLAIM_PREDICATE_NOT:
        return new Not(fromXdr(xdr.getNotPredicate()));
      case CLAIM_PREDICATE_BEFORE_RELATIVE_TIME:
        return new RelBefore(xdr.getRelBefore().getInt64());
      case CLAIM_PREDICATE_BEFORE_ABSOLUTE_TIME:
        return new AbsBefore(xdr.getAbsBefore().getInt64());
      default:
        throw new IllegalArgumentException("Unknown asset type " + xdr.getDiscriminant());
    }
  }

  @Override
  public abstract boolean equals(Object object);

  /** Generates XDR object from a given Asset object */
  public abstract org.stellar.sdk.xdr.ClaimPredicate toXdr();

  /** Represents a predicate that is always true. */
  @EqualsAndHashCode(callSuper = false)
  public static class Unconditional extends Predicate {
    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_UNCONDITIONAL);
      return xdr;
    }
  }

  @EqualsAndHashCode(callSuper = false)
  @AllArgsConstructor
  @Getter
  public static class Not extends Predicate {
    private final Predicate inner;

    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_NOT);
      xdr.setNotPredicate(inner.toXdr());
      return xdr;
    }
  }

  @EqualsAndHashCode(callSuper = false)
  @AllArgsConstructor
  @Getter
  public static class Or extends Predicate {
    private final List<Predicate> inner;

    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_OR);
      ClaimPredicate[] xdrInner = new ClaimPredicate[inner.size()];
      for (int i = 0; i < inner.size(); i++) {
        xdrInner[i] = inner.get(i).toXdr();
      }
      xdr.setOrPredicates(xdrInner);
      return xdr;
    }
  }

  @EqualsAndHashCode(callSuper = false)
  @AllArgsConstructor
  @Getter
  public static class And extends Predicate {
    private final List<Predicate> inner;

    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_AND);
      ClaimPredicate[] xdrInner = new ClaimPredicate[inner.size()];
      for (int i = 0; i < inner.size(); i++) {
        xdrInner[i] = inner.get(i).toXdr();
      }
      xdr.setAndPredicates(xdrInner);
      return xdr;
    }
  }

  /** Represents a predicate based on a maximum date and time. */
  @EqualsAndHashCode(callSuper = false)
  public static class AbsBefore extends Predicate {
    private final TimePoint timePoint;

    public AbsBefore(TimePoint timePoint) {
      this.timePoint = timePoint;
    }

    public AbsBefore(long epochSeconds) {
      this(new TimePoint(new Uint64(new XdrUnsignedHyperInteger(epochSeconds))));
    }

    public AbsBefore(BigInteger epochSeconds) {
      this(new TimePoint(new Uint64(new XdrUnsignedHyperInteger(epochSeconds))));
    }

    /**
     * Gets the Predicate epoch in seconds.
     *
     * @return BigInteger the predicate epoch in seconds
     */
    public BigInteger getTimestampSeconds() {
      return timePoint.getTimePoint().getUint64().getNumber();
    }

    /**
     * Gets the java date representation of a Predicate. If the Predicate specifies an epoch larger
     * than 31556889864403199, it will coerce to {@link Instant#MAX} instead as no greater value can
     * be represented.
     *
     * <p>If you want to get the real epoch, use {@link #getTimestampSeconds()} instead.
     *
     * @return Instant the java date representation of the predicate
     */
    public Instant getDate() {
      Instant instantMax = Instant.MAX;
      if (getTimestampSeconds().compareTo(BigInteger.valueOf(instantMax.getEpochSecond())) > 0) {
        return instantMax;
      }

      return Instant.ofEpochSecond(timePoint.getTimePoint().getUint64().getNumber().longValue());
    }

    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_BEFORE_ABSOLUTE_TIME);
      xdr.setAbsBefore(new Int64(timePoint.getTimePoint().getUint64().getNumber().longValue()));
      return xdr;
    }
  }

  /** Represents predicate based on maximum length of time */
  @EqualsAndHashCode(callSuper = false)
  @AllArgsConstructor
  public static class RelBefore extends Predicate {
    private final Duration duration;

    public RelBefore(long secondsSinceClose) {
      this(new Duration(new Uint64(new XdrUnsignedHyperInteger(secondsSinceClose))));
    }

    public long getSecondsSinceClose() {
      return duration.getDuration().getUint64().getNumber().longValue();
    }

    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_BEFORE_RELATIVE_TIME);
      xdr.setRelBefore(new Int64(duration.getDuration().getUint64().getNumber().longValue()));
      return xdr;
    }
  }
}
