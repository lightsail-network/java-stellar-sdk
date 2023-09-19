package org.stellar.sdk.scval;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
final class Longs {
  /**
   * @return a big-endian representation of {@code value} in an 8-element byte array.
   */
  static byte[] toByteArray(long value) {
    byte[] result = new byte[8];
    for (int i = 7; i >= 0; i--) {
      result[i] = (byte) (value & 0xffL);
      value >>= 8;
    }
    return result;
  }

  /**
   * @return the {@code long} value whose byte representation is the given 8 bytes, in big-endian
   *     order.
   */
  static long fromByteArray(byte[] bytes) {
    if (bytes.length != 8) {
      throw new IllegalArgumentException("array length is not 8");
    }
    return (bytes[0] & 0xFFL) << 56
        | (bytes[1] & 0xFFL) << 48
        | (bytes[2] & 0xFFL) << 40
        | (bytes[3] & 0xFFL) << 32
        | (bytes[4] & 0xFFL) << 24
        | (bytes[5] & 0xFFL) << 16
        | (bytes[6] & 0xFFL) << 8
        | (bytes[7] & 0xFFL);
  }
}
