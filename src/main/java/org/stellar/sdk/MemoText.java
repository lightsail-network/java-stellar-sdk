package org.stellar.sdk;

import com.google.common.base.Objects;
import org.stellar.sdk.xdr.MemoType;

import javax.annotation.Nullable;
import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents MEMO_TEXT.
 */
public class MemoText extends Memo {
  private String text;

  public MemoText(String text, @Nullable Charset encoding) {
    this.text = checkNotNull(text, "text cannot be null");

    int length;
    if (encoding != null) {
      length = text.getBytes(encoding).length;
    } else {
      int lengthUtf8 = text.getBytes((Charset.forName("UTF-8"))).length;
      int lengthAscii = text.getBytes((Charset.forName("ASCII"))).length;
      length = Math.min(lengthUtf8, lengthAscii);
    }
    if (length > 28) {
      throw new MemoTooLongException("text must be <= 28 bytes. length=" + length);
    }
  }

  public String getText() {
    return text;
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
    return Objects.hashCode(this.text);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MemoText memoText = (MemoText) o;
    return Objects.equal(this.text, memoText.text);
  }

    @Override
    public String toString() {
        return text == null ? "" : text;
    }
}
