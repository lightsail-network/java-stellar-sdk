package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import lombok.Value;
import org.stellar.sdk.Base64Factory;

/**
 * Represents XDR Unsigned Hyper Integer.
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4506#section-4.5">XDR: External Data
 *     Representation Standard</a>
 */
@Value
public class XdrUnsignedHyperInteger implements XdrElement {
  /** Largest value representable by XDR unsigned hyper integer. */
  public static final BigInteger MAX_VALUE = new BigInteger("18446744073709551615");

  /** Smallest value representable by XDR unsigned hyper integer. */
  public static final BigInteger MIN_VALUE = BigInteger.ZERO;

  /**
   * Numeric value stored by this XDR unsigned hyper integer.
   *
   * @return the unsigned 64-bit value
   */
  BigInteger number;

  /**
   * Creates an {@link XdrUnsignedHyperInteger} from a {@link BigInteger}.
   *
   * @param number the unsigned 64-bit value
   * @throws IllegalArgumentException if {@code number} is outside the valid range
   */
  public XdrUnsignedHyperInteger(BigInteger number) {
    if (number.compareTo(MIN_VALUE) < 0 || number.compareTo(MAX_VALUE) > 0) {
      throw new IllegalArgumentException("number must be between 0 and 2^64 - 1 inclusive");
    }
    this.number = number;
  }

  /**
   * Creates an {@link XdrUnsignedHyperInteger} from a non-negative {@link Long}.
   *
   * @param number the unsigned 64-bit value
   * @throws IllegalArgumentException if {@code number} is negative
   */
  public XdrUnsignedHyperInteger(Long number) {
    if (number < 0) {
      throw new IllegalArgumentException(
          "number must be greater than or equal to 0 if you want to construct it from Long");
    }
    this.number = BigInteger.valueOf(number);
  }

  /**
   * Encodes this value to XDR.
   *
   * @param stream the destination XDR output stream
   * @throws IOException if an I/O error occurs while writing the value
   */
  @Override
  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.write(getBytes());
  }

  /**
   * Decodes an {@link XdrUnsignedHyperInteger} from the provided stream.
   *
   * @param stream the source XDR input stream
   * @param maxDepth the maximum decoding depth, ignored for this leaf type
   * @return the decoded {@link XdrUnsignedHyperInteger}
   * @throws IOException if an I/O error occurs while reading the value
   */
  public static XdrUnsignedHyperInteger decode(XdrDataInputStream stream, int maxDepth)
      throws IOException {
    // maxDepth is intentionally not checked - XdrUnsignedHyperInteger is a leaf type with no
    // recursive decoding
    byte[] bytes = new byte[8];
    stream.readFully(bytes);
    BigInteger uint64 = new BigInteger(1, bytes);
    return new XdrUnsignedHyperInteger(uint64);
  }

  /**
   * Decodes an {@link XdrUnsignedHyperInteger} from the provided stream using the default maximum
   * depth.
   *
   * @param stream the source XDR input stream
   * @return the decoded {@link XdrUnsignedHyperInteger}
   * @throws IOException if an I/O error occurs while reading the value
   */
  public static XdrUnsignedHyperInteger decode(XdrDataInputStream stream) throws IOException {
    return decode(stream, XdrDataInputStream.DEFAULT_MAX_DEPTH);
  }

  private byte[] getBytes() {
    byte[] bytes = number.toByteArray();
    byte[] paddedBytes = new byte[8];

    int numBytesToCopy = Math.min(bytes.length, 8);
    int copyStartIndex = bytes.length - numBytesToCopy;
    System.arraycopy(bytes, copyStartIndex, paddedBytes, 8 - numBytesToCopy, numBytesToCopy);
    return paddedBytes;
  }

  /**
   * Serializes this value to JSON.
   *
   * @return the JSON representation of this value
   */
  @Override
  public String toJson() {
    return XdrElement.gson.toJson(toJsonObject());
  }

  Object toJsonObject() {
    return this.number.toString();
  }

  /**
   * Parses an {@link XdrUnsignedHyperInteger} from JSON.
   *
   * @param json the JSON representation
   * @return the parsed {@link XdrUnsignedHyperInteger}, or {@code null} if the input is {@code
   *     null}
   * @throws IllegalArgumentException if the JSON value is invalid
   */
  public static XdrUnsignedHyperInteger fromJson(String json) {
    return fromJsonObject(XdrElement.gson.fromJson(json, Object.class));
  }

  static XdrUnsignedHyperInteger fromJsonObject(Object json) {
    if (json == null) {
      return null;
    }
    return new XdrUnsignedHyperInteger(XdrElement.jsonToBigInteger(json));
  }

  /**
   * Decodes an {@link XdrUnsignedHyperInteger} from a base64-encoded XDR string.
   *
   * @param xdr the base64-encoded XDR string
   * @return the decoded {@link XdrUnsignedHyperInteger}
   * @throws IOException if the input is invalid or cannot be decoded
   */
  public static XdrUnsignedHyperInteger fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  /**
   * Decodes an {@link XdrUnsignedHyperInteger} from raw XDR bytes.
   *
   * @param xdr the raw XDR bytes
   * @return the decoded {@link XdrUnsignedHyperInteger}
   * @throws IOException if the input is invalid or cannot be decoded
   */
  public static XdrUnsignedHyperInteger fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
