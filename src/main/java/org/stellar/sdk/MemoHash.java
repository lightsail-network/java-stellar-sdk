package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.stellar.sdk.xdr.MemoType;

/** Represents MEMO_HASH. */
@Getter
@EqualsAndHashCode(callSuper = true)
public class MemoHash extends MemoHashAbstract {
  public MemoHash(byte[] bytes) {
    super(bytes);
  }

  public MemoHash(String hexString) {
    super(hexString);
  }

  @Override
  org.stellar.sdk.xdr.Memo toXdr() {
    org.stellar.sdk.xdr.Memo memo = new org.stellar.sdk.xdr.Memo();
    memo.setDiscriminant(MemoType.MEMO_HASH);

    org.stellar.sdk.xdr.Hash hash = new org.stellar.sdk.xdr.Hash();
    hash.setHash(bytes);

    memo.setHash(hash);
    return memo;
  }
}
