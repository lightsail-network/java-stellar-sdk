package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.stellar.sdk.xdr.MemoType;
import org.stellar.sdk.xdr.XdrString;

/** Represents MEMO_TEXT. */
@EqualsAndHashCode(callSuper = false)
public class MemoText extends Memo {
  @NonNull private final XdrString text;

  public MemoText(@NonNull String text) {
    this(new XdrString(text));
  }

  public MemoText(byte @NonNull [] text) {
    this(new XdrString(text));
  }

  public MemoText(@NonNull XdrString text) {
    if (text.getBytes().length > 28) {
      throw new IllegalArgumentException("text cannot be more than 28-bytes long.");
    }
    this.text = text;
  }

  public String getText() {
    return this.text.toString();
  }

  public byte[] getBytes() {
    return this.text.getBytes();
  }

  @Override
  org.stellar.sdk.xdr.Memo toXdr() {
    org.stellar.sdk.xdr.Memo memo = new org.stellar.sdk.xdr.Memo();
    memo.setDiscriminant(MemoType.MEMO_TEXT);
    memo.setText(this.text);
    return memo;
  }

  @Override
  public String toString() {
    return this.getText();
  }
}
