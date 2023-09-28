package org.stellar.sdk;

/** Base32 interface used by the SDK to encode and decode strings and bytes. */
public interface Base32 {

  /**
   * Encodes bytes to base32 bytes
   *
   * @param data bytes to encode
   * @return encoded bytes
   */
  byte[] encode(byte[] data);

  /**
   * Decodes base32 string to bytes
   *
   * @param data string to decode
   * @return decoded bytes
   */
  byte[] decode(String data);

  /**
   * Decodes base32 bytes to bytes
   *
   * @param data bytes to decode
   * @return decoded bytes
   */
  byte[] decode(byte[] data);
}
