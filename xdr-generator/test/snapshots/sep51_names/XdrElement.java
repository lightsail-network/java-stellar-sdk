package org.stellar.sdk.xdr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import org.stellar.sdk.Base64Factory;

/** Common parent interface for all generated classes. */
public interface XdrElement {
  /** Shared Gson instance used by generated XDR classes for JSON serialization. */
  Gson gson =
      new GsonBuilder()
          .disableHtmlEscaping()
          .serializeNulls()
          .setObjectToNumberStrategy(ToNumberPolicy.BIG_DECIMAL)
          .create();

  /**
   * Encodes this value to XDR and writes it to the provided stream.
   *
   * @param stream the destination XDR output stream
   * @throws IOException if an I/O error occurs while writing the value
   */
  void encode(XdrDataOutputStream stream) throws IOException;

  /**
   * Encodes this value to XDR and returns the base64-encoded result.
   *
   * @return the base64-encoded XDR representation
   * @throws IOException if an I/O error occurs while encoding the value
   */
  default String toXdrBase64() throws IOException {
    return Base64Factory.getInstance().encodeToString(toXdrByteArray());
  }

  /**
   * Encodes this value to XDR and returns the raw bytes.
   *
   * @return the raw XDR byte representation
   * @throws IOException if an I/O error occurs while encoding the value
   */
  default byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  /**
   * Serializes this value to JSON.
   *
   * @return the JSON representation of this value
   */
  String toJson();

  /**
   * Returns the lowercase hexadecimal representation of a byte array.
   *
   * @param bytes the bytes to encode
   * @return the lowercase hexadecimal representation
   */
  static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      sb.append(String.format("%02x", b & 0xFF));
    }
    return sb.toString();
  }

  /**
   * Decodes a hexadecimal string into bytes.
   *
   * @param hex the hexadecimal string to decode
   * @return the decoded bytes
   * @throws IllegalArgumentException if the input length is odd or contains non-hex characters
   */
  static byte[] hexToBytes(String hex) {
    if (hex.length() % 2 != 0) {
      throw new IllegalArgumentException("Hex string must have an even length");
    }
    int len = hex.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      int hi = Character.digit(hex.charAt(i), 16);
      int lo = Character.digit(hex.charAt(i + 1), 16);
      if (hi < 0 || lo < 0) {
        throw new IllegalArgumentException("Hex string contains non-hex characters");
      }
      data[i / 2] = (byte) ((hi << 4) + lo);
    }
    return data;
  }

  /**
   * Converts a byte array to an escaped ASCII string suitable for JSON serialization.
   *
   * @param data the bytes to encode
   * @return the escaped ASCII representation
   */
  static String bytesToEscapedAscii(byte[] data) {
    StringBuilder sb = new StringBuilder();
    for (byte b : data) {
      int unsigned = b & 0xFF;
      switch (unsigned) {
        case 0:
          sb.append("\\0");
          break;
        case 9:
          sb.append("\\t");
          break;
        case 10:
          sb.append("\\n");
          break;
        case 13:
          sb.append("\\r");
          break;
        case 92:
          sb.append("\\\\");
          break;
        default:
          if (unsigned >= 32 && unsigned <= 126) {
            sb.append((char) unsigned);
          } else {
            sb.append(String.format("\\x%02x", unsigned));
          }
          break;
      }
    }
    return sb.toString();
  }

  /**
   * Decodes an escaped ASCII string produced by {@link #bytesToEscapedAscii(byte[])}.
   *
   * @param s the escaped ASCII string to decode
   * @return the decoded bytes
   * @throws IllegalArgumentException if the input contains invalid escape sequences or characters
   */
  static byte[] escapedAsciiToBytes(String s) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int i = 0;
    while (i < s.length()) {
      char c = s.charAt(i);
      if (c != '\\') {
        if (c < 0x20 || c > 0x7E) {
          throw new IllegalArgumentException(
              "Unescaped characters must be printable ASCII: " + Integer.toHexString(c));
        }
        out.write(c);
        i++;
        continue;
      }

      if (i + 1 >= s.length()) {
        throw new IllegalArgumentException("Incomplete escape sequence at end of string");
      }

      char next = s.charAt(i + 1);
      switch (next) {
        case '0':
          out.write(0);
          i += 2;
          break;
        case 't':
          out.write(9);
          i += 2;
          break;
        case 'n':
          out.write(10);
          i += 2;
          break;
        case 'r':
          out.write(13);
          i += 2;
          break;
        case '\\':
          out.write(92);
          i += 2;
          break;
        case 'x':
          if (i + 3 >= s.length()) {
            throw new IllegalArgumentException("Incomplete hex escape sequence");
          }
          int hi = Character.digit(s.charAt(i + 2), 16);
          int lo = Character.digit(s.charAt(i + 3), 16);
          if (hi < 0 || lo < 0) {
            throw new IllegalArgumentException("Invalid hex escape sequence");
          }
          out.write((hi << 4) + lo);
          i += 4;
          break;
        default:
          throw new IllegalArgumentException("Unknown escape sequence: \\" + next);
      }
    }
    return out.toByteArray();
  }

  /**
   * Converts a JSON scalar into a Java {@code long}.
   *
   * @param json the JSON value to convert
   * @return the converted {@code long} value
   * @throws IllegalArgumentException if the JSON value is not a string or number
   */
  static long jsonToLong(Object json) {
    if (json instanceof String) {
      return Long.parseLong((String) json);
    }
    if (json instanceof BigDecimal) {
      return ((BigDecimal) json).longValueExact();
    }
    if (json instanceof Number) {
      return ((Number) json).longValue();
    }
    throw new IllegalArgumentException("Expected JSON string or number, got: " + json);
  }

  /**
   * Converts a JSON scalar into a {@link BigInteger}.
   *
   * @param json the JSON value to convert
   * @return the converted {@link BigInteger} value
   * @throws IllegalArgumentException if the JSON value is not a string or number
   */
  static BigInteger jsonToBigInteger(Object json) {
    if (json instanceof String) {
      return new BigInteger((String) json);
    }
    if (json instanceof BigDecimal) {
      return ((BigDecimal) json).toBigIntegerExact();
    }
    if (json instanceof Number) {
      return BigInteger.valueOf(((Number) json).longValue());
    }
    throw new IllegalArgumentException("Expected JSON string or number, got: " + json);
  }

  /**
   * Converts a Java array into a JSON array using the provided mapper.
   *
   * @param array the array to convert
   * @param mapper maps each element index to a JSON-compatible value
   * @param <T> the Java element type
   * @return the converted JSON array
   */
  @SuppressWarnings("unchecked")
  static <T> List<Object> arrayToJsonArray(T[] array, IntFunction<Object> mapper) {
    List<Object> list = new ArrayList<>(array.length);
    for (int i = 0; i < array.length; i++) {
      list.add(mapper.apply(i));
    }
    return list;
  }

  /**
   * Converts a JSON array into a Java array using the provided mapper.
   *
   * @param list the JSON array to convert
   * @param clazz the Java array component type
   * @param mapper maps each JSON value to the target Java type
   * @param <T> the Java element type
   * @return the converted Java array
   */
  @SuppressWarnings("unchecked")
  static <T> T[] jsonArrayToArray(List<Object> list, Class<T> clazz, Function<Object, T> mapper) {
    T[] array = (T[]) Array.newInstance(clazz, list.size());
    for (int i = 0; i < list.size(); i++) {
      array[i] = mapper.apply(list.get(i));
    }
    return array;
  }
}
