package org.stellar.sdk.scval;

import static org.stellar.sdk.Util.getBytes;

import java.math.BigInteger;
import java.util.Arrays;
import org.stellar.sdk.xdr.Int256Parts;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_I256}. */
class ScvInt256 {
  private static final SCValType TYPE = SCValType.SCV_I256;

  private static final BigInteger MIN_VALUE = BigInteger.valueOf(-2).pow(255);
  private static final BigInteger MAX_VALUE =
      BigInteger.valueOf(2).pow(255).subtract(BigInteger.ONE);

  static SCVal toSCVal(BigInteger value) {
    if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
      throw new IllegalArgumentException(
          String.format(
              "invalid value, expected between %s and %s, but got %s",
              MIN_VALUE, MAX_VALUE, value));
    }

    byte[] bytes = value.toByteArray();
    byte[] paddedBytes = new byte[32];

    if (value.signum() >= 0) {
      int numBytesToCopy = Math.min(bytes.length, 32);
      int copyStartIndex = bytes.length - numBytesToCopy;
      System.arraycopy(bytes, copyStartIndex, paddedBytes, 32 - numBytesToCopy, numBytesToCopy);
    } else {
      Arrays.fill(paddedBytes, 0, 32 - bytes.length, (byte) 0xFF);
      System.arraycopy(bytes, 0, paddedBytes, 32 - bytes.length, bytes.length);
    }

    Int256Parts int256Parts =
        new Int256Parts.Builder()
            .hi_hi(new Int64(Longs.fromByteArray(Arrays.copyOfRange(paddedBytes, 0, 8))))
            .hi_lo(
                new Uint64(
                    new XdrUnsignedHyperInteger(
                        new BigInteger(1, Arrays.copyOfRange(paddedBytes, 8, 16)))))
            .lo_hi(
                new Uint64(
                    new XdrUnsignedHyperInteger(
                        new BigInteger(1, Arrays.copyOfRange(paddedBytes, 16, 24)))))
            .lo_lo(
                new Uint64(
                    new XdrUnsignedHyperInteger(
                        new BigInteger(1, Arrays.copyOfRange(paddedBytes, 24, 32)))))
            .build();
    return new SCVal.Builder().discriminant(TYPE).i256(int256Parts).build();
  }

  static BigInteger fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    Int256Parts int256Parts = scVal.getI256();
    byte[] hiHiBytes = Longs.toByteArray(int256Parts.getHi_hi().getInt64());
    byte[] hiLoBytes = getBytes(int256Parts.getHi_lo().getUint64().getNumber());
    byte[] loHiBytes = getBytes(int256Parts.getLo_hi().getUint64().getNumber());
    byte[] loLoBytes = getBytes(int256Parts.getLo_lo().getUint64().getNumber());

    byte[] fullBytes = new byte[32];
    System.arraycopy(hiHiBytes, 0, fullBytes, 0, 8);
    System.arraycopy(hiLoBytes, 0, fullBytes, 8, 8);
    System.arraycopy(loHiBytes, 0, fullBytes, 16, 8);
    System.arraycopy(loLoBytes, 0, fullBytes, 24, 8);

    return new BigInteger(fullBytes);
  }
}
