package org.stellar.sdk;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
		if(minTime >= maxTime) {
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
	
	public org.stellar.sdk.xdr.TimeBounds toXdr() {
		org.stellar.sdk.xdr.TimeBounds timeBounds = new org.stellar.sdk.xdr.TimeBounds();
		Uint64 minTime = new Uint64();
		Uint64 maxTime = new Uint64();
		minTime.setUint64(mMinTime);
		maxTime.setUint64(mMaxTime);
		timeBounds.setMinTime(minTime);
		timeBounds.setMaxTime(maxTime);
		return timeBounds;
	}

	/**
	 * Constructs a TimeBounds instance from an XDR TimeBounds instance.
	 */
	public static TimeBounds fromXdr(org.stellar.sdk.xdr.TimeBounds bounds) {
		return (bounds == null) ? null : new TimeBounds(
				bounds.getMinTime().getUint64(),
				bounds.getMaxTime().getUint64()
		);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		TimeBounds that = (TimeBounds) o;

		return new EqualsBuilder()
				.append(mMinTime, that.mMinTime)
				.append(mMaxTime, that.mMaxTime)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(mMinTime)
				.append(mMaxTime)
				.toHashCode();
	}
}
