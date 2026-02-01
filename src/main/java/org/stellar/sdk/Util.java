package org.stellar.sdk;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.stellar.sdk.exception.UnexpectedException;

/**
 * Utility class for common operations.
 *
 * <p>note: For some reason we need to make it public, but I don't recommend to use it directly.
 */
public class Util {
  /**
   * Returns hex representation of <code>bytes</code> array.
   *
   * @param bytes byte array to convert to hex
   * @return hex representation of the byte array (uppercase)
   */
  public static String bytesToHex(byte[] bytes) {
    return Hex.encodeHexString(bytes, false);
  }

  /**
   * Returns byte array representation of <code>hex</code> string.
   *
   * @param s hex string to convert to byte array
   * @return byte array representation of the hex string
   * @throws IllegalArgumentException if the string contains non-hex characters or has odd length
   */
  public static byte[] hexToBytes(String s) {
    try {
      return Hex.decodeHex(s);
    } catch (DecoderException e) {
      throw new IllegalArgumentException("Invalid hex string: " + s, e);
    }
  }

  /**
   * Returns SHA-256 hash of <code>data</code>.
   *
   * @param data byte array to hash
   * @return SHA-256 hash of the data
   */
  public static byte[] hash(byte[] data) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(data);
      return md.digest();
    } catch (NoSuchAlgorithmException e) {
      throw new UnexpectedException("SHA-256 not implemented");
    }
  }

  /**
   * Pads <code>bytes</code> array to <code>length</code> with zeros.
   *
   * @param bytes byte array to pad
   * @param length length of the resulting array
   * @return padded byte array
   */
  static byte[] paddedByteArray(byte[] bytes, int length) {
    byte[] finalBytes = new byte[length];
    Arrays.fill(finalBytes, (byte) 0);
    System.arraycopy(bytes, 0, finalBytes, 0, bytes.length);
    return finalBytes;
  }

  /**
   * Pads <code>string</code> to <code>length</code> with zeros.
   *
   * @param string string to pad
   * @param length length of the resulting array
   * @return padded byte array
   */
  public static byte[] paddedByteArray(String string, int length) {
    return Util.paddedByteArray(string.getBytes(), length);
  }

  /**
   * Remove zeros from the end of <code>bytes</code> array.
   *
   * @param bytes byte array to trim
   * @return trimmed byte array
   */
  static String paddedByteArrayToString(byte[] bytes) {
    String[] strArray = new String(bytes, StandardCharsets.UTF_8).split("\0");
    if (strArray.length > 0) {
      return strArray[0];
    }
    return "";
  }

  /**
   * Get the version of the SDK
   *
   * @return version
   */
  public static String getSdkVersion() {
    String clientVersion = "dev";
    String implementationVersion = Util.class.getPackage().getImplementationVersion();
    if (implementationVersion != null) {
      clientVersion = implementationVersion;
    }
    return clientVersion;
  }

  /**
   * Throws {@link IllegalArgumentException} if the given asset is native (XLM).
   *
   * @param asset The asset to check
   */
  public static AssetTypeCreditAlphaNum assertNonNativeAsset(Asset asset) {
    if (asset instanceof AssetTypeCreditAlphaNum) {
      return (AssetTypeCreditAlphaNum) asset;
    }
    throw new IllegalArgumentException("native assets are not supported");
  }

  /**
   * Returns an 8 byte array representation of a BigInteger value.
   *
   * @param value BigInteger value to convert to byte array
   * @return byte array
   */
  public static byte[] getBytes(BigInteger value) {
    byte[] bytes = value.toByteArray();
    if (bytes.length < 8) {
      byte[] temp = new byte[8];
      System.arraycopy(bytes, 0, temp, 8 - bytes.length, bytes.length);
      bytes = temp;
    } else if (bytes.length > 8) {
      bytes = Arrays.copyOfRange(bytes, bytes.length - 8, bytes.length);
    }
    return bytes;
  }

  /** The function that converts XDR string to XDR object. */
  @FunctionalInterface
  public interface XdrDecodeFunction<T, R> {
    R apply(T t) throws IOException;
  }

  /**
   * Parses XDR string to XDR object.
   *
   * @param xdr XDR string
   * @param fromXdrBase64 function that converts XDR string to XDR object
   * @return XDR object, or null if the input string is null or empty
   * @param <T> type of XDR object
   */
  public static <T> T parseXdr(String xdr, XdrDecodeFunction<String, T> fromXdrBase64) {
    if (xdr == null || xdr.isEmpty()) {
      return null;
    }
    try {
      return fromXdrBase64.apply(xdr);
    } catch (IOException e) {
      throw new UnexpectedException("Unable to parse XDR string", e);
    }
  }
}
