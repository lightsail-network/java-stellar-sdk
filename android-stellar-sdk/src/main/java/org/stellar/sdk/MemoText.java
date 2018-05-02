package org.stellar.sdk;

import static org.stellar.sdk.Util.CHARSET_UTF8;
import static org.stellar.sdk.Util.checkNotNull;

import java.io.UnsupportedEncodingException;
import org.stellar.sdk.xdr.MemoType;

/**
 * Represents MEMO_TEXT.
 */
public class MemoText extends Memo {
  private String text;

  public MemoText(String text) {
    this.text = checkNotNull(text, "text cannot be null");

    int length = 0;
    try {
      length = text.getBytes(CHARSET_UTF8).length;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
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
}
