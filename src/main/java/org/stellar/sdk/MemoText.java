package org.stellar.sdk;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.stellar.sdk.xdr.MemoType;

import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents MEMO_TEXT.
 */
public class MemoText extends Memo {
  private String text;

  public MemoText(String text) {
    this.text = checkNotNull(text, "text cannot be null");

    int length = text.getBytes((Charset.forName("UTF-8"))).length;
    if (length > 28) {
      throw new MemoTooLongException("text must be <= 28 bytes. length=" + String.valueOf(length));
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
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    MemoText memoText = (MemoText) o;

    return new EqualsBuilder()
            .append(getText(), memoText.getText())
            .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(getText())
            .toHashCode();
  }
}
