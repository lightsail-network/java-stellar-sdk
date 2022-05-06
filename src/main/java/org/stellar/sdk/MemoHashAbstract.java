package org.stellar.sdk;

import com.google.common.io.BaseEncoding;

import java.util.Arrays;

abstract class MemoHashAbstract extends Memo {
  protected byte[] bytes;

  public MemoHashAbstract(byte[] bytes) {
    if (bytes.length != 32) {
        throw new IncorrectMemoSizeException("MEMO_HASH must contain 32 bytes.", bytes.length);
    }

    this.bytes = bytes;
  }

  public MemoHashAbstract(String hexString) {
    // We change to lowercase because we want to decode both: upper cased and lower cased alphabets.
    this(BaseEncoding.base16().lowerCase().decode(hexString.toLowerCase()));
  }

  /**
   * Returns 32 bytes long array contained in this memo.
   */
  public byte[] getBytes() {
    return bytes;
  }

  /**
   * <p>Returns hex representation of bytes contained in this memo.</p>
   *
   * <p>Example:</p>
   * <code>
   *   MemoHash memo = new MemoHash("e98869bba8bce08c10b78406202127f3888c25454cd37b02600862452751f526");
   *   memo.getHexValue(); // e98869bba8bce08c10b78406202127f3888c25454cd37b02600862452751f526
   * </code>
   */
  public String getHexValue() {
    return BaseEncoding.base16().lowerCase().encode(this.bytes);
  }

  @Override
  abstract org.stellar.sdk.xdr.Memo toXdr();

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.bytes);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MemoHashAbstract that = (MemoHashAbstract) o;
    return Arrays.equals(this.bytes, that.bytes);
  }

    @Override
    public String toString() {
        return bytes == null ? "" : getHexValue();
    }
}
