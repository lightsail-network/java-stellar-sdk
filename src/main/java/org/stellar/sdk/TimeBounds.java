package org.stellar.sdk;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.TimePoint;
import org.stellar.sdk.xdr.Uint64;

/**
 * <p>TimeBounds represents the time interval that a transaction is valid.</p>
 * @see Transaction
 */
final public class TimeBounds {
	final private long mMinTime;
	final private long mMaxTime;
	
	/**
	 * @param minTime 64bit Unix timestamp
	 * @param maxTime 64bit Unix timestamp
	 */
	public TimeBounds(long minTime, long maxTime) {
		if (maxTime > 0 && minTime >= maxTime) {
			throw new IllegalArgumentException("minTime must be >= maxTime");
		}
		
		mMinTime = minTime;
		mMaxTime = maxTime;
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
				timeBounds.getMinTime().getTimePoint().getUint64().longValue(),
				timeBounds.getMaxTime().getTimePoint().getUint64().longValue()
		);
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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TimeBounds that = (TimeBounds) o;

		if (mMinTime != that.mMinTime) return false;
		return mMaxTime == that.mMaxTime;
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(
				this.mMaxTime,
				this.mMinTime
		);
	}
}
