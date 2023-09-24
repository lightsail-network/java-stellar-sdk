package org.stellar.sdk;

import java.nio.charset.StandardCharsets;

/**
 * Base64 utility class.
 *
 * <p>The java.util.Base64 class is not available in Android API level 25 and below, The minSDK
 * currently used by the Lobstr client is 23, and we need to make it compatible.
 */
public class Base64 {
  // ApacheCodec.Base64 is thread-safe
  private static final ApacheCodec.Base64 base64 =
      new ApacheCodec.Base64(0, null, false, ApacheCodec.CodecPolicy.STRICT);

  /**
   * Encodes bytes to base64 string
   *
   * @param data bytes to encode
   * @return encoded string
   */
  public static String encodeToString(byte[] data) {
    return base64.encodeToString(data);
  }

  /**
   * Encodes bytes to base64 bytes
   *
   * @param data bytes to encode
   * @return encoded bytes
   */
  public static byte[] encode(byte[] data) {
    return base64.encode(data);
  }

  /**
   * Decodes base64 string to bytes
   *
   * @param data string to decode
   * @return decoded bytes
   */
  public static byte[] decode(String data) {
    byte[] raw = data.getBytes(StandardCharsets.UTF_8);
    if (!isInAlphabet(raw)) {
      throw new IllegalArgumentException("Invalid base64 string");
    }
    return base64.decode(raw);
  }

  private static boolean isInAlphabet(final byte[] arrayOctet) {
    for (final byte octet : arrayOctet) {
      // allow padding characters in the base64 string.
      if (!base64.isInAlphabet(octet) && (octet != base64.pad)) {
        return false;
      }
    }
    return true;
  }
}
