package org.stellar.sdk;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.stellar.sdk.xdr.ClaimPredicate;
import org.stellar.sdk.xdr.ClaimPredicateType;
import org.stellar.sdk.xdr.Int64;
import org.threeten.bp.Instant;

import java.util.List;

public abstract class Predicate {

  private static List<Predicate> convertXDRPredicates(ClaimPredicate[] predicates) {
    List<Predicate> list = Lists.newArrayList();
    for (ClaimPredicate p : predicates) {
      list.add(fromXdr(p));
    }
    return list;
  }

  /**
   * Generates Predicate object from a given XDR object
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

  /**
   * Generates XDR object from a given Asset object
   */
  public abstract org.stellar.sdk.xdr.ClaimPredicate toXdr();

  public static class Unconditional extends Predicate {
    @Override
    public boolean equals(Object o) {
      return (this == o) || (getClass() == o.getClass());
    }

    @Override
    public int hashCode() {
      return 0;
    }

    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_UNCONDITIONAL);
      return xdr;
    }
  }

  public static class Not extends Predicate {
    private final Predicate inner;
    public Not(Predicate inner) {
      this.inner = inner;
    }

    public Predicate getInner() {
      return inner;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      return (getClass() == o.getClass()) && Objects.equal(inner, ((Not)o).inner);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(inner);
    }

    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_NOT);
      xdr.setNotPredicate(inner.toXdr());
      return xdr;
    }
  }

  public static class Or extends Predicate {
    private final List<Predicate> inner;

    public Or(List<Predicate> inner) {
      this.inner = inner;
    }

    public List<Predicate> getInner() {
      return inner;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      return (getClass() == o.getClass()) && Objects.equal(inner, ((Or)o).inner);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(inner);
    }

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


  public static class And extends Predicate {
    private final List<Predicate> inner;

    public And(List<Predicate> inner) {
      this.inner = inner;
    }

    public List<Predicate> getInner() {
      return inner;
    }


    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      return (getClass() == o.getClass()) && Objects.equal(inner, ((And)o).inner);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(inner);
    }

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

  public static class AbsBefore extends Predicate {
    private final long epochSeconds;

    public AbsBefore(long epochSeconds) {
      this.epochSeconds = epochSeconds;
    }

    public long getTimestampSeconds() {
      return epochSeconds;
    }

    public Instant getDate() {
      return Instant.ofEpochSecond(epochSeconds);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      return (getClass() == o.getClass()) && Objects.equal(epochSeconds, ((AbsBefore)o).epochSeconds);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(epochSeconds);
    }

    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_BEFORE_ABSOLUTE_TIME);
      Int64 t = new Int64();
      t.setInt64(epochSeconds);
      xdr.setAbsBefore(t);
      return xdr;
    }
  }

  public static class RelBefore extends Predicate {
    private final long secondsSinceClose;

    public RelBefore(long secondsSinceClose) {
      this.secondsSinceClose = secondsSinceClose;
    }

    public long getSecondsSinceClose() {
      return secondsSinceClose;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      return (getClass() == o.getClass()) && Objects.equal(secondsSinceClose, ((RelBefore)o).secondsSinceClose);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(secondsSinceClose);
    }

    @Override
    public ClaimPredicate toXdr() {
      org.stellar.sdk.xdr.ClaimPredicate xdr = new org.stellar.sdk.xdr.ClaimPredicate();
      xdr.setDiscriminant(ClaimPredicateType.CLAIM_PREDICATE_BEFORE_RELATIVE_TIME);
      Int64 t = new Int64();
      t.setInt64(secondsSinceClose);
      xdr.setRelBefore(t);
      return xdr;
    }
  }

  }
