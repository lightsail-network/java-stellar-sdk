package org.stellar.sdk;


import org.apache.commons.android.codec.DecoderException;
import org.apache.commons.android.codec.binary.Hex;

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

  public MemoHashAbstract(String hexString) throws DecoderException {
    this(Hex.decodeHex(hexString.toCharArray()));
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
    return new String(Hex.encodeHex(this.bytes));
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
}
