package org.stellar.sdk.xdr;

import com.google.common.base.Objects;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Represents XDR Unsigned Hyper Integer.
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4506#section-4.5">XDR: External Data
 *     Representation Standard</a>
 */
public class XdrUnsignedHyperInteger implements XdrElement {
  public static final BigInteger MAX_VALUE = new BigInteger("18446744073709551615");
  public static final BigInteger MIN_VALUE = BigInteger.ZERO;
  private final BigInteger number;

  public XdrUnsignedHyperInteger(BigInteger number) {
    if (number.compareTo(MIN_VALUE) < 0 || number.compareTo(MAX_VALUE) > 0) {
      throw new IllegalArgumentException("number must be between 0 and 2^64 - 1 inclusive");
    }
    this.number = number;
  }

  public XdrUnsignedHyperInteger(Long number) {
    if (number < 0) {
      throw new IllegalArgumentException(
          "number must be greater than or equal to 0 if you want to construct it from Long");
    }
    this.number = BigInteger.valueOf(number);
  }

  @Override
  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.write(getBytes());
  }

  public static XdrUnsignedHyperInteger decode(XdrDataInputStream stream) throws IOException {
    byte[] bytes = new byte[8];
    stream.readFully(bytes);
    BigInteger uint64 = new BigInteger(1, bytes);
    return new XdrUnsignedHyperInteger(uint64);
  }

  private byte[] getBytes() {
    byte[] bytes = number.toByteArray();
    byte[] paddedBytes = new byte[8];

    int numBytesToCopy = Math.min(bytes.length, 8);
    int copyStartIndex = bytes.length - numBytesToCopy;
    System.arraycopy(bytes, copyStartIndex, paddedBytes, 8 - numBytesToCopy, numBytesToCopy);
    return paddedBytes;
  }

  public BigInteger getNumber() {
    return number;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.number);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof XdrUnsignedHyperInteger)) {
      return false;
    }

    XdrUnsignedHyperInteger other = (XdrUnsignedHyperInteger) object;
    return Objects.equal(this.number, other.number);
  }

  public String toString() {
    return "XdrUnsignedInteger(number=" + this.getNumber() + ")";
  }
}
