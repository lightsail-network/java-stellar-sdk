package org.stellar.sdk;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.MemoType;

import java.nio.charset.Charset;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents MEMO_TEXT.
 */
public class MemoText extends Memo {
  private byte[] text;

  public MemoText(String text) {
    this(checkNotNull(text, "text cannot be null").getBytes((Charset.forName("UTF-8"))));
  }

  public MemoText(byte[] text) {
    this.text = checkNotNull(text, "text cannot be null");
    if (this.text.length > 28) {
      throw new MemoTooLongException("text must be <= 28 bytes. length=" + String.valueOf(this.text.length));
    }
  }

  public String getText() {
    return new String(this.text, Charset.forName("UTF-8"));
  }

  public byte[] getBytes() {
    return this.text;
  }

  @Override
  org.stellar.sdk.xdr.Memo toXdr() {
    org.stellar.sdk.xdr.Memo memo = new org.stellar.sdk.xdr.Memo();
    memo.setDiscriminant(MemoType.MEMO_TEXT);
    memo.setText(text);
    return memo;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.text);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MemoText memoText = (MemoText) o;
    return Arrays.equals(this.text, memoText.text);
  }

    @Override
    public String toString() {
        return text == null ? "" : this.getText();
    }
}
