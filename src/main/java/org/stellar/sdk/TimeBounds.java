package org.stellar.sdk;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.TimePoint;
import org.stellar.sdk.xdr.Uint64;

/**
 * TimeBounds represents the time interval that a transaction is valid.
 *
 * @see Transaction
 */
public final class TimeBounds {
  private final long mMinTime;
  private final long mMaxTime;

  /**
   * @param minTime 64bit Unix timestamp
   * @param maxTime 64bit Unix timestamp
   */
  public TimeBounds(long minTime, long maxTime) {
    if (minTime < 0) {
      throw new IllegalArgumentException("minTime cannot be negative");
    }

    if (maxTime < 0) {
      throw new IllegalArgumentException("maxTime cannot be negative");
    }

    if (maxTime != TransactionPreconditions.TIMEOUT_INFINITE && minTime > maxTime) {
      throw new IllegalArgumentException("minTime must be >= maxTime");
    }

    mMinTime = minTime;
    mMaxTime = maxTime;
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

  public long getMinTime() {
    return mMinTime;
  }

  public long getMaxTime() {
    return mMaxTime;
  }

  public static TimeBounds fromXdr(org.stellar.sdk.xdr.TimeBounds timeBounds) {
    if (timeBounds == null) {
      return null;
    }

    return new TimeBounds(
        timeBounds.getMinTime().getTimePoint().getUint64(),
        timeBounds.getMaxTime().getTimePoint().getUint64());
  }

  public org.stellar.sdk.xdr.TimeBounds toXdr() {
    org.stellar.sdk.xdr.TimeBounds timeBounds = new org.stellar.sdk.xdr.TimeBounds();
    TimePoint minTime = new TimePoint();
    TimePoint maxTime = new TimePoint();
    Uint64 minTimeTemp = new Uint64();
    Uint64 maxTimeTemp = new Uint64();
    minTimeTemp.setUint64(mMinTime);
    maxTimeTemp.setUint64(mMaxTime);
    minTime.setTimePoint(minTimeTemp);
    maxTime.setTimePoint(maxTimeTemp);
    timeBounds.setMinTime(minTime);
    timeBounds.setMaxTime(maxTime);
    return timeBounds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimeBounds that = (TimeBounds) o;
    return mMinTime == that.mMinTime && mMaxTime == that.mMaxTime;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.mMaxTime, this.mMinTime);
  }
}
