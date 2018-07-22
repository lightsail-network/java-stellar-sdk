package org.stellar.sdk;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.stellar.sdk.xdr.MemoType;
import org.stellar.sdk.xdr.Uint64;

/**
 * Represents MEMO_ID.
 */
public class MemoId extends Memo {
  private long id;

  public MemoId(long id) {
    if (id < 0) {
      throw new IllegalArgumentException("id must be a positive number");
    }
    this.id = id;
  }

  public long getId() {
    return id;
  }

  @Override
  org.stellar.sdk.xdr.Memo toXdr() {
    org.stellar.sdk.xdr.Memo memo = new org.stellar.sdk.xdr.Memo();
    memo.setDiscriminant(MemoType.MEMO_ID);
    Uint64 idXdr = new Uint64();
    idXdr.setUint64(id);
    memo.setId(idXdr);
    return memo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    MemoId memoId = (MemoId) o;

    return new EqualsBuilder()
            .append(getId(), memoId.getId())
            .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(getId())
            .toHashCode();
  }
}
