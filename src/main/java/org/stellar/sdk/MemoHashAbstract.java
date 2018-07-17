package org.stellar.sdk;

import com.google.common.base.Objects;
import com.google.common.io.BaseEncoding;

abstract class MemoHashAbstract extends Memo {
  protected byte[] bytes;

  public MemoHashAbstract(byte[] bytes) {
    if (bytes.length < 32) {
      bytes = Util.paddedByteArray(bytes, 32);
    } else if (bytes.length > 32) {
      throw new MemoTooLongException("MEMO_HASH can contain 32 bytes at max.");
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
   *   MemoHash memo = new MemoHash("4142434445");
   *   memo.getHexValue(); // 4142434445000000000000000000000000000000000000000000000000000000
   *   memo.getTrimmedHexValue(); // 4142434445
   * </code>
   */
  public String getHexValue() {
    return BaseEncoding.base16().lowerCase().encode(this.bytes);
  }

  /**
   * <p>Returns hex representation of bytes contained in this memo until null byte (0x00) is found.</p>
   *
   * <p>Example:</p>
   * <code>
   *   MemoHash memo = new MemoHash("4142434445");
   *   memo.getHexValue(); // 4142434445000000000000000000000000000000000000000000000000000000
   *   memo.getTrimmedHexValue(); // 4142434445
   * </code>
   */
  public String getTrimmedHexValue() {
    return this.getHexValue().split("00")[0];
  }

  @Override
  abstract org.stellar.sdk.xdr.Memo toXdr();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MemoHashAbstract that = (MemoHashAbstract) o;
    return Objects.equal(bytes, that.bytes);
  }
}
