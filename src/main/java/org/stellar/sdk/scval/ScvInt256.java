package org.stellar.sdk.scval;

import com.google.common.primitives.Longs;
import java.math.BigInteger;
import java.util.Arrays;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.xdr.Int256Parts;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

@Value
@EqualsAndHashCode(callSuper = false)
public class ScvInt256 extends Scv {
  private static final SCValType TYPE = SCValType.SCV_I256;

  public static final BigInteger MIN_VALUE = BigInteger.valueOf(-2).pow(255);
  public static final BigInteger MAX_VALUE =
      BigInteger.valueOf(2).pow(255).subtract(BigInteger.ONE);

  BigInteger value;

  public ScvInt256(BigInteger value) {
    if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
      throw new IllegalArgumentException(
          String.format(
              "invalid value, expected between %s and %s, but got %s",
              MIN_VALUE, MAX_VALUE, value));
    }
    this.value = value;
  }

  @Override
  public SCVal toSCVal() {
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

  public static ScvInt256 fromSCVal(SCVal scVal) {
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

    BigInteger value = new BigInteger(fullBytes);
    return new ScvInt256(value);
  }

  @Override
  public SCValType getSCValType() {
    return SCValType.SCV_I256;
  }
}
