package org.stellar.sdk.scval;

import static org.stellar.sdk.Util.getBytes;

import java.math.BigInteger;
import java.util.Arrays;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.UInt256Parts;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_U256}. */
class ScvUint256 {
  private static final SCValType TYPE = SCValType.SCV_U256;

  private static final BigInteger MIN_VALUE = BigInteger.ZERO;
  private static final BigInteger MAX_VALUE =
      BigInteger.valueOf(2).pow(256).subtract(BigInteger.ONE);

  static SCVal toSCVal(BigInteger value) {
    if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
      throw new IllegalArgumentException(
          String.format(
              "invalid value, expected between %s and %s, but got %s",
              MIN_VALUE, MAX_VALUE, value));
    }

    byte[] bytes = value.toByteArray();
    byte[] paddedBytes = new byte[32];
    int numBytesToCopy = Math.min(bytes.length, 32);
    int copyStartIndex = bytes.length - numBytesToCopy;
    System.arraycopy(bytes, copyStartIndex, paddedBytes, 32 - numBytesToCopy, numBytesToCopy);

    UInt256Parts uInt256Parts =
        UInt256Parts.builder()
            .hi_hi(
                new Uint64(
                    new XdrUnsignedHyperInteger(
                        new BigInteger(1, Arrays.copyOfRange(paddedBytes, 0, 8)))))
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
    return SCVal.builder().discriminant(TYPE).u256(uInt256Parts).build();
  }

  static BigInteger fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    UInt256Parts uInt256Parts = scVal.getU256();
    byte[] hiHiBytes = getBytes(uInt256Parts.getHi_hi().getUint64().getNumber());
    byte[] hiLoBytes = getBytes(uInt256Parts.getHi_lo().getUint64().getNumber());
    byte[] loHiBytes = getBytes(uInt256Parts.getLo_hi().getUint64().getNumber());
    byte[] loLoBytes = getBytes(uInt256Parts.getLo_lo().getUint64().getNumber());

    byte[] fullBytes = new byte[32];
    System.arraycopy(hiHiBytes, 0, fullBytes, 0, 8);
    System.arraycopy(hiLoBytes, 0, fullBytes, 8, 8);
    System.arraycopy(loHiBytes, 0, fullBytes, 16, 8);
    System.arraycopy(loLoBytes, 0, fullBytes, 24, 8);

    return new BigInteger(1, fullBytes);
  }
}
