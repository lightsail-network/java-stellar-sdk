package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(callSuper = false)
abstract class MemoHashAbstract extends Memo {
  /** 32 bytes long array contained in this memo. */
  protected byte @NonNull [] bytes;

  public MemoHashAbstract(byte @NonNull [] bytes) {
    if (bytes.length != 32) {
      throw new IllegalArgumentException("bytes must be 32-bytes long.");
    }
    this.bytes = bytes;
  }

  public MemoHashAbstract(String hexString) {
    // We change to lowercase because we want to decode both: upper-cased and lower cased alphabets.
    this(Util.hexToBytes(hexString.toLowerCase()));
  }

  /**
   * Returns hex representation of bytes contained in this memo.
   *
   * <p>Example: <code>
   *   MemoHash memo = new MemoHash("e98869bba8bce08c10b78406202127f3888c25454cd37b02600862452751f526");
   *   memo.getHexValue(); // e98869bba8bce08c10b78406202127f3888c25454cd37b02600862452751f526
   * </code>
   */
  public String getHexValue() {
    return Util.bytesToHex(this.bytes).toLowerCase();
  }

  @Override
  abstract org.stellar.sdk.xdr.Memo toXdr();

  @Override
  public String toString() {
    return getHexValue();
  }
}
