package org.stellar.sdk;

import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.xdr.MemoType;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

/** Represents MEMO_ID. */
@Getter
@EqualsAndHashCode(callSuper = false)
public class MemoId extends Memo {
  @NonNull private final BigInteger id;

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

  @Override
  org.stellar.sdk.xdr.Memo toXdr() {
    org.stellar.sdk.xdr.Memo memo = new org.stellar.sdk.xdr.Memo();
    memo.setDiscriminant(MemoType.MEMO_ID);
    Uint64 idXdr = new Uint64(new XdrUnsignedHyperInteger(id));
    memo.setId(idXdr);
    return memo;
  }

  @Override
  public String toString() {
    return id.toString();
  }
}
