package org.stellar.sdk;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.exception.ConnectionErrorException;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.requests.ResponseHandler;

/**
 * Utility class for common operations.
 *
 * <p>note: For some reason we need to make it public, but I don't recommend to use it directly.
 */
public class Util {
  // TODO: make HEX_ARRAY private
  public static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

  /**
   * Returns hex representation of <code>bytes</code> array.
   *
   * @param bytes byte array to convert to hex
   * @return hex representation of the byte array
   */
  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = HEX_ARRAY[v >>> 4];
      hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }

  /**
   * Returns byte array representation of <code>hex</code> string.
   *
   * @param s hex string to convert to byte array
   * @return byte array representation of the hex string
   */
  public static byte[] hexToBytes(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] =
          (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
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
      throw new RuntimeException("SHA-256 not implemented");
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
    String[] strArray = new String(bytes).split("\0");
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

  /**
   * Executes a GET request and handles the response.
   *
   * @param <T> The type of the response object.
   * @param httpClient The OkHttpClient to use for the request.
   * @param url The URL to send the GET request to.
   * @param typeToken The TypeToken representing the type of the response.
   * @return The response object of type T.
   * @throws ConnectionErrorException If there's an error during the HTTP request.
   */
  // TODO: move to requestBuilder
  public static <T> T executeGetRequest(
      OkHttpClient httpClient, HttpUrl url, TypeToken<T> typeToken) {
    ResponseHandler<T> responseHandler = new ResponseHandler<>(typeToken);

    Request request = new Request.Builder().get().url(url).build();
    Response response = null;
    try {
      response = httpClient.newCall(request).execute();
    } catch (IOException e) {
      throw new ConnectionErrorException(e);
    }
    return responseHandler.handleResponse(response);
  }
}
