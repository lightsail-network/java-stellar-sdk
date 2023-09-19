package org.stellar.sdk.scval;

import static org.stellar.sdk.Util.getBytes;

import com.google.common.primitives.Longs;
import java.math.BigInteger;
import java.util.Arrays;
import org.stellar.sdk.xdr.Int128Parts;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_I128}. */
class ScvInt128 {
  private static final SCValType TYPE = SCValType.SCV_I128;

  private static final BigInteger MIN_VALUE = BigInteger.valueOf(-2).pow(127);
  private static final BigInteger MAX_VALUE =
      BigInteger.valueOf(2).pow(127).subtract(BigInteger.ONE);

  static SCVal toSCVal(BigInteger value) {
    if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
      throw new IllegalArgumentException(
          String.format(
              "invalid value, expected between %s and %s, but got %s",
              MIN_VALUE, MAX_VALUE, value));
    }

    byte[] bytes = value.toByteArray();
    byte[] paddedBytes = new byte[16];
    if (value.signum() >= 0) {
      int numBytesToCopy = Math.min(bytes.length, 16);
      int copyStartIndex = bytes.length - numBytesToCopy;
      System.arraycopy(bytes, copyStartIndex, paddedBytes, 16 - numBytesToCopy, numBytesToCopy);
    } else {
      Arrays.fill(paddedBytes, 0, 16 - bytes.length, (byte) 0xFF);
      System.arraycopy(bytes, 0, paddedBytes, 16 - bytes.length, bytes.length);
    }

    Int128Parts int128Parts =
        new Int128Parts.Builder()
            .hi(new Int64(Longs.fromByteArray(Arrays.copyOfRange(paddedBytes, 0, 8))))
            .lo(
                new Uint64(
                    new XdrUnsignedHyperInteger(
                        new BigInteger(1, Arrays.copyOfRange(paddedBytes, 8, 16)))))
            .build();
    return new SCVal.Builder().discriminant(TYPE).i128(int128Parts).build();
  }

  static BigInteger fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    Int128Parts int128Parts = scVal.getI128();
    byte[] hiBytes = Longs.toByteArray(int128Parts.getHi().getInt64());
    byte[] loBytes = getBytes(int128Parts.getLo().getUint64().getNumber());

    byte[] fullBytes = new byte[16];
    System.arraycopy(hiBytes, 0, fullBytes, 0, 8);
    System.arraycopy(loBytes, 0, fullBytes, 8, 8);

    return new BigInteger(fullBytes);
  }
}
