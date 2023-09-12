package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import lombok.NonNull;

abstract class MemoHashAbstract extends Memo {
  protected byte[] bytes;

  public MemoHashAbstract(byte @NonNull [] bytes) {
    checkArgument(bytes.length == 32, "bytes must be 32-bytes long.");
    this.bytes = bytes;
  }

  public MemoHashAbstract(String hexString) {
    // We change to lowercase because we want to decode both: upper cased and lower cased alphabets.
    this(Util.hexToBytes(hexString.toLowerCase()));
  }

  /** Returns 32 bytes long array contained in this memo. */
  public byte[] getBytes() {
    return bytes;
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
