package org.stellar.sdk;

import org.stellar.sdk.xdr.MemoType;

/**
 * Represents MEMO_NONE.
 */
public class MemoNone extends Memo {
  @Override
  org.stellar.sdk.xdr.Memo toXdr() {
    org.stellar.sdk.xdr.Memo memo = new org.stellar.sdk.xdr.Memo();
    memo.setDiscriminant(MemoType.MEMO_NONE);
    return memo;
  }

  @Override
  public boolean equals(Object o) {
    return this == o || o instanceof MemoNone;
  }

  @Override
  public int hashCode() {
    return MemoNone.class.hashCode();
  }
}
