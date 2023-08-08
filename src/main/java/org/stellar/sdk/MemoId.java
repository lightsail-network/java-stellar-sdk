package org.stellar.sdk;

import com.google.common.base.Objects;
import java.math.BigInteger;
import org.stellar.sdk.xdr.MemoType;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

/** Represents MEMO_ID. */
public class MemoId extends Memo {
  private final BigInteger id;

  public MemoId(BigInteger id) {
    if (id.compareTo(XdrUnsignedHyperInteger.MIN_VALUE) < 0
        || id.compareTo(XdrUnsignedHyperInteger.MAX_VALUE) > 0) {
      throw new IllegalArgumentException("MEMO_ID must be between 0 and 2^64-1");
    }
    this.id = id;
  }

  public MemoId(Long id) {
    this(BigInteger.valueOf(id));
  }

  public BigInteger getId() {
    return id;
  }

  @Override
  org.stellar.sdk.xdr.Memo toXdr() {
    org.stellar.sdk.xdr.Memo memo = new org.stellar.sdk.xdr.Memo();
    memo.setDiscriminant(MemoType.MEMO_ID);
    Uint64 idXdr = new Uint64(new XdrUnsignedHyperInteger(id));
    memo.setId(idXdr);
    return memo;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MemoId memoId = (MemoId) o;
    return Objects.equal(id, memoId.id);
  }

  @Override
  public String toString() {
    return id.toString();
  }
}
