package org.stellar.sdk;

import java.math.BigInteger;
import java.time.Instant;
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
  /**
   * Generates Predicate object from a given XDR object.
   *
   * @param xdr XDR object
   * @return Predicate object
   */
  public static Predicate fromXdr(org.stellar.sdk.xdr.ClaimPredicate xdr) {
    switch (xdr.getDiscriminant()) {
      case CLAIM_PREDICATE_UNCONDITIONAL:
        return new Unconditional();
      case CLAIM_PREDICATE_AND:
        return new And(fromXdr(xdr.getAndPredicates()[0]), fromXdr(xdr.getAndPredicates()[1]));
      case CLAIM_PREDICATE_OR:
        return new Or(fromXdr(xdr.getOrPredicates()[0]), fromXdr(xdr.getOrPredicates()[1]));
      case CLAIM_PREDICATE_NOT:
        return new Not(fromXdr(xdr.getNotPredicate()));
      case CLAIM_PREDICATE_BEFORE_RELATIVE_TIME:
        return new RelBefore(xdr.getRelBefore().getInt64());
      case CLAIM_PREDICATE_BEFORE_ABSOLUTE_TIME:
        return new AbsBefore(xdr.getAbsBefore().getInt64());
      default:
        throw new IllegalArgumentException("Unknown predicate type: " + xdr.getDiscriminant());
    }
  }

  @Override
  public abstract boolean equals(Object object);

  /**
   * Generates XDR object from a given Predicate object.
   *
   * @return XDR object
   */
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

  /** Represents a logical NOT of a predicate. */
  @EqualsAndHashCode(callSuper = false)
  @AllArgsConstructor
  @Getter
  public static class Not extends Predicate {
    /** The inner predicate to negate. */
    private final Predicate inner;

    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_NOT);
      xdr.setNotPredicate(inner.toXdr());
      return xdr;
    }
  }

  /** Represents a logical OR of two predicates. */
  @EqualsAndHashCode(callSuper = false)
  @AllArgsConstructor
  @Getter
  public static class Or extends Predicate {
    /** The left predicate. */
    private final Predicate left;

    /** The right predicate. */
    private final Predicate right;

    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_OR);
      xdr.setOrPredicates(new ClaimPredicate[] {left.toXdr(), right.toXdr()});
      return xdr;
    }
  }

  /** Represents a logical AND of two predicates. */
  @EqualsAndHashCode(callSuper = false)
  @AllArgsConstructor
  @Getter
  public static class And extends Predicate {
    /** The left predicate. */
    private final Predicate left;

    /** The right predicate. */
    private final Predicate right;

    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_AND);
      xdr.setAndPredicates(new ClaimPredicate[] {left.toXdr(), right.toXdr()});
      return xdr;
    }
  }

  /** Represents a predicate based on a maximum absolute timestamp. */
  @EqualsAndHashCode(callSuper = false)
  @AllArgsConstructor
  public static class AbsBefore extends Predicate {
    /** The maximum absolute timestamp in seconds. */
    private final TimePoint timePoint;

    public AbsBefore(long epochSeconds) {
      this(new TimePoint(new Uint64(new XdrUnsignedHyperInteger(epochSeconds))));
    }

    public AbsBefore(BigInteger epochSeconds) {
      this(new TimePoint(new Uint64(new XdrUnsignedHyperInteger(epochSeconds))));
    }

    /**
     * Gets the predicate timestamp in seconds.
     *
     * @return the predicate timestamp in seconds
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
      BigInteger seconds = getTimestampSeconds();
      if (seconds.compareTo(BigInteger.valueOf(Instant.MAX.getEpochSecond())) > 0) {
        return Instant.MAX;
      }
      return Instant.ofEpochSecond(seconds.longValue());
    }

    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_BEFORE_ABSOLUTE_TIME);
      xdr.setAbsBefore(new Int64(timePoint.getTimePoint().getUint64().getNumber().longValue()));
      return xdr;
    }
  }

  /**
   * Represents a predicate based on a relative time offset from the close time of the ledger in
   * which the {@link org.stellar.sdk.operations.CreateClaimableBalanceOperation} is included.
   */
  @EqualsAndHashCode(callSuper = false)
  @AllArgsConstructor
  public static class RelBefore extends Predicate {
    /** The relative time offset in seconds from the close time of the ledger. */
    private final Duration duration;

    /**
     * Creates a new predicate based on a relative time offset from the close time of the ledger.
     *
     * @param secondsSinceClose the relative time offset in seconds from the close time of the
     *     ledger
     */
    public RelBefore(long secondsSinceClose) {
      this(new Duration(new Uint64(new XdrUnsignedHyperInteger(secondsSinceClose))));
    }

    /**
     * Gets the relative time offset in seconds.
     *
     * @return the relative time offset in seconds
     */
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
