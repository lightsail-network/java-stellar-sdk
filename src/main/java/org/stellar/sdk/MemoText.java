package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.stellar.sdk.xdr.MemoType;
import org.stellar.sdk.xdr.XdrString;

/** Represents MEMO_TEXT. */
public class MemoText extends Memo {
  private XdrString text;

  public MemoText(String text) {
    this(new XdrString(checkNotNull(text, "text cannot be null")));
  }

  public MemoText(byte[] text) {
    this(new XdrString(checkNotNull(text, "text cannot be null")));
  }

  public MemoText(XdrString text) {
    checkNotNull(text, "text cannot be null");
    checkArgument(text.getBytes().length <= 28, "text cannot be more than 28-bytes long.");
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
  public int hashCode() {
    return this.text.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MemoText memoText = (MemoText) o;
    return this.text.equals(memoText.text);
  }

  @Override
  public String toString() {
    return text == null ? "" : this.getText();
  }
}
