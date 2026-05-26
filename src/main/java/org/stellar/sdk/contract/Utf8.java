package org.stellar.sdk.contract;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

/**
 * Strict UTF-8 decoding shared by the contract introspection parsers.
 *
 * <p>Unlike {@code new String(bytes, UTF_8)}, this rejects malformed or unmappable byte sequences
 * instead of silently substituting replacement characters, so callers can surface invalid contract
 * data as an error.
 */
final class Utf8 {
  private Utf8() {}

  /**
   * Decodes {@code data} as UTF-8.
   *
   * @param data the bytes to decode
   * @return the decoded string
   * @throws CharacterCodingException if the bytes are not valid UTF-8
   */
  static String strictDecode(byte[] data) throws CharacterCodingException {
    return StandardCharsets.UTF_8
        .newDecoder()
        .onMalformedInput(CodingErrorAction.REPORT)
        .onUnmappableCharacter(CodingErrorAction.REPORT)
        .decode(ByteBuffer.wrap(data))
        .toString();
  }
}
