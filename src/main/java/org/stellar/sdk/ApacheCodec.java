package org.stellar.sdk;

import static org.stellar.sdk.ApacheCodec.BaseNCodec.EOF;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * Copy from commons-codec:commons-codec:1.16.0
 *
 * <p>This is a compromise, because the Android platform built-in with commons-codec:commons-codec:
 * 1.3.0 version in API < 28, which does not include the Base32 module. See <a
 * href="https://github.com/stellar/java-stellar-sdk/issues/542">the issue</a>
 */
class ApacheCodec {
  enum CodecPolicy {

    /** The strict policy. Data that causes a codec to fail should throw an exception. */
    STRICT,

    /** The lenient policy. Data that causes a codec to fail should not throw an exception. */
    LENIENT
  }

  static class Base32 extends BaseNCodec {

    /**
     * BASE32 characters are 5 bits in length. They are formed by taking a block of five octets to
     * form a 40-bit string, which is converted into eight BASE32 characters.
     */
    private static final int BITS_PER_ENCODED_BYTE = 5;

    private static final int BYTES_PER_ENCODED_BLOCK = 8;
    private static final int BYTES_PER_UNENCODED_BLOCK = 5;

    /**
     * This array is a lookup table that translates Unicode characters drawn from the "Base32
     * Alphabet" (as specified in Table 3 of RFC 4648) into their 5-bit positive integer
     * equivalents. Characters that are not in the Base32 alphabet but fall within the bounds of the
     * array are translated to -1.
     */
    private static final byte[] DECODE_TABLE = {
      //  0   1   2   3   4   5   6   7   8   9   A   B   C   D   E   F
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1, // 00-0f
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1, // 10-1f
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1, // 20-2f
      -1,
      -1,
      26,
      27,
      28,
      29,
      30,
      31,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1, // 30-3f 2-7
      -1,
      0,
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9,
      10,
      11,
      12,
      13,
      14, // 40-4f A-O
      15,
      16,
      17,
      18,
      19,
      20,
      21,
      22,
      23,
      24,
      25, // 50-5a P-Z
      -1,
      -1,
      -1,
      -1,
      -1, // 5b-5f
      -1,
      0,
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9,
      10,
      11,
      12,
      13,
      14, // 60-6f a-o
      15,
      16,
      17,
      18,
      19,
      20,
      21,
      22,
      23,
      24,
      25, // 70-7a p-z
    };

    /**
     * This array is a lookup table that translates 5-bit positive integer index values into their
     * "Base32 Alphabet" equivalents as specified in Table 3 of RFC 4648.
     */
    private static final byte[] ENCODE_TABLE = {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
      'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
      '2', '3', '4', '5', '6', '7',
    };

    /**
     * This array is a lookup table that translates Unicode characters drawn from the "Base32 Hex
     * Alphabet" (as specified in Table 4 of RFC 4648) into their 5-bit positive integer
     * equivalents. Characters that are not in the Base32 Hex alphabet but fall within the bounds of
     * the array are translated to -1.
     */
    private static final byte[] HEX_DECODE_TABLE = {
      //  0   1   2   3   4   5   6   7   8   9   A   B   C   D   E   F
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1, // 00-0f
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1, // 10-1f
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1, // 20-2f
      0,
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1, // 30-3f 0-9
      -1,
      10,
      11,
      12,
      13,
      14,
      15,
      16,
      17,
      18,
      19,
      20,
      21,
      22,
      23,
      24, // 40-4f A-O
      25,
      26,
      27,
      28,
      29,
      30,
      31, // 50-56 P-V
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1,
      -1, // 57-5f
      -1,
      10,
      11,
      12,
      13,
      14,
      15,
      16,
      17,
      18,
      19,
      20,
      21,
      22,
      23,
      24, // 60-6f a-o
      25,
      26,
      27,
      28,
      29,
      30,
      31 // 70-76 p-v
    };

    /**
     * This array is a lookup table that translates 5-bit positive integer index values into their
     * "Base32 Hex Alphabet" equivalents as specified in Table 4 of RFC 4648.
     */
    private static final byte[] HEX_ENCODE_TABLE = {
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
      'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
    };

    /** Mask used to extract 5 bits, used when encoding Base32 bytes */
    private static final int MASK_5BITS = 0x1f;

    /** Mask used to extract 4 bits, used when decoding final trailing character. */
    private static final long MASK_4BITS = 0x0fL;

    /** Mask used to extract 3 bits, used when decoding final trailing character. */
    private static final long MASK_3BITS = 0x07L;

    /** Mask used to extract 2 bits, used when decoding final trailing character. */
    private static final long MASK_2BITS = 0x03L;

    /** Mask used to extract 1 bits, used when decoding final trailing character. */
    private static final long MASK_1BITS = 0x01L;

    // The static final fields above are used for the original static byte[] methods on Base32.
    // The private member fields below are used with the new streaming approach, which requires
    // some state be preserved between calls of encode() and decode().

    /**
     * Convenience variable to help us determine when our buffer is going to run out of room and
     * needs resizing. {@code decodeSize = {@link #BYTES_PER_ENCODED_BLOCK} - 1 +
     * lineSeparator.length;}
     */
    private final int decodeSize;

    /** Decode table to use. */
    private final byte[] decodeTable;

    /**
     * Convenience variable to help us determine when our buffer is going to run out of room and
     * needs resizing. {@code encodeSize = {@link #BYTES_PER_ENCODED_BLOCK} + lineSeparator.length;}
     */
    private final int encodeSize;

    /** Encode table to use. */
    private final byte[] encodeTable;

    /** Line separator for encoding. Not used when decoding. Only used if lineLength &gt; 0. */
    private final byte[] lineSeparator;

    /**
     * Creates a Base32 codec used for decoding and encoding.
     *
     * <p>When encoding the line length is 0 (no chunking).
     */
    public Base32() {
      this(false);
    }

    /**
     * Creates a Base32 codec used for decoding and encoding.
     *
     * <p>When encoding the line length is 0 (no chunking).
     *
     * @param useHex if {@code true} then use Base32 Hex alphabet
     */
    public Base32(final boolean useHex) {
      this(0, null, useHex, PAD_DEFAULT);
    }

    /**
     * Creates a Base32 codec used for decoding and encoding.
     *
     * <p>When encoding the line length is 0 (no chunking).
     *
     * @param useHex if {@code true} then use Base32 Hex alphabet
     * @param padding byte used as padding byte.
     */
    public Base32(final boolean useHex, final byte padding) {
      this(0, null, useHex, padding);
    }

    /**
     * Creates a Base32 codec used for decoding and encoding.
     *
     * <p>When encoding the line length is 0 (no chunking).
     *
     * @param pad byte used as padding byte.
     */
    public Base32(final byte pad) {
      this(false, pad);
    }

    /**
     * Creates a Base32 codec used for decoding and encoding.
     *
     * <p>When encoding the line length is given in the constructor, the line separator is CRLF.
     *
     * @param lineLength Each line of encoded data will be at most of the given length (rounded down
     *     to the nearest multiple of 8). If lineLength &lt;= 0, then the output will not be divided
     *     into lines (chunks). Ignored when decoding.
     */
    public Base32(final int lineLength) {
      this(lineLength, CHUNK_SEPARATOR);
    }

    /**
     * Creates a Base32 codec used for decoding and encoding.
     *
     * <p>When encoding the line length and line separator are given in the constructor.
     *
     * <p>Line lengths that aren't multiples of 8 will still essentially end up being multiples of 8
     * in the encoded data.
     *
     * @param lineLength Each line of encoded data will be at most of the given length (rounded down
     *     to the nearest multiple of 8). If lineLength &lt;= 0, then the output will not be divided
     *     into lines (chunks). Ignored when decoding.
     * @param lineSeparator Each line of encoded data will end with this sequence of bytes.
     * @throws IllegalArgumentException Thrown when the {@code lineSeparator} contains Base32
     *     characters.
     */
    public Base32(final int lineLength, final byte[] lineSeparator) {
      this(lineLength, lineSeparator, false, PAD_DEFAULT);
    }

    /**
     * Creates a Base32 / Base32 Hex codec used for decoding and encoding.
     *
     * <p>When encoding the line length and line separator are given in the constructor.
     *
     * <p>Line lengths that aren't multiples of 8 will still essentially end up being multiples of 8
     * in the encoded data.
     *
     * @param lineLength Each line of encoded data will be at most of the given length (rounded down
     *     to the nearest multiple of 8). If lineLength &lt;= 0, then the output will not be divided
     *     into lines (chunks). Ignored when decoding.
     * @param lineSeparator Each line of encoded data will end with this sequence of bytes.
     * @param useHex if {@code true}, then use Base32 Hex alphabet, otherwise use Base32 alphabet
     * @throws IllegalArgumentException Thrown when the {@code lineSeparator} contains Base32
     *     characters. Or the lineLength &gt; 0 and lineSeparator is null.
     */
    public Base32(final int lineLength, final byte[] lineSeparator, final boolean useHex) {
      this(lineLength, lineSeparator, useHex, PAD_DEFAULT);
    }

    /**
     * Creates a Base32 / Base32 Hex codec used for decoding and encoding.
     *
     * <p>When encoding the line length and line separator are given in the constructor.
     *
     * <p>Line lengths that aren't multiples of 8 will still essentially end up being multiples of 8
     * in the encoded data.
     *
     * @param lineLength Each line of encoded data will be at most of the given length (rounded down
     *     to the nearest multiple of 8). If lineLength &lt;= 0, then the output will not be divided
     *     into lines (chunks). Ignored when decoding.
     * @param lineSeparator Each line of encoded data will end with this sequence of bytes.
     * @param useHex if {@code true}, then use Base32 Hex alphabet, otherwise use Base32 alphabet
     * @param padding byte used as padding byte.
     * @throws IllegalArgumentException Thrown when the {@code lineSeparator} contains Base32
     *     characters. Or the lineLength &gt; 0 and lineSeparator is null.
     */
    public Base32(
        final int lineLength,
        final byte[] lineSeparator,
        final boolean useHex,
        final byte padding) {
      this(lineLength, lineSeparator, useHex, padding, DECODING_POLICY_DEFAULT);
    }

    /**
     * Creates a Base32 / Base32 Hex codec used for decoding and encoding.
     *
     * <p>When encoding the line length and line separator are given in the constructor.
     *
     * <p>Line lengths that aren't multiples of 8 will still essentially end up being multiples of 8
     * in the encoded data.
     *
     * @param lineLength Each line of encoded data will be at most of the given length (rounded down
     *     to the nearest multiple of 8). If lineLength &lt;= 0, then the output will not be divided
     *     into lines (chunks). Ignored when decoding.
     * @param lineSeparator Each line of encoded data will end with this sequence of bytes.
     * @param useHex if {@code true}, then use Base32 Hex alphabet, otherwise use Base32 alphabet
     * @param padding byte used as padding byte.
     * @param decodingPolicy The decoding policy.
     * @throws IllegalArgumentException Thrown when the {@code lineSeparator} contains Base32
     *     characters. Or the lineLength &gt; 0 and lineSeparator is null.
     * @since 1.15
     */
    public Base32(
        final int lineLength,
        final byte[] lineSeparator,
        final boolean useHex,
        final byte padding,
        final CodecPolicy decodingPolicy) {
      super(
          BYTES_PER_UNENCODED_BLOCK,
          BYTES_PER_ENCODED_BLOCK,
          lineLength,
          lineSeparator == null ? 0 : lineSeparator.length,
          padding,
          decodingPolicy);
      if (useHex) {
        this.encodeTable = HEX_ENCODE_TABLE;
        this.decodeTable = HEX_DECODE_TABLE;
      } else {
        this.encodeTable = ENCODE_TABLE;
        this.decodeTable = DECODE_TABLE;
      }
      if (lineLength > 0) {
        if (lineSeparator == null) {
          throw new IllegalArgumentException(
              "lineLength " + lineLength + " > 0, but lineSeparator is null");
        }
        // Must be done after initializing the tables
        if (containsAlphabetOrPad(lineSeparator)) {
          final String sep = StringUtils.newStringUtf8(lineSeparator);
          throw new IllegalArgumentException(
              "lineSeparator must not contain Base32 characters: [" + sep + "]");
        }
        this.encodeSize = BYTES_PER_ENCODED_BLOCK + lineSeparator.length;
        this.lineSeparator = lineSeparator.clone();
      } else {
        this.encodeSize = BYTES_PER_ENCODED_BLOCK;
        this.lineSeparator = null;
      }
      this.decodeSize = this.encodeSize - 1;

      if (isInAlphabet(padding) || Character.isWhitespace(padding)) {
        throw new IllegalArgumentException("pad must not be in alphabet or whitespace");
      }
    }

    /**
     * Decodes all of the provided data, starting at inPos, for inAvail bytes. Should be called at
     * least twice: once with the data to decode, and once with inAvail set to "-1" to alert decoder
     * that EOF has been reached. The "-1" call is not necessary when decoding, but it doesn't hurt,
     * either.
     *
     * <p>Ignores all non-Base32 characters. This is how chunked (e.g. 76 character) data is
     * handled, since CR and LF are silently ignored, but has implications for other bytes, too.
     * This method subscribes to the garbage-in, garbage-out philosophy: it will not check the
     * provided data for validity.
     *
     * @param input byte[] array of ASCII data to Base32 decode.
     * @param inPos Position to start reading data from.
     * @param inAvail Amount of bytes available from input for decoding.
     * @param context the context to be used
     */
    @Override
    void decode(final byte[] input, int inPos, final int inAvail, final Context context) {
      // package protected for access from I/O streams

      if (context.eof) {
        return;
      }
      if (inAvail < 0) {
        context.eof = true;
      }
      for (int i = 0; i < inAvail; i++) {
        final byte b = input[inPos++];
        if (b == pad) {
          // We're done.
          context.eof = true;
          break;
        }
        final byte[] buffer = ensureBufferSize(decodeSize, context);
        if (b >= 0 && b < this.decodeTable.length) {
          final int result = this.decodeTable[b];
          if (result >= 0) {
            context.modulus = (context.modulus + 1) % BYTES_PER_ENCODED_BLOCK;
            // collect decoded bytes
            context.lbitWorkArea = (context.lbitWorkArea << BITS_PER_ENCODED_BYTE) + result;
            if (context.modulus == 0) { // we can output the 5 bytes
              buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 32) & MASK_8BITS);
              buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 24) & MASK_8BITS);
              buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 16) & MASK_8BITS);
              buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 8) & MASK_8BITS);
              buffer[context.pos++] = (byte) (context.lbitWorkArea & MASK_8BITS);
            }
          }
        }
      }

      // Two forms of EOF as far as Base32 decoder is concerned: actual
      // EOF (-1) and first time '=' character is encountered in stream.
      // This approach makes the '=' padding characters completely optional.
      if (context.eof && context.modulus > 0) { // if modulus == 0, nothing to do
        final byte[] buffer = ensureBufferSize(decodeSize, context);

        // We ignore partial bytes, i.e. only multiples of 8 count.
        // Any combination not part of a valid encoding is either partially decoded
        // or will raise an exception. Possible trailing characters are 2, 4, 5, 7.
        // It is not possible to encode with 1, 3, 6 trailing characters.
        // For backwards compatibility 3 & 6 chars are decoded anyway rather than discarded.
        // See the encode(byte[]) method EOF section.
        switch (context.modulus) {
            //              case 0 : // impossible, as excluded above
          case 1: // 5 bits - either ignore entirely, or raise an exception
            validateTrailingCharacters();
          case 2: // 10 bits, drop 2 and output one byte
            validateCharacter(MASK_2BITS, context);
            buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 2) & MASK_8BITS);
            break;
          case 3: // 15 bits, drop 7 and output 1 byte, or raise an exception
            validateTrailingCharacters();
            // Not possible from a valid encoding but decode anyway
            buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 7) & MASK_8BITS);
            break;
          case 4: // 20 bits = 2*8 + 4
            validateCharacter(MASK_4BITS, context);
            context.lbitWorkArea = context.lbitWorkArea >> 4; // drop 4 bits
            buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 8) & MASK_8BITS);
            buffer[context.pos++] = (byte) ((context.lbitWorkArea) & MASK_8BITS);
            break;
          case 5: // 25 bits = 3*8 + 1
            validateCharacter(MASK_1BITS, context);
            context.lbitWorkArea = context.lbitWorkArea >> 1;
            buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 16) & MASK_8BITS);
            buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 8) & MASK_8BITS);
            buffer[context.pos++] = (byte) ((context.lbitWorkArea) & MASK_8BITS);
            break;
          case 6: // 30 bits = 3*8 + 6, or raise an exception
            validateTrailingCharacters();
            // Not possible from a valid encoding but decode anyway
            context.lbitWorkArea = context.lbitWorkArea >> 6;
            buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 16) & MASK_8BITS);
            buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 8) & MASK_8BITS);
            buffer[context.pos++] = (byte) ((context.lbitWorkArea) & MASK_8BITS);
            break;
          case 7: // 35 bits = 4*8 +3
            validateCharacter(MASK_3BITS, context);
            context.lbitWorkArea = context.lbitWorkArea >> 3;
            buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 24) & MASK_8BITS);
            buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 16) & MASK_8BITS);
            buffer[context.pos++] = (byte) ((context.lbitWorkArea >> 8) & MASK_8BITS);
            buffer[context.pos++] = (byte) ((context.lbitWorkArea) & MASK_8BITS);
            break;
          default:
            // modulus can be 0-7, and we excluded 0,1 already
            throw new IllegalStateException("Impossible modulus " + context.modulus);
        }
      }
    }

    /**
     * Encodes all of the provided data, starting at inPos, for inAvail bytes. Must be called at
     * least twice: once with the data to encode, and once with inAvail set to "-1" to alert encoder
     * that EOF has been reached, so flush last remaining bytes (if not multiple of 5).
     *
     * @param input byte[] array of binary data to Base32 encode.
     * @param inPos Position to start reading data from.
     * @param inAvail Amount of bytes available from input for encoding.
     * @param context the context to be used
     */
    @Override
    void encode(final byte[] input, int inPos, final int inAvail, final Context context) {
      // package protected for access from I/O streams

      if (context.eof) {
        return;
      }
      // inAvail < 0 is how we're informed of EOF in the underlying data we're
      // encoding.
      if (inAvail < 0) {
        context.eof = true;
        if (0 == context.modulus && lineLength == 0) {
          return; // no leftovers to process and not using chunking
        }
        final byte[] buffer = ensureBufferSize(encodeSize, context);
        final int savedPos = context.pos;
        switch (context.modulus) { // % 5
          case 0:
            break;
          case 1: // Only 1 octet; take top 5 bits then remainder
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 3) & MASK_5BITS]; // 8-1*5 = 3
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea << 2) & MASK_5BITS]; // 5-3=2
            buffer[context.pos++] = pad;
            buffer[context.pos++] = pad;
            buffer[context.pos++] = pad;
            buffer[context.pos++] = pad;
            buffer[context.pos++] = pad;
            buffer[context.pos++] = pad;
            break;
          case 2: // 2 octets = 16 bits to use
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 11) & MASK_5BITS]; // 16-1*5 = 11
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 6) & MASK_5BITS]; // 16-2*5 = 6
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 1) & MASK_5BITS]; // 16-3*5 = 1
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea << 4) & MASK_5BITS]; // 5-1 = 4
            buffer[context.pos++] = pad;
            buffer[context.pos++] = pad;
            buffer[context.pos++] = pad;
            buffer[context.pos++] = pad;
            break;
          case 3: // 3 octets = 24 bits to use
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 19) & MASK_5BITS]; // 24-1*5 = 19
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 14) & MASK_5BITS]; // 24-2*5 = 14
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 9) & MASK_5BITS]; // 24-3*5 = 9
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 4) & MASK_5BITS]; // 24-4*5 = 4
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea << 1) & MASK_5BITS]; // 5-4 = 1
            buffer[context.pos++] = pad;
            buffer[context.pos++] = pad;
            buffer[context.pos++] = pad;
            break;
          case 4: // 4 octets = 32 bits to use
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 27) & MASK_5BITS]; // 32-1*5 = 27
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 22) & MASK_5BITS]; // 32-2*5 = 22
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 17) & MASK_5BITS]; // 32-3*5 = 17
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 12) & MASK_5BITS]; // 32-4*5 = 12
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 7) & MASK_5BITS]; // 32-5*5 =  7
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea >> 2) & MASK_5BITS]; // 32-6*5 =  2
            buffer[context.pos++] =
                encodeTable[(int) (context.lbitWorkArea << 3) & MASK_5BITS]; // 5-2 = 3
            buffer[context.pos++] = pad;
            break;
          default:
            throw new IllegalStateException("Impossible modulus " + context.modulus);
        }
        context.currentLinePos += context.pos - savedPos; // keep track of current line position
        // if currentPos == 0 we are at the start of a line, so don't add CRLF
        if (lineLength > 0 && context.currentLinePos > 0) { // add chunk separator if required
          System.arraycopy(lineSeparator, 0, buffer, context.pos, lineSeparator.length);
          context.pos += lineSeparator.length;
        }
      } else {
        for (int i = 0; i < inAvail; i++) {
          final byte[] buffer = ensureBufferSize(encodeSize, context);
          context.modulus = (context.modulus + 1) % BYTES_PER_UNENCODED_BLOCK;
          int b = input[inPos++];
          if (b < 0) {
            b += 256;
          }
          context.lbitWorkArea = (context.lbitWorkArea << 8) + b; // BITS_PER_BYTE
          if (0 == context.modulus) { // we have enough bytes to create our output
            buffer[context.pos++] = encodeTable[(int) (context.lbitWorkArea >> 35) & MASK_5BITS];
            buffer[context.pos++] = encodeTable[(int) (context.lbitWorkArea >> 30) & MASK_5BITS];
            buffer[context.pos++] = encodeTable[(int) (context.lbitWorkArea >> 25) & MASK_5BITS];
            buffer[context.pos++] = encodeTable[(int) (context.lbitWorkArea >> 20) & MASK_5BITS];
            buffer[context.pos++] = encodeTable[(int) (context.lbitWorkArea >> 15) & MASK_5BITS];
            buffer[context.pos++] = encodeTable[(int) (context.lbitWorkArea >> 10) & MASK_5BITS];
            buffer[context.pos++] = encodeTable[(int) (context.lbitWorkArea >> 5) & MASK_5BITS];
            buffer[context.pos++] = encodeTable[(int) context.lbitWorkArea & MASK_5BITS];
            context.currentLinePos += BYTES_PER_ENCODED_BLOCK;
            if (lineLength > 0 && lineLength <= context.currentLinePos) {
              System.arraycopy(lineSeparator, 0, buffer, context.pos, lineSeparator.length);
              context.pos += lineSeparator.length;
              context.currentLinePos = 0;
            }
          }
        }
      }
    }

    /**
     * Returns whether or not the {@code octet} is in the Base32 alphabet.
     *
     * @param octet The value to test
     * @return {@code true} if the value is defined in the Base32 alphabet {@code false} otherwise.
     */
    @Override
    public boolean isInAlphabet(final byte octet) {
      return octet >= 0 && octet < decodeTable.length && decodeTable[octet] != -1;
    }

    /**
     * Validates whether decoding the final trailing character is possible in the context of the set
     * of possible base 32 values.
     *
     * <p>The character is valid if the lower bits within the provided mask are zero. This is used
     * to test the final trailing base-32 digit is zero in the bits that will be discarded.
     *
     * @param emptyBitsMask The mask of the lower bits that should be empty
     * @param context the context to be used
     * @throws IllegalArgumentException if the bits being checked contain any non-zero value
     */
    private void validateCharacter(final long emptyBitsMask, final Context context) {
      // Use the long bit work area
      if (isStrictDecoding() && (context.lbitWorkArea & emptyBitsMask) != 0) {
        throw new IllegalArgumentException(
            "Strict decoding: Last encoded character (before the paddings if any) is a valid "
                + "base 32 alphabet but not a possible encoding. "
                + "Expected the discarded bits from the character to be zero.");
      }
    }

    /**
     * Validates whether decoding allows final trailing characters that cannot be created during
     * encoding.
     *
     * @throws IllegalArgumentException if strict decoding is enabled
     */
    private void validateTrailingCharacters() {
      if (isStrictDecoding()) {
        throw new IllegalArgumentException(
            "Strict decoding: Last encoded character(s) (before the paddings if any) are valid "
                + "base 32 alphabet but not a possible encoding. "
                + "Decoding requires either 2, 4, 5, or 7 trailing 5-bit characters to create bytes.");
      }
    }
  }

  static class BinaryCodec {
    /*
     * tried to avoid using ArrayUtils to minimize dependencies while using these empty arrays - dep is just not worth
     * it.
     */
    /** Empty char array. */
    private static final char[] EMPTY_CHAR_ARRAY = {};

    /** Empty byte array. */
    private static final byte[] EMPTY_BYTE_ARRAY = {};

    /** Mask for bit 0 of a byte. */
    private static final int BIT_0 = 1;

    /** Mask for bit 1 of a byte. */
    private static final int BIT_1 = 0x02;

    /** Mask for bit 2 of a byte. */
    private static final int BIT_2 = 0x04;

    /** Mask for bit 3 of a byte. */
    private static final int BIT_3 = 0x08;

    /** Mask for bit 4 of a byte. */
    private static final int BIT_4 = 0x10;

    /** Mask for bit 5 of a byte. */
    private static final int BIT_5 = 0x20;

    /** Mask for bit 6 of a byte. */
    private static final int BIT_6 = 0x40;

    /** Mask for bit 7 of a byte. */
    private static final int BIT_7 = 0x80;

    private static final int[] BITS = {BIT_0, BIT_1, BIT_2, BIT_3, BIT_4, BIT_5, BIT_6, BIT_7};

    /**
     * Decodes a byte array where each byte represents an ASCII '0' or '1'.
     *
     * @param ascii each byte represents an ASCII '0' or '1'
     * @return the raw encoded binary where each bit corresponds to a byte in the byte array
     *     argument
     */
    public static byte[] fromAscii(final byte[] ascii) {
      if (isEmpty(ascii)) {
        return EMPTY_BYTE_ARRAY;
      }
      final int asciiLength = ascii.length;
      // get length/8 times bytes with 3 bit shifts to the right of the length
      final byte[] raw = new byte[asciiLength >> 3];
      /*
       * We decr index jj by 8 as we go along to not recompute indices using multiplication every time inside the
       * loop.
       */
      for (int ii = 0, jj = asciiLength - 1; ii < raw.length; ii++, jj -= 8) {
        for (int bits = 0; bits < BITS.length; ++bits) {
          if (ascii[jj - bits] == '1') {
            raw[ii] |= BITS[bits];
          }
        }
      }
      return raw;
    }

    // ------------------------------------------------------------------------
    //
    // static codec operations
    //
    // ------------------------------------------------------------------------

    /**
     * Decodes a char array where each char represents an ASCII '0' or '1'.
     *
     * @param ascii each char represents an ASCII '0' or '1'
     * @return the raw encoded binary where each bit corresponds to a char in the char array
     *     argument
     */
    public static byte[] fromAscii(final char[] ascii) {
      if (ascii == null || ascii.length == 0) {
        return EMPTY_BYTE_ARRAY;
      }
      final int asciiLength = ascii.length;
      // get length/8 times bytes with 3 bit shifts to the right of the length
      final byte[] raw = new byte[asciiLength >> 3];
      /*
       * We decr index jj by 8 as we go along to not recompute indices using multiplication every time inside the
       * loop.
       */
      for (int ii = 0, jj = asciiLength - 1; ii < raw.length; ii++, jj -= 8) {
        for (int bits = 0; bits < BITS.length; ++bits) {
          if (ascii[jj - bits] == '1') {
            raw[ii] |= BITS[bits];
          }
        }
      }
      return raw;
    }

    /**
     * Returns {@code true} if the given array is {@code null} or empty (size 0.)
     *
     * @param array the source array
     * @return {@code true} if the given array is {@code null} or empty (size 0.)
     */
    static boolean isEmpty(final byte[] array) {
      return array == null || array.length == 0;
    }

    /**
     * Converts an array of raw binary data into an array of ASCII 0 and 1 character bytes - each
     * byte is a truncated char.
     *
     * @param raw the raw binary data to convert
     * @return an array of 0 and 1 character bytes for each bit of the argument
     */
    public static byte[] toAsciiBytes(final byte[] raw) {
      if (isEmpty(raw)) {
        return EMPTY_BYTE_ARRAY;
      }
      final int rawLength = raw.length;
      // get 8 times the bytes with 3 bit shifts to the left of the length
      final byte[] l_ascii = new byte[rawLength << 3];
      /*
       * We decr index jj by 8 as we go along to not recompute indices using multiplication every time inside the
       * loop.
       */
      for (int ii = 0, jj = l_ascii.length - 1; ii < rawLength; ii++, jj -= 8) {
        for (int bits = 0; bits < BITS.length; ++bits) {
          if ((raw[ii] & BITS[bits]) == 0) {
            l_ascii[jj - bits] = '0';
          } else {
            l_ascii[jj - bits] = '1';
          }
        }
      }
      return l_ascii;
    }

    /**
     * Converts an array of raw binary data into an array of ASCII 0 and 1 characters.
     *
     * @param raw the raw binary data to convert
     * @return an array of 0 and 1 characters for each bit of the argument
     */
    public static char[] toAsciiChars(final byte[] raw) {
      if (isEmpty(raw)) {
        return EMPTY_CHAR_ARRAY;
      }
      final int rawLength = raw.length;
      // get 8 times the bytes with 3 bit shifts to the left of the length
      final char[] l_ascii = new char[rawLength << 3];
      /*
       * We decr index jj by 8 as we go along to not recompute indices using multiplication every time inside the
       * loop.
       */
      for (int ii = 0, jj = l_ascii.length - 1; ii < rawLength; ii++, jj -= 8) {
        for (int bits = 0; bits < BITS.length; ++bits) {
          if ((raw[ii] & BITS[bits]) == 0) {
            l_ascii[jj - bits] = '0';
          } else {
            l_ascii[jj - bits] = '1';
          }
        }
      }
      return l_ascii;
    }

    /**
     * Converts an array of raw binary data into a String of ASCII 0 and 1 characters.
     *
     * @param raw the raw binary data to convert
     * @return a String of 0 and 1 characters representing the binary data
     */
    public static String toAsciiString(final byte[] raw) {
      return new String(toAsciiChars(raw));
    }

    /**
     * Decodes a byte array where each byte represents an ASCII '0' or '1'.
     *
     * @param ascii each byte represents an ASCII '0' or '1'
     * @return the raw encoded binary where each bit corresponds to a byte in the byte array
     *     argument
     */
    public byte[] decode(final byte[] ascii) {
      return fromAscii(ascii);
    }

    /**
     * Decodes a byte array where each byte represents an ASCII '0' or '1'.
     *
     * @param ascii each byte represents an ASCII '0' or '1'
     * @return the raw encoded binary where each bit corresponds to a byte in the byte array
     *     argument
     * @throws DecoderException if argument is not a byte[], char[] or String
     */
    public Object decode(final Object ascii) throws DecoderException {
      if (ascii == null) {
        return EMPTY_BYTE_ARRAY;
      }
      if (ascii instanceof byte[]) {
        return fromAscii((byte[]) ascii);
      }
      if (ascii instanceof char[]) {
        return fromAscii((char[]) ascii);
      }
      if (ascii instanceof String) {
        return fromAscii(((String) ascii).toCharArray());
      }
      throw new DecoderException("argument not a byte array");
    }

    /**
     * Converts an array of raw binary data into an array of ASCII 0 and 1 characters.
     *
     * @param raw the raw binary data to convert
     * @return 0 and 1 ASCII character bytes one for each bit of the argument
     */
    public byte[] encode(final byte[] raw) {
      return toAsciiBytes(raw);
    }

    /**
     * Converts an array of raw binary data into an array of ASCII 0 and 1 chars.
     *
     * @param raw the raw binary data to convert
     * @return 0 and 1 ASCII character chars one for each bit of the argument
     * @throws EncoderException if the argument is not a byte[]
     */
    public Object encode(final Object raw) throws EncoderException {
      if (!(raw instanceof byte[])) {
        throw new EncoderException("argument not a byte array");
      }
      return toAsciiChars((byte[]) raw);
    }

    /**
     * Decodes a String where each char of the String represents an ASCII '0' or '1'.
     *
     * @param ascii String of '0' and '1' characters
     * @return the raw encoded binary where each bit corresponds to a byte in the byte array
     *     argument
     */
    public byte[] toByteArray(final String ascii) {
      if (ascii == null) {
        return EMPTY_BYTE_ARRAY;
      }
      return fromAscii(ascii.toCharArray());
    }
  }

  abstract static class BaseNCodec {

    /**
     * MIME chunk size per RFC 2045 section 6.8.
     *
     * <p>The {@value} character limit does not count the trailing CRLF, but counts all other
     * characters, including any equal signs.
     *
     * @see <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045 section 6.8</a>
     */
    public static final int MIME_CHUNK_SIZE = 76;

    /**
     * PEM chunk size per RFC 1421 section 4.3.2.4.
     *
     * <p>The {@value} character limit does not count the trailing CRLF, but counts all other
     * characters, including any equal signs.
     *
     * @see <a href="http://tools.ietf.org/html/rfc1421">RFC 1421 section 4.3.2.4</a>
     */
    public static final int PEM_CHUNK_SIZE = 64;

    /** Mask used to extract 8 bits, used in decoding bytes */
    protected static final int MASK_8BITS = 0xff;

    /** Byte used to pad output. */
    protected static final byte PAD_DEFAULT = '='; // Allow static access to default

    /**
     * The default decoding policy.
     *
     * @since 1.15
     */
    protected static final CodecPolicy DECODING_POLICY_DEFAULT = CodecPolicy.LENIENT;

    /**
     * EOF
     *
     * @since 1.7
     */
    static final int EOF = -1;

    /**
     * Chunk separator per RFC 2045 section 2.1.
     *
     * @see <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045 section 2.1</a>
     */
    static final byte[] CHUNK_SEPARATOR = {'\r', '\n'};

    private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;

    /**
     * Defines the default buffer size - currently {@value} - must be large enough for at least one
     * encoded block+separator
     */
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * The maximum size buffer to allocate.
     *
     * <p>This is set to the same size used in the JDK {@code java.util.ArrayList}:
     *
     * <blockquote>
     *
     * Some VMs reserve some header words in an array. Attempts to allocate larger arrays may result
     * in OutOfMemoryError: Requested array size exceeds VM limit.
     *
     * </blockquote>
     */
    private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;

    /**
     * @deprecated Use {@link #pad}. Will be removed in 2.0.
     */
    @Deprecated
    protected final byte PAD = PAD_DEFAULT; // instance variable just in case it needs to vary later

    /** Pad byte. Instance variable just in case it needs to vary later. */
    protected final byte pad;

    /**
     * Chunksize for encoding. Not used when decoding. A value of zero or less implies no chunking
     * of the encoded data. Rounded down to the nearest multiple of encodedBlockSize.
     */
    protected final int lineLength;

    /** Number of bytes in each full block of unencoded data, e.g. 4 for Base64 and 5 for Base32 */
    private final int unencodedBlockSize;

    /** Number of bytes in each full block of encoded data, e.g. 3 for Base64 and 8 for Base32 */
    private final int encodedBlockSize;

    /** Size of chunk separator. Not used unless {@link #lineLength} &gt; 0. */
    private final int chunkSeparatorLength;

    /**
     * Defines the decoding behavior when the input bytes contain leftover trailing bits that cannot
     * be created by a valid encoding. These can be bits that are unused from the final character or
     * entire characters. The default mode is lenient decoding. Set this to {@code true} to enable
     * strict decoding.
     *
     * <ul>
     *   <li>Lenient: Any trailing bits are composed into 8-bit bytes where possible. The remainder
     *       are discarded.
     *   <li>Strict: The decoding will raise an {@link IllegalArgumentException} if trailing bits
     *       are not part of a valid encoding. Any unused bits from the final character must be
     *       zero. Impossible counts of entire final characters are not allowed.
     * </ul>
     *
     * <p>When strict decoding is enabled it is expected that the decoded bytes will be re-encoded
     * to a byte array that matches the original, i.e. no changes occur on the final character. This
     * requires that the input bytes use the same padding and alphabet as the encoder.
     */
    private final CodecPolicy decodingPolicy;

    /**
     * Note {@code lineLength} is rounded down to the nearest multiple of the encoded block size. If
     * {@code chunkSeparatorLength} is zero, then chunking is disabled.
     *
     * @param unencodedBlockSize the size of an unencoded block (e.g. Base64 = 3)
     * @param encodedBlockSize the size of an encoded block (e.g. Base64 = 4)
     * @param lineLength if &gt; 0, use chunking with a length {@code lineLength}
     * @param chunkSeparatorLength the chunk separator length, if relevant
     */
    protected BaseNCodec(
        final int unencodedBlockSize,
        final int encodedBlockSize,
        final int lineLength,
        final int chunkSeparatorLength) {
      this(unencodedBlockSize, encodedBlockSize, lineLength, chunkSeparatorLength, PAD_DEFAULT);
    }

    /**
     * Note {@code lineLength} is rounded down to the nearest multiple of the encoded block size. If
     * {@code chunkSeparatorLength} is zero, then chunking is disabled.
     *
     * @param unencodedBlockSize the size of an unencoded block (e.g. Base64 = 3)
     * @param encodedBlockSize the size of an encoded block (e.g. Base64 = 4)
     * @param lineLength if &gt; 0, use chunking with a length {@code lineLength}
     * @param chunkSeparatorLength the chunk separator length, if relevant
     * @param pad byte used as padding byte.
     */
    protected BaseNCodec(
        final int unencodedBlockSize,
        final int encodedBlockSize,
        final int lineLength,
        final int chunkSeparatorLength,
        final byte pad) {
      this(
          unencodedBlockSize,
          encodedBlockSize,
          lineLength,
          chunkSeparatorLength,
          pad,
          DECODING_POLICY_DEFAULT);
    }

    /**
     * Note {@code lineLength} is rounded down to the nearest multiple of the encoded block size. If
     * {@code chunkSeparatorLength} is zero, then chunking is disabled.
     *
     * @param unencodedBlockSize the size of an unencoded block (e.g. Base64 = 3)
     * @param encodedBlockSize the size of an encoded block (e.g. Base64 = 4)
     * @param lineLength if &gt; 0, use chunking with a length {@code lineLength}
     * @param chunkSeparatorLength the chunk separator length, if relevant
     * @param pad byte used as padding byte.
     * @param decodingPolicy Decoding policy.
     * @since 1.15
     */
    protected BaseNCodec(
        final int unencodedBlockSize,
        final int encodedBlockSize,
        final int lineLength,
        final int chunkSeparatorLength,
        final byte pad,
        final CodecPolicy decodingPolicy) {
      this.unencodedBlockSize = unencodedBlockSize;
      this.encodedBlockSize = encodedBlockSize;
      final boolean useChunking = lineLength > 0 && chunkSeparatorLength > 0;
      this.lineLength = useChunking ? (lineLength / encodedBlockSize) * encodedBlockSize : 0;
      this.chunkSeparatorLength = chunkSeparatorLength;
      this.pad = pad;
      this.decodingPolicy = Objects.requireNonNull(decodingPolicy, "codecPolicy");
    }

    /**
     * Create a positive capacity at least as large the minimum required capacity. If the minimum
     * capacity is negative then this throws an OutOfMemoryError as no array can be allocated.
     *
     * @param minCapacity the minimum capacity
     * @return the capacity
     * @throws OutOfMemoryError if the {@code minCapacity} is negative
     */
    private static int createPositiveCapacity(final int minCapacity) {
      if (minCapacity < 0) {
        // overflow
        throw new OutOfMemoryError("Unable to allocate array size: " + (minCapacity & 0xffffffffL));
      }
      // This is called when we require buffer expansion to a very big array.
      // Use the conservative maximum buffer size if possible, otherwise the biggest required.
      //
      // Note: In this situation JDK 1.8 java.util.ArrayList returns Integer.MAX_VALUE.
      // This excludes some VMs that can exceed MAX_BUFFER_SIZE but not allocate a full
      // Integer.MAX_VALUE length array.
      // The result is that we may have to allocate an array of this size more than once if
      // the capacity must be expanded again.
      return Math.max(minCapacity, MAX_BUFFER_SIZE);
    }

    /**
     * Gets a copy of the chunk separator per RFC 2045 section 2.1.
     *
     * @return the chunk separator
     * @see <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045 section 2.1</a>
     * @since 1.15
     */
    public static byte[] getChunkSeparator() {
      return CHUNK_SEPARATOR.clone();
    }

    /**
     * Checks if a byte value is whitespace or not.
     *
     * @param byteToCheck the byte to check
     * @return true if byte is whitespace, false otherwise
     * @see Character#isWhitespace(int)
     * @deprecated Use {@link Character#isWhitespace(int)}.
     */
    @Deprecated
    protected static boolean isWhiteSpace(final byte byteToCheck) {
      return Character.isWhitespace(byteToCheck);
    }

    /**
     * Increases our buffer by the {@link #DEFAULT_BUFFER_RESIZE_FACTOR}.
     *
     * @param context the context to be used
     * @param minCapacity the minimum required capacity
     * @return the resized byte[] buffer
     * @throws OutOfMemoryError if the {@code minCapacity} is negative
     */
    private static byte[] resizeBuffer(final Context context, final int minCapacity) {
      // Overflow-conscious code treats the min and new capacity as unsigned.
      final int oldCapacity = context.buffer.length;
      int newCapacity = oldCapacity * DEFAULT_BUFFER_RESIZE_FACTOR;
      if (Integer.compareUnsigned(newCapacity, minCapacity) < 0) {
        newCapacity = minCapacity;
      }
      if (Integer.compareUnsigned(newCapacity, MAX_BUFFER_SIZE) > 0) {
        newCapacity = createPositiveCapacity(minCapacity);
      }

      final byte[] b = Arrays.copyOf(context.buffer, newCapacity);
      context.buffer = b;
      return b;
    }

    /**
     * Returns the amount of buffered data available for reading.
     *
     * @param context the context to be used
     * @return The amount of buffered data available for reading.
     */
    int available(final Context context) { // package protected for access from I/O streams
      return hasData(context) ? context.pos - context.readPos : 0;
    }

    /**
     * Tests a given byte array to see if it contains any characters within the alphabet or PAD.
     *
     * <p>Intended for use in checking line-ending arrays
     *
     * @param arrayOctet byte array to test
     * @return {@code true} if any byte is a valid character in the alphabet or PAD; {@code false}
     *     otherwise
     */
    protected boolean containsAlphabetOrPad(final byte[] arrayOctet) {
      if (arrayOctet == null) {
        return false;
      }
      for (final byte element : arrayOctet) {
        if (pad == element || isInAlphabet(element)) {
          return true;
        }
      }
      return false;
    }

    /**
     * Decodes a byte[] containing characters in the Base-N alphabet.
     *
     * @param pArray A byte array containing Base-N character data
     * @return a byte array containing binary data
     */
    public byte[] decode(final byte[] pArray) {
      if (BinaryCodec.isEmpty(pArray)) {
        return pArray;
      }
      final Context context = new Context();
      decode(pArray, 0, pArray.length, context);
      decode(pArray, 0, EOF, context); // Notify decoder of EOF.
      final byte[] result = new byte[context.pos];
      readResults(result, 0, result.length, context);
      return result;
    }

    // package protected for access from I/O streams
    abstract void decode(byte[] pArray, int i, int length, Context context);

    /**
     * Decodes an Object using the Base-N algorithm. This method is provided in order to satisfy the
     * requirements of the Decoder interface, and will throw a DecoderException if the supplied
     * object is not of type byte[] or String.
     *
     * @param obj Object to decode
     * @return An object (of type byte[]) containing the binary data which corresponds to the byte[]
     *     or String supplied.
     * @throws DecoderException if the parameter supplied is not of type byte[]
     */
    public Object decode(final Object obj) throws DecoderException {
      if (obj instanceof byte[]) {
        return decode((byte[]) obj);
      }
      if (obj instanceof String) {
        return decode((String) obj);
      }
      throw new DecoderException("Parameter supplied to Base-N decode is not a byte[] or a String");
    }

    /**
     * Decodes a String containing characters in the Base-N alphabet.
     *
     * @param pArray A String containing Base-N character data
     * @return a byte array containing binary data
     */
    public byte[] decode(final String pArray) {
      return decode(StringUtils.getBytesUtf8(pArray));
    }

    /**
     * Encodes a byte[] containing binary data, into a byte[] containing characters in the alphabet.
     *
     * @param pArray a byte array containing binary data
     * @return A byte array containing only the base N alphabetic character data
     */
    public byte[] encode(final byte[] pArray) {
      if (BinaryCodec.isEmpty(pArray)) {
        return pArray;
      }
      return encode(pArray, 0, pArray.length);
    }

    /**
     * Encodes a byte[] containing binary data, into a byte[] containing characters in the alphabet.
     *
     * @param pArray a byte array containing binary data
     * @param offset initial offset of the subarray.
     * @param length length of the subarray.
     * @return A byte array containing only the base N alphabetic character data
     * @since 1.11
     */
    public byte[] encode(final byte[] pArray, final int offset, final int length) {
      if (BinaryCodec.isEmpty(pArray)) {
        return pArray;
      }
      final Context context = new Context();
      encode(pArray, offset, length, context);
      encode(pArray, offset, EOF, context); // Notify encoder of EOF.
      final byte[] buf = new byte[context.pos - context.readPos];
      readResults(buf, 0, buf.length, context);
      return buf;
    }

    // package protected for access from I/O streams
    abstract void encode(byte[] pArray, int i, int length, Context context);

    /**
     * Encodes an Object using the Base-N algorithm. This method is provided in order to satisfy the
     * requirements of the Encoder interface, and will throw an EncoderException if the supplied
     * object is not of type byte[].
     *
     * @param obj Object to encode
     * @return An object (of type byte[]) containing the Base-N encoded data which corresponds to
     *     the byte[] supplied.
     * @throws EncoderException if the parameter supplied is not of type byte[]
     */
    public Object encode(final Object obj) throws EncoderException {
      if (!(obj instanceof byte[])) {
        throw new EncoderException("Parameter supplied to Base-N encode is not a byte[]");
      }
      return encode((byte[]) obj);
    }

    /**
     * Encodes a byte[] containing binary data, into a String containing characters in the
     * appropriate alphabet. Uses UTF8 encoding.
     *
     * @param pArray a byte array containing binary data
     * @return String containing only character data in the appropriate alphabet.
     * @since 1.5 This is a duplicate of {@link #encodeToString(byte[])}; it was merged during
     *     refactoring.
     */
    public String encodeAsString(final byte[] pArray) {
      return StringUtils.newStringUtf8(encode(pArray));
    }

    /**
     * Encodes a byte[] containing binary data, into a String containing characters in the Base-N
     * alphabet. Uses UTF8 encoding.
     *
     * @param pArray a byte array containing binary data
     * @return A String containing only Base-N character data
     */
    public String encodeToString(final byte[] pArray) {
      return StringUtils.newStringUtf8(encode(pArray));
    }

    /**
     * Ensure that the buffer has room for {@code size} bytes
     *
     * @param size minimum spare space required
     * @param context the context to be used
     * @return the buffer
     */
    protected byte[] ensureBufferSize(final int size, final Context context) {
      if (context.buffer == null) {
        context.buffer = new byte[Math.max(size, getDefaultBufferSize())];
        context.pos = 0;
        context.readPos = 0;

        // Overflow-conscious:
        // x + y > z  ==  x + y - z > 0
      } else if (context.pos + size - context.buffer.length > 0) {
        return resizeBuffer(context, context.pos + size);
      }
      return context.buffer;
    }

    /**
     * Returns the decoding behavior policy.
     *
     * <p>The default is lenient. If the decoding policy is strict, then decoding will raise an
     * {@link IllegalArgumentException} if trailing bits are not part of a valid encoding. Decoding
     * will compose trailing bits into 8-bit bytes and discard the remainder.
     *
     * @return true if using strict decoding
     * @since 1.15
     */
    public CodecPolicy getCodecPolicy() {
      return decodingPolicy;
    }

    /**
     * Get the default buffer size. Can be overridden.
     *
     * @return the default buffer size.
     */
    protected int getDefaultBufferSize() {
      return DEFAULT_BUFFER_SIZE;
    }

    /**
     * Calculates the amount of space needed to encode the supplied array.
     *
     * @param pArray byte[] array which will later be encoded
     * @return amount of space needed to encode the supplied array. Returns a long since a max-len
     *     array will require &gt; Integer.MAX_VALUE
     */
    public long getEncodedLength(final byte[] pArray) {
      // Calculate non-chunked size - rounded up to allow for padding
      // cast to long is needed to avoid possibility of overflow
      long len =
          ((pArray.length + unencodedBlockSize - 1) / unencodedBlockSize) * (long) encodedBlockSize;
      if (lineLength > 0) { // We're using chunking
        // Round up to nearest multiple
        len += ((len + lineLength - 1) / lineLength) * chunkSeparatorLength;
      }
      return len;
    }

    /**
     * Returns true if this object has buffered data for reading.
     *
     * @param context the context to be used
     * @return true if there is data still available for reading.
     */
    boolean hasData(final Context context) { // package protected for access from I/O streams
      return context.pos > context.readPos;
    }

    /**
     * Returns whether or not the {@code octet} is in the current alphabet. Does not allow
     * whitespace or pad.
     *
     * @param value The value to test
     * @return {@code true} if the value is defined in the current alphabet, {@code false}
     *     otherwise.
     */
    protected abstract boolean isInAlphabet(byte value);

    /**
     * Tests a given byte array to see if it contains only valid characters within the alphabet. The
     * method optionally treats whitespace and pad as valid.
     *
     * @param arrayOctet byte array to test
     * @param allowWSPad if {@code true}, then whitespace and PAD are also allowed
     * @return {@code true} if all bytes are valid characters in the alphabet or if the byte array
     *     is empty; {@code false}, otherwise
     */
    public boolean isInAlphabet(final byte[] arrayOctet, final boolean allowWSPad) {
      for (final byte octet : arrayOctet) {
        if (!isInAlphabet(octet)
            && (!allowWSPad || (octet != pad) && !Character.isWhitespace(octet))) {
          return false;
        }
      }
      return true;
    }

    /**
     * Tests a given String to see if it contains only valid characters within the alphabet. The
     * method treats whitespace and PAD as valid.
     *
     * @param basen String to test
     * @return {@code true} if all characters in the String are valid characters in the alphabet or
     *     if the String is empty; {@code false}, otherwise
     * @see #isInAlphabet(byte[], boolean)
     */
    public boolean isInAlphabet(final String basen) {
      return isInAlphabet(StringUtils.getBytesUtf8(basen), true);
    }

    /**
     * Returns true if decoding behavior is strict. Decoding will raise an {@link
     * IllegalArgumentException} if trailing bits are not part of a valid encoding.
     *
     * <p>The default is false for lenient decoding. Decoding will compose trailing bits into 8-bit
     * bytes and discard the remainder.
     *
     * @return true if using strict decoding
     * @since 1.15
     */
    public boolean isStrictDecoding() {
      return decodingPolicy == CodecPolicy.STRICT;
    }

    /**
     * Extracts buffered data into the provided byte[] array, starting at position bPos, up to a
     * maximum of bAvail bytes. Returns how many bytes were actually extracted.
     *
     * <p>Package private for access from I/O streams.
     *
     * @param b byte[] array to extract the buffered data into.
     * @param bPos position in byte[] array to start extraction at.
     * @param bAvail amount of bytes we're allowed to extract. We may extract fewer (if fewer are
     *     available).
     * @param context the context to be used
     * @return The number of bytes successfully extracted into the provided byte[] array.
     */
    int readResults(final byte[] b, final int bPos, final int bAvail, final Context context) {
      if (hasData(context)) {
        final int len = Math.min(available(context), bAvail);
        System.arraycopy(context.buffer, context.readPos, b, bPos, len);
        context.readPos += len;
        if (!hasData(context)) {
          // All data read.
          // Reset position markers but do not set buffer to null to allow its reuse.
          // hasData(context) will still return false, and this method will return 0 until
          // more data is available, or -1 if EOF.
          context.pos = context.readPos = 0;
        }
        return len;
      }
      return context.eof ? EOF : 0;
    }

    /**
     * Holds thread context so classes can be thread-safe.
     *
     * <p>This class is not itself thread-safe; each thread must allocate its own copy.
     *
     * @since 1.7
     */
    static class Context {

      /**
       * Placeholder for the bytes we're dealing with for our based logic. Bitwise operations store
       * and extract the encoding or decoding from this variable.
       */
      int ibitWorkArea;

      /**
       * Placeholder for the bytes we're dealing with for our based logic. Bitwise operations store
       * and extract the encoding or decoding from this variable.
       */
      long lbitWorkArea;

      /** Buffer for streaming. */
      byte[] buffer;

      /** Position where next character should be written in the buffer. */
      int pos;

      /** Position where next character should be read from the buffer. */
      int readPos;

      /**
       * Boolean flag to indicate the EOF has been reached. Once EOF has been reached, this object
       * becomes useless, and must be thrown away.
       */
      boolean eof;

      /**
       * Variable tracks how many characters have been written to the current line. Only used when
       * encoding. We use it to make sure each encoded line never goes beyond lineLength (if
       * lineLength &gt; 0).
       */
      int currentLinePos;

      /**
       * Writes to the buffer only occur after every 3/5 reads when encoding, and every 4/8 reads
       * when decoding. This variable helps track that.
       */
      int modulus;

      /**
       * Returns a String useful for debugging (especially within a debugger.)
       *
       * @return a String useful for debugging.
       */
      @SuppressWarnings("boxing") // OK to ignore boxing here
      @Override
      public String toString() {
        return String.format(
            "%s[buffer=%s, currentLinePos=%s, eof=%s, ibitWorkArea=%s, lbitWorkArea=%s, "
                + "modulus=%s, pos=%s, readPos=%s]",
            this.getClass().getSimpleName(),
            Arrays.toString(buffer),
            currentLinePos,
            eof,
            ibitWorkArea,
            lbitWorkArea,
            modulus,
            pos,
            readPos);
      }
    }
  }

  static class BaseNCodecOutputStream extends FilterOutputStream {

    private final boolean doEncode;

    private final BaseNCodec baseNCodec;

    private final byte[] singleByte = new byte[1];

    private final BaseNCodec.Context context = new BaseNCodec.Context();

    /**
     * @param outputStream the underlying output or null.
     * @param basedCodec a BaseNCodec.
     * @param doEncode true to encode, false to decode
     */
    public BaseNCodecOutputStream(
        final OutputStream outputStream, final BaseNCodec basedCodec, final boolean doEncode) {
      super(outputStream);
      this.baseNCodec = basedCodec;
      this.doEncode = doEncode;
    }

    /**
     * Closes this output stream and releases any system resources associated with the stream.
     *
     * <p>To write the EOF marker without closing the stream, call {@link #eof()} or use an <a
     * href="https://commons.apache.org/proper/commons-io/">Apache Commons IO</a> <a href=
     * "https://commons.apache.org/proper/commons-io/apidocs/org/apache/commons/io/output/CloseShieldOutputStream.html"
     * >CloseShieldOutputStream</a>.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
      eof();
      flush();
      out.close();
    }

    /**
     * Writes EOF.
     *
     * @since 1.11
     */
    public void eof() {
      // Notify encoder of EOF (-1).
      if (doEncode) {
        baseNCodec.encode(singleByte, 0, EOF, context);
      } else {
        baseNCodec.decode(singleByte, 0, EOF, context);
      }
    }

    /**
     * Flushes this output stream and forces any buffered output bytes to be written out to the
     * stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void flush() throws IOException {
      flush(true);
    }

    /**
     * Flushes this output stream and forces any buffered output bytes to be written out to the
     * stream. If propagate is true, the wrapped stream will also be flushed.
     *
     * @param propagate boolean flag to indicate whether the wrapped OutputStream should also be
     *     flushed.
     * @throws IOException if an I/O error occurs.
     */
    private void flush(final boolean propagate) throws IOException {
      final int avail = baseNCodec.available(context);
      if (avail > 0) {
        final byte[] buf = new byte[avail];
        final int c = baseNCodec.readResults(buf, 0, avail, context);
        if (c > 0) {
          out.write(buf, 0, c);
        }
      }
      if (propagate) {
        out.flush();
      }
    }

    /**
     * Returns true if decoding behavior is strict. Decoding will raise an {@link
     * IllegalArgumentException} if trailing bits are not part of a valid encoding.
     *
     * <p>The default is false for lenient encoding. Decoding will compose trailing bits into 8-bit
     * bytes and discard the remainder.
     *
     * @return true if using strict decoding
     * @since 1.15
     */
    public boolean isStrictDecoding() {
      return baseNCodec.isStrictDecoding();
    }

    /**
     * Writes {@code len} bytes from the specified {@code b} array starting at {@code offset} to
     * this output stream.
     *
     * @param array source byte array
     * @param offset where to start reading the bytes
     * @param len maximum number of bytes to write
     * @throws IOException if an I/O error occurs.
     * @throws NullPointerException if the byte array parameter is null
     * @throws IndexOutOfBoundsException if offset, len or buffer size are invalid
     */
    @Override
    public void write(final byte[] array, final int offset, final int len) throws IOException {
      Objects.requireNonNull(array, "array");
      if (offset < 0 || len < 0) {
        throw new IndexOutOfBoundsException();
      }
      if (offset > array.length || offset + len > array.length) {
        throw new IndexOutOfBoundsException();
      }
      if (len > 0) {
        if (doEncode) {
          baseNCodec.encode(array, offset, len, context);
        } else {
          baseNCodec.decode(array, offset, len, context);
        }
        flush(false);
      }
    }

    /**
     * Writes the specified {@code byte} to this output stream.
     *
     * @param i source byte
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(final int i) throws IOException {
      singleByte[0] = (byte) i;
      write(singleByte, 0, 1);
    }
  }

  static class Base32OutputStream extends BaseNCodecOutputStream {

    /**
     * Creates a Base32OutputStream such that all data written is Base32-encoded to the original
     * provided OutputStream.
     *
     * @param outputStream OutputStream to wrap.
     */
    public Base32OutputStream(final OutputStream outputStream) {
      this(outputStream, true);
    }

    /**
     * Creates a Base32OutputStream such that all data written is either Base32-encoded or
     * Base32-decoded to the original provided OutputStream.
     *
     * @param outputStream OutputStream to wrap.
     * @param doEncode true if we should encode all data written to us, false if we should decode.
     */
    public Base32OutputStream(final OutputStream outputStream, final boolean doEncode) {
      super(outputStream, new Base32(false), doEncode);
    }

    /**
     * Creates a Base32OutputStream such that all data written is either Base32-encoded or
     * Base32-decoded to the original provided OutputStream.
     *
     * @param outputStream OutputStream to wrap.
     * @param doEncode true if we should encode all data written to us, false if we should decode.
     * @param lineLength If doEncode is true, each line of encoded data will contain lineLength
     *     characters (rounded down to the nearest multiple of 4). If lineLength &lt;= 0, the
     *     encoded data is not divided into lines. If doEncode is false, lineLength is ignored.
     * @param lineSeparator If doEncode is true, each line of encoded data will be terminated with
     *     this byte sequence (e.g. \r\n). If lineLength &lt;= 0, the lineSeparator is not used. If
     *     doEncode is false lineSeparator is ignored.
     */
    public Base32OutputStream(
        final OutputStream outputStream,
        final boolean doEncode,
        final int lineLength,
        final byte[] lineSeparator) {
      super(outputStream, new Base32(lineLength, lineSeparator), doEncode);
    }

    /**
     * Creates a Base32OutputStream such that all data written is either Base32-encoded or
     * Base32-decoded to the original provided OutputStream.
     *
     * @param outputStream OutputStream to wrap.
     * @param doEncode true if we should encode all data written to us, false if we should decode.
     * @param lineLength If doEncode is true, each line of encoded data will contain lineLength
     *     characters (rounded down to the nearest multiple of 4). If lineLength &lt;= 0, the
     *     encoded data is not divided into lines. If doEncode is false, lineLength is ignored.
     * @param lineSeparator If doEncode is true, each line of encoded data will be terminated with
     *     this byte sequence (e.g. \r\n). If lineLength &lt;= 0, the lineSeparator is not used. If
     *     doEncode is false lineSeparator is ignored.
     * @param decodingPolicy The decoding policy.
     * @since 1.15
     */
    public Base32OutputStream(
        final OutputStream outputStream,
        final boolean doEncode,
        final int lineLength,
        final byte[] lineSeparator,
        final CodecPolicy decodingPolicy) {
      super(
          outputStream,
          new Base32(lineLength, lineSeparator, false, BaseNCodec.PAD_DEFAULT, decodingPolicy),
          doEncode);
    }
  }

  static class DecoderException extends Exception {

    /**
     * Declares the Serial Version Uid.
     *
     * @see <a href="http://c2.com/cgi/wiki?AlwaysDeclareSerialVersionUid">Always Declare Serial
     *     Version Uid</a>
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with {@code null} as its detail message. The cause is not
     * initialized, and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @since 1.4
     */
    public DecoderException() {}

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized,
     * and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message The detail message which is saved for later retrieval by the {@link
     *     #getMessage()} method.
     */
    public DecoderException(final String message) {
      super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * <p>Note that the detail message associated with {@code cause} is not automatically
     * incorporated into this exception's detail message.
     *
     * @param message The detail message which is saved for later retrieval by the {@link
     *     #getMessage()} method.
     * @param cause The cause which is saved for later retrieval by the {@link #getCause()} method.
     *     A {@code null} value is permitted, and indicates that the cause is nonexistent or
     *     unknown.
     * @since 1.4
     */
    public DecoderException(final String message, final Throwable cause) {
      super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of <code>
     * (cause==null ?
     * null : cause.toString())</code> (which typically contains the class and detail message of
     * {@code cause}). This constructor is useful for exceptions that are little more than wrappers
     * for other throwables.
     *
     * @param cause The cause which is saved for later retrieval by the {@link #getCause()} method.
     *     A {@code null} value is permitted, and indicates that the cause is nonexistent or
     *     unknown.
     * @since 1.4
     */
    public DecoderException(final Throwable cause) {
      super(cause);
    }
  }

  static class EncoderException extends Exception {

    /**
     * Declares the Serial Version Uid.
     *
     * @see <a href="http://c2.com/cgi/wiki?AlwaysDeclareSerialVersionUid">Always Declare Serial
     *     Version Uid</a>
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with {@code null} as its detail message. The cause is not
     * initialized, and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @since 1.4
     */
    public EncoderException() {}

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized,
     * and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message a useful message relating to the encoder specific error.
     */
    public EncoderException(final String message) {
      super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * <p>Note that the detail message associated with {@code cause} is not automatically
     * incorporated into this exception's detail message.
     *
     * @param message The detail message which is saved for later retrieval by the {@link
     *     #getMessage()} method.
     * @param cause The cause which is saved for later retrieval by the {@link #getCause()} method.
     *     A {@code null} value is permitted, and indicates that the cause is nonexistent or
     *     unknown.
     * @since 1.4
     */
    public EncoderException(final String message, final Throwable cause) {
      super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of <code>
     * (cause==null ?
     * null : cause.toString())</code> (which typically contains the class and detail message of
     * {@code cause}). This constructor is useful for exceptions that are little more than wrappers
     * for other throwables.
     *
     * @param cause The cause which is saved for later retrieval by the {@link #getCause()} method.
     *     A {@code null} value is permitted, and indicates that the cause is nonexistent or
     *     unknown.
     * @since 1.4
     */
    public EncoderException(final Throwable cause) {
      super(cause);
    }
  }

  static class StringUtils {
    /**
     * Calls {@link String#getBytes(Charset)}
     *
     * @param string The string to encode (if null, return null).
     * @param charset The {@link Charset} to encode the {@code String}
     * @return the encoded bytes
     */
    private static byte[] getBytes(final String string, final Charset charset) {
      return string == null ? null : string.getBytes(charset);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-8 charset, storing the result
     * into a new byte array.
     *
     * @param string the String to encode, may be {@code null}
     * @return encoded bytes, or {@code null} if the input string was {@code null}
     * @throws NullPointerException Thrown if {@link StandardCharsets#UTF_8} is not initialized,
     *     which should never happen since it is required by the Java platform specification.
     * @see Charset
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     */
    public static byte[] getBytesUtf8(final String string) {
      return getBytes(string, StandardCharsets.UTF_8);
    }

    /**
     * Constructs a new {@code String} by decoding the specified array of bytes using the given
     * charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @param charset The {@link Charset} to encode the {@code String}; not {@code null}
     * @return A new {@code String} decoded from the specified array of bytes using the given
     *     charset, or {@code null} if the input byte array was {@code null}.
     * @throws NullPointerException Thrown if charset is {@code null}
     */
    private static String newString(final byte[] bytes, final Charset charset) {
      return bytes == null ? null : new String(bytes, charset);
    }

    /**
     * Constructs a new {@code String} by decoding the specified array of bytes using the UTF-8
     * charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @return A new {@code String} decoded from the specified array of bytes using the UTF-8
     *     charset, or {@code null} if the input byte array was {@code null}.
     * @throws NullPointerException Thrown if {@link StandardCharsets#UTF_8} is not initialized,
     *     which should never happen since it is required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     */
    public static String newStringUtf8(final byte[] bytes) {
      return newString(bytes, StandardCharsets.UTF_8);
    }
  }
}
