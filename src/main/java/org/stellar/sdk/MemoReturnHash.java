package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.stellar.sdk.xdr.Memo;
import org.stellar.sdk.xdr.MemoType;

/** Represents MEMO_RETURN. */
@Getter
@EqualsAndHashCode(callSuper = true)
public class MemoReturnHash extends MemoHashAbstract {
  public MemoReturnHash(byte[] bytes) {
    super(bytes);
  }

  public MemoReturnHash(String hexString) {
    super(hexString);
  }

  @Override
  Memo toXdr() {
    org.stellar.sdk.xdr.Memo memo = new org.stellar.sdk.xdr.Memo();
    memo.setDiscriminant(MemoType.MEMO_RETURN);

    org.stellar.sdk.xdr.Hash hash = new org.stellar.sdk.xdr.Hash();
    hash.setHash(bytes);

    memo.setRetHash(hash);
    return memo;
  }
}
