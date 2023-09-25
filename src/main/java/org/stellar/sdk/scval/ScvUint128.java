package org.stellar.sdk.scval;

import static org.stellar.sdk.Util.getBytes;

import java.math.BigInteger;
import java.util.Arrays;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.UInt128Parts;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_I32}. */
class ScvUint128 {
  private static final SCValType TYPE = SCValType.SCV_U128;

  private static final BigInteger MIN_VALUE = BigInteger.ZERO;
  private static final BigInteger MAX_VALUE =
      BigInteger.valueOf(2).pow(128).subtract(BigInteger.ONE);

  static SCVal toSCVal(BigInteger value) {
    if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
      throw new IllegalArgumentException(
          String.format(
              "invalid value, expected between %s and %s, but got %s",
              MIN_VALUE, MAX_VALUE, value));
    }

    byte[] bytes = value.toByteArray();
    byte[] paddedBytes = new byte[16];
    int numBytesToCopy = Math.min(bytes.length, 16);
    int copyStartIndex = bytes.length - numBytesToCopy;
    System.arraycopy(bytes, copyStartIndex, paddedBytes, 16 - numBytesToCopy, numBytesToCopy);

    UInt128Parts uInt128Parts =
        new UInt128Parts.Builder()
            .hi(
                new Uint64(
                    new XdrUnsignedHyperInteger(
                        new BigInteger(1, Arrays.copyOfRange(paddedBytes, 0, 8)))))
            .lo(
                new Uint64(
                    new XdrUnsignedHyperInteger(
                        new BigInteger(1, Arrays.copyOfRange(paddedBytes, 8, 16)))))
            .build();
    return new SCVal.Builder().discriminant(TYPE).u128(uInt128Parts).build();
  }

  static BigInteger fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    UInt128Parts uInt128Parts = scVal.getU128();
    byte[] hiBytes = getBytes(uInt128Parts.getHi().getUint64().getNumber());
    byte[] loBytes = getBytes(uInt128Parts.getLo().getUint64().getNumber());

    byte[] fullBytes = new byte[16];
    System.arraycopy(hiBytes, 0, fullBytes, 0, 8);
    System.arraycopy(loBytes, 0, fullBytes, 8, 8);

    return new BigInteger(1, fullBytes);
  }
}
