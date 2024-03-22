package org.stellar.sdk;

import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.stellar.sdk.xdr.TimePoint;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

/**
 * TimeBounds represents the time interval that a transaction is valid.
 *
 * <p>The UNIX timestamp (in seconds), determined by ledger time, of a lower and upper bound of when
 * this transaction will be valid. If a transaction is submitted too early or too late, it will fail
 * to make it into the transaction set.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/fundamentals-and-concepts/stellar-data-structures/operations-and-transactions#time-bounds"
 *     target="_blank">Time Bounds</a>
 * @see Transaction
 */
@Getter
@EqualsAndHashCode
public final class TimeBounds {
  /** the UNIX timestamp (in seconds) */
  private final BigInteger minTime;

  /** the UNIX timestamp (in seconds) */
  private final BigInteger maxTime;

  /**
   * @param minTime 64bit Unix timestamp
   * @param maxTime 64bit Unix timestamp
   */
  public TimeBounds(BigInteger minTime, BigInteger maxTime) {
    if (minTime.compareTo(XdrUnsignedHyperInteger.MIN_VALUE) < 0
        || minTime.compareTo(XdrUnsignedHyperInteger.MAX_VALUE) > 0) {
      throw new IllegalArgumentException("minTime must be between 0 and 2^64-1");
    }

    if (maxTime.compareTo(XdrUnsignedHyperInteger.MIN_VALUE) < 0
        || maxTime.compareTo(XdrUnsignedHyperInteger.MAX_VALUE) > 0) {
      throw new IllegalArgumentException("maxTime must be between 0 and 2^64-1");
    }

    if (!TransactionPreconditions.TIMEOUT_INFINITE.equals(maxTime)
        && minTime.compareTo(maxTime) > 0) {
      throw new IllegalArgumentException("minTime must be <= maxTime");
    }

    this.minTime = minTime;
    this.maxTime = maxTime;
  }

  /**
   * @param minTime 64bit Unix timestamp
   * @param maxTime 64bit Unix timestamp
   */
  public TimeBounds(long minTime, long maxTime) {
    this(BigInteger.valueOf(minTime), BigInteger.valueOf(maxTime));
  }

  /**
   * A factory method that sets maxTime to the specified second from now.
   *
   * @param timeout Timeout in seconds.
   * @return TimeBounds
   */
  public static TimeBounds expiresAfter(long timeout) {
    long now = System.currentTimeMillis() / 1000L;
    long endTime = now + timeout;
    return new TimeBounds(0, endTime);
  }

  public static TimeBounds fromXdr(org.stellar.sdk.xdr.TimeBounds timeBounds) {
    if (timeBounds == null) {
      return null;
    }

    return new TimeBounds(
        timeBounds.getMinTime().getTimePoint().getUint64().getNumber(),
        timeBounds.getMaxTime().getTimePoint().getUint64().getNumber());
  }

  public org.stellar.sdk.xdr.TimeBounds toXdr() {
    org.stellar.sdk.xdr.TimeBounds timeBounds = new org.stellar.sdk.xdr.TimeBounds();
    TimePoint minTime = new TimePoint();
    TimePoint maxTime = new TimePoint();
    Uint64 minTimeTemp = new Uint64();
    Uint64 maxTimeTemp = new Uint64();
    minTimeTemp.setUint64(new XdrUnsignedHyperInteger(this.minTime));
    maxTimeTemp.setUint64(new XdrUnsignedHyperInteger(this.maxTime));
    minTime.setTimePoint(minTimeTemp);
    maxTime.setTimePoint(maxTimeTemp);
    timeBounds.setMinTime(minTime);
    timeBounds.setMaxTime(maxTime);
    return timeBounds;
  }
}
