package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import org.stellar.sdk.xdr.MemoType;

/** Represents MEMO_NONE. */
@EqualsAndHashCode(callSuper = false)
public class MemoNone extends Memo {
  @Override
  org.stellar.sdk.xdr.Memo toXdr() {
    org.stellar.sdk.xdr.Memo memo = new org.stellar.sdk.xdr.Memo();
    memo.setDiscriminant(MemoType.MEMO_NONE);
    return memo;
  }

  @Override
  public String toString() {
    return "";
  }
}
